package com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.inlaco.crewmgrservice.feature.course.application.model.CourseSearchCriteria;
import com.inlaco.crewmgrservice.feature.course.application.port.out.CourseRepository;
import com.inlaco.crewmgrservice.feature.course.domain.model.Course;
import com.inlaco.crewmgrservice.feature.course.domain.model.UserCourse;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.entity.CourseEntity;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.entity.CourseMemberEntity;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.mapper.CourseEntityMapper;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.repository.CourseMongoRepository;
import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.aggregation.FacetResult;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CourseRepositoryAdapter implements CourseRepository {

  private final CourseMongoRepository repository;
  private final MongoOperations mongoOperations;
  private final AuditorAware<ObjectId> auditorAware;
  private final CourseEntityMapper mapper;

  @Override
  @Cacheable(value = "courses", key = "#id", unless = "#result == null")
  public Optional<Course> findById(String id) {
    return repository.findByIdAndDeletedAtIsNull(id).map(mapper::toCourse);
  }

  @Override
  public Page<Course> findAll(Pageable pageable) {
    return repository.findByDeletedAtIsNull(pageable).map(mapper::toCourse);
  }

  @Override
  public Page<Course> findAll(@Nullable CourseSearchCriteria criteria, Pageable pageable) {
    var query = where("deletedAt").exists(false);

    if (criteria != null) {
      if (StringUtils.hasText(criteria.getKeyword())) {
        query.orOperator(
            where("name").regex(criteria.getKeyword(), "i"),
            where("achievedPosition").regex(criteria.getKeyword(), "i"));
      }

      Instant now = Instant.now();

      if (Boolean.TRUE.equals(criteria.getNonExpired())) {
        query.and("endDate").gte(now);
      }

      if (Boolean.TRUE.equals(criteria.getRegistrationEnabled())) {
        query.and("startRegistrationAt").lte(now).and("endRegistrationAt").gte(now);
      }
    }

    Aggregation aggregation =
        newAggregation(
            match(query),
            facet(Aggregation.count().as(FacetResult.COUNT_KEY))
                .as(FacetResult.COUNT_FACET_NAME)
                .and(
                    sort(pageable.getSort()),
                    skip(pageable.getOffset()),
                    limit(pageable.getPageSize()))
                .as(FacetResult.DATA_FACET_NAME));

    return mongoOperations
        .aggregate(aggregation, CourseEntity.class, CourseEntityFacetResult.class)
        .getUniqueMappedResult()
        .toPage(pageable)
        .map(mapper::toCourse);
  }

  @Override
  @CacheEvict(value = "courses", key = "#course.id", condition = "#course.id != null")
  public Course save(Course course) {
    String id = course.getId();
    if (id == null) {
      return mapper.toCourse(repository.insert(mapper.toCourseEntity(course)));
    }
    CourseEntity entity =
        repository
            .findById(id)
            .map(
                existing -> {
                  mapper.updateFromCourse(course, existing);
                  return existing;
                })
            .orElseGet(() -> mapper.toCourseEntity(course));
    return mapper.toCourse(repository.save(entity));
  }

  @Override
  @CacheEvict(value = "courses", key = "#id")
  public void deleteById(String id) {
    mongoOperations.updateFirst(
        Query.query(Criteria.where("_id").is(id).and("deletedAt").exists(false)),
        new Update()
            .addToSet("deletedBy", auditorAware.getCurrentAuditor().orElse(null))
            .addToSet("deletedAt", Instant.now()),
        CourseEntity.class);
  }

  @Override
  public Page<UserCourse> findAllEnrolled(String userId, Pageable pageable) {
    log.debug("Fetching enrolled courses with pagination");

    Aggregation aggregation =
        newAggregation(
            match(Criteria.where("userId").is(new ObjectId(userId))),
            facet(Aggregation.count().as(FacetResult.COUNT_KEY))
                .as(FacetResult.COUNT_FACET_NAME)
                .and(
                    lookup(
                        mongoOperations.getCollectionName(CourseEntity.class),
                        "courseId",
                        "_id",
                        "courses"),
                    project()
                        .and("courses")
                        .arrayElementAt(0)
                        .as("course")
                        .and("createdAt")
                        .as("enrolledAt"),
                    sort(pageable.getSort()),
                    skip(pageable.getOffset()),
                    limit(pageable.getPageSize()))
                .as(FacetResult.DATA_FACET_NAME));
    var raw =
        mongoOperations
            .aggregate(aggregation, CourseMemberEntity.class, Document.class)
            .getUniqueMappedResult();

    if (raw == null) return Page.empty(pageable);

    MongoConverter converter = mongoOperations.getConverter();

    // 2️⃣ Extract count
    long total =
        raw.getList(FacetResult.COUNT_FACET_NAME, Document.class).stream()
            .findFirst()
            .map(d -> d.get(FacetResult.COUNT_KEY))
            .filter(Number.class::isInstance)
            .map(Number.class::cast)
            .map(Number::longValue)
            .orElse(0L);

    // 3️⃣ Extract data + convert
    List<UserCourse> data =
        raw.getList(FacetResult.DATA_FACET_NAME, Document.class).stream()
            .map(
                doc -> {
                  CourseEntity courseEntity =
                      converter.read(CourseEntity.class, (Document) doc.get("course"));
                  UserCourse userCourse = converter.read(UserCourse.class, doc);
                  userCourse.setCourse(mapper.toCourse(courseEntity));
                  return userCourse;
                })
            .toList();

    // 4️⃣ Return page
    return new PageImpl<>(data, pageable, total);
  }

  // public Page<CourseMemberInfoResponse> findEnrolledCourseSailors(String courseId, Pageable p) {
  //   var pageable = PageableUtils.extendDefaultSort(p);
  //   log.debug("Fetching non expired courses with pagination");

  //   Aggregation aggregation =
  //       Aggregation.newAggregation(
  //           match(Criteria.where("courseId").is(new ObjectId(courseId))),
  //           Aggregation.facet(Aggregation.count().as(FacetResult.getCountFacetName()))
  //               .as(FacetResult.getCountFacetName())
  //               .and(
  //                   sort(pageable.getSort()),
  //                   skip(pageable.getOffset()),
  //                   limit(pageable.getPageSize()),
  //                   lookup(
  //                       mongoTemplate.getCollectionName(SailorProfile.class),
  //                       "userId",
  //                       "accountId",
  //                       "sailorProfile"),
  //                   project().and("sailorProfile").arrayElementAt(0).as("sailorProfile"))
  //               .as(FacetResult.getDataFacetName()));

  //   var result =
  //       mongoTemplate
  //           .aggregate(aggregation, CourseMember.class, UserCourseFacetResult.class)
  //           .getUniqueMappedResult();

  //   return result.toPage(pageable);
  // }

  static class CourseEntityFacetResult extends FacetResult<CourseEntity> {}
}
