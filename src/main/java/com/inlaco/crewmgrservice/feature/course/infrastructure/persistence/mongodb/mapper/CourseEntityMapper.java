package com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.course.domain.model.Course;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.entity.CourseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    componentModel = "spring")
public interface CourseEntityMapper {
  CourseEntity toCourseEntity(Course course);

  Course toCourse(CourseEntity entity);

  void updateFromCourse(Course course, @MappingTarget CourseEntity entity);
}
