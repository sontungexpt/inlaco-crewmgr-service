package com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.course.domain.model.CourseMember;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.entity.CourseMemberEntity;
import com.inlaco.crewmgrservice.shared.mapper.ObjectIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ObjectIdMapper.class)
public interface CourseMemberEntityMapper {

  @Mapping(target = "courseId", source = "courseId", qualifiedByName = "stringToObjectId")
  @Mapping(target = "userId", source = "userId", qualifiedByName = "stringToObjectId")
  CourseMemberEntity totEntity(CourseMember course);

  @Mapping(target = "courseId", source = "courseId", qualifiedByName = "objectIdToString")
  @Mapping(target = "userId", source = "userId", qualifiedByName = "objectIdToString")
  CourseMember toDomain(CourseMemberEntity entity);
}
