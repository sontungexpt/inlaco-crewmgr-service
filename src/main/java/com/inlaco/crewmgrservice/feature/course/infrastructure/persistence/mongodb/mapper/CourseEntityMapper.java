package com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.course.domain.model.Course;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.entity.CourseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseEntityMapper {
  CourseEntity toEntity(Course course);

  Course toCourse(CourseEntity entity);
}
