package com.inlaco.crewmgrservice.feature.course.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.common.model.FacetResult;
import com.inlaco.crewmgrservice.feature.course.model.Course;
import com.inlaco.crewmgrservice.feature.course.model.CourseMember;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseEnrollment;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseMemberInfo;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.utils.PageableUtils;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomCourseRepository {

  private final MongoTemplate mongoTemplate;

  public Page<CourseEnrollment> findCourseEnrollments(String userId, Pageable pageable) {
    log.debug("Get course enrollments for user {}", userId);
    Aggregation aggregation =
        Aggregation.newAggregation(
            Aggregation.match(Criteria.where("userId").is(new ObjectId(userId))),
            Aggregation.facet(Aggregation.count().as("totalCourses"))
                .as(FacetResult.getCountFacetName())
                .and(
                    lookup(
                        mongoTemplate.getCollectionName(Course.class),
                        "courseId",
                        "_id",
                        "courses"),
                    Aggregation.project().and("courses").arrayElementAt(0).as("course"),
                    Aggregation.project()
                        .andExpression("course._id")
                        .as("id")
                        .andExpression("course.name")
                        .as("name")
                        .andExpression("course.slug")
                        .as("slug")
                        .andExpression("course.limitStudent")
                        .as("limitStudent")
                        .andExpression("course.description")
                        .as("description")
                        .andExpression("course.teacherName")
                        .as("teacherName")
                        .andExpression("course.startDate")
                        .as("startDate")
                        .andExpression("course.endDate")
                        .as("endDate")
                        .andExpression("course.status")
                        .as("status")
                        .andExpression("course.note")
                        .as("note")
                        .andExpression("course.createdAt")
                        .as("createdAt")
                        .andExpression("course.updatedAt")
                        .as("updatedAt")
                        .andExpression("course.deletedAt")
                        .as("deletedAt")
                        .andExpression("course.reopenedBasedOn")
                        .as("reopenedBasedOn")
                        .andExpression("course.createdBy")
                        .as("createdBy")
                        .andExpression("course.updatedBy")
                        .as("updatedBy"),
                    sort(pageable.getSort()),
                    skip(pageable.getOffset()),
                    limit(pageable.getPageSize()))
                .as(FacetResult.getDataFacetName()));

    var result =
        mongoTemplate
            .aggregate(aggregation, CourseMember.class, CourseEnrollmentFacetResult.class)
            .getUniqueMappedResult();

    return new PageImpl<>(result.getDataFacet(), pageable, result.getCount("totalCourses"));
  }

  public Page<Course> findByNonExpiredCourses(Pageable p) {
    var pageable = PageableUtils.extendDefaultSort(p);
    log.debug("Fetching non expired courses with pagination");

    Aggregation aggregation =
        Aggregation.newAggregation(
            match(Criteria.where("deleted").is(false).and("endDate").gte(Instant.now())),
            Aggregation.facet(Aggregation.count().as("totalCourses"))
                .as(FacetResult.getCountFacetName())
                .and(
                    sort(pageable.getSort()),
                    skip(pageable.getOffset()),
                    limit(pageable.getPageSize()))
                .as(FacetResult.getDataFacetName()));

    var result =
        mongoTemplate
            .aggregate(aggregation, Course.class, CourseFacetResult.class)
            .getUniqueMappedResult();

    return new PageImpl<>(result.getDatas(), pageable, result.getCount("totalCourses"));
  }

  public Page<Course> searchCourse(String keyword, boolean nonExpired, Pageable p) {
    var pageable = PageableUtils.extendDefaultSort(p);
    log.debug("Searching course by name with pagination");

    var query =
        Criteria.where("deleted")
            .is(false)
            .orOperator(
                Criteria.where("name").regex(keyword, "i"),
                Criteria.where("achievedPosition").regex(keyword, "i"));

    if (nonExpired) {
      query.and("endDate").gte(Instant.now());
    }

    Aggregation aggregation =
        Aggregation.newAggregation(
            match(query),
            Aggregation.facet(Aggregation.count().as("totalCourses"))
                .as(FacetResult.getCountFacetName())
                .and(
                    sort(pageable.getSort()),
                    skip(pageable.getOffset()),
                    limit(pageable.getPageSize()))
                .as(FacetResult.getDataFacetName()));

    var result =
        mongoTemplate
            .aggregate(aggregation, Course.class, CourseFacetResult.class)
            .getUniqueMappedResult();

    return new PageImpl<>(result.getDatas(), pageable, result.getCount("totalCourses"));
  }

  public Page<CourseMemberInfo> findEnrolledCourseSailors(String courseId, Pageable p) {
    var pageable = PageableUtils.extendDefaultSort(p);
    log.debug("Fetching non expired courses with pagination");

    Aggregation aggregation =
        Aggregation.newAggregation(
            match(Criteria.where("courseId").is(new ObjectId(courseId))),
            Aggregation.facet(Aggregation.count().as("totalSailors"))
                .as(FacetResult.getCountFacetName())
                .and(
                    lookup(
                        mongoTemplate.getCollectionName(SailorProfile.class),
                        "accountId",
                        "userId",
                        "sailor"),
                    project().and("sailor").arrayElementAt(0).as("sailorProfile"),
                    sort(pageable.getSort()),
                    skip(pageable.getOffset()),
                    limit(pageable.getPageSize()))
                .as(FacetResult.getDataFacetName()));

    var result =
        mongoTemplate
            .aggregate(aggregation, CourseMember.class, CourseMemberInfoFacetResult.class)
            .getUniqueMappedResult();

    return new PageImpl<>(result.getDatas(), pageable, result.getCount("totalSailors"));
  }

  class CourseMemberInfoFacetResult extends FacetResult<CourseMemberInfo> {
    public CourseMemberInfoFacetResult(
        List<CourseMemberInfo> dataFacet, List<Map<String, Object>> countFacet) {
      super(dataFacet, countFacet);
    }
  }

  class CourseEnrollmentFacetResult extends FacetResult<CourseEnrollment> {
    public CourseEnrollmentFacetResult(
        List<CourseEnrollment> dataFacet, List<Map<String, Object>> countFacet) {
      super(dataFacet, countFacet);
    }
  }

  class CourseFacetResult extends FacetResult<Course> {
    public CourseFacetResult(List<Course> dataFacet, List<Map<String, Object>> countFacet) {
      super(dataFacet, countFacet);
    }
  }
}
