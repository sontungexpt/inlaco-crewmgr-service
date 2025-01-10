package com.inlaco.crewmgrservice.feature.course.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.common.model.FacetResult;
import com.inlaco.crewmgrservice.feature.course.model.Course;
import com.inlaco.crewmgrservice.feature.course.model.CourseMemberTracking;
import com.inlaco.crewmgrservice.feature.course.model.dto.CourseEnrollment;
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

  private MongoTemplate mongoTemplate;

  public Page<CourseEnrollment> getCourseEnrollments(String userId, Pageable pageable) {
    log.debug("Get course enrollments for user {}", userId);
    Aggregation aggregation =
        Aggregation.newAggregation(
            Aggregation.match(Criteria.where("userId").is(new ObjectId(userId))),
            Aggregation.facet(Aggregation.count().as("totalCourses"))
                .as(FacetResult.getCountFacetName())
                .and(
                    match(Criteria.where("userId").is(new ObjectId(userId))),
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
            .aggregate(aggregation, CourseMemberTracking.class, FacetResultCourseEnrollment.class)
            .getUniqueMappedResult();

    return new PageImpl<>(result.getDataFacet(), pageable, result.getCount("totalCourses"));
  }

  class FacetResultCourseEnrollment extends FacetResult<CourseEnrollment> {

    public FacetResultCourseEnrollment(
        List<CourseEnrollment> dataFacet, List<Map<String, Object>> countFacet) {
      super(dataFacet, countFacet);
    }
  }
}
