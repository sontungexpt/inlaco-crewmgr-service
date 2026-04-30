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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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
    if (criteria != null && StringUtils.hasText(criteria.getAccountId())) {
      return findAllEnrolledCourses(criteria, pageable);
    }
    return findAllCourses(criteria, pageable);
  }

  private Criteria buildCourseCriteria(CourseSearchCriteria criteria, String prefix) {
    List<Criteria> ands = new ArrayList<>();

    if (criteria == null) return new Criteria();

    String p = prefix == null ? "" : prefix;

    if (StringUtils.hasText(criteria.getKeyword())) {
      ands.add(
          new Criteria()
              .orOperator(
                  Criteria.where(p + "name").regex(criteria.getKeyword(), "i"),
                  Criteria.where(p + "achievedPosition").regex(criteria.getKeyword(), "i")));
    }

    Instant now = Instant.now();

    if (Boolean.TRUE.equals(criteria.getNonExpired())) {
      ands.add(Criteria.where(p + "endDate").gte(now));
    }

    if (Boolean.TRUE.equals(criteria.getRegistrationEnabled())) {
      ands.add(
          Criteria.where(p + "startRegistrationAt").lte(now).and(p + "endRegistrationAt").gte(now));
    }

    return ands.isEmpty() ? new Criteria() : new Criteria().andOperator(ands);
  }

  private Page<Course> findAllCourses(CourseSearchCriteria criteria, Pageable pageable) {
    Criteria query = where("deletedAt").exists(false);

    Criteria courseCriteria = buildCourseCriteria(criteria, "");

    if (courseCriteria != null) {
      query = new Criteria().andOperator(query, courseCriteria);
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

  private Page<Course> findAllEnrolledCourses(CourseSearchCriteria criteria, Pageable pageable) {
    ObjectId userId = new ObjectId(criteria.getAccountId());

    Criteria userCriteria = Criteria.where("userId").is(userId);
    Criteria courseCriteria = buildCourseCriteria(criteria, "course.");

    Aggregation aggregation =
        newAggregation(
            match(userCriteria),
            lookup(
                mongoOperations.getCollectionName(CourseEntity.class), "courseId", "_id", "course"),
            unwind("course"),
            match(courseCriteria),
            match(Criteria.where("course.deletedAt").exists(false)),
            facet(Aggregation.count().as(FacetResult.COUNT_KEY))
                .as(FacetResult.COUNT_FACET_NAME)
                .and(
                    replaceRoot("course"),
                    sort(pageable.getSort()),
                    skip(pageable.getOffset()),
                    limit(pageable.getPageSize()))
                .as(FacetResult.DATA_FACET_NAME));

    var result =
        mongoOperations
            .aggregate(aggregation, CourseMemberEntity.class, CourseEntityFacetResult.class)
            .getUniqueMappedResult();

    if (result == null) return Page.empty(pageable);

    return result.toPage(pageable).map(mapper::toCourse);
  }

  @Override
  @CachePut(value = "courses", key = "#result.id")
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
