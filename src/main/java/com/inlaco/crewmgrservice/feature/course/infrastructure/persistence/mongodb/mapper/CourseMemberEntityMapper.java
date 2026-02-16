package com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.course.domain.model.CourseMember;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.entity.CourseMemberEntity;
import com.inlaco.crewmgrservice.shared.mapper.CentralMapperConfig;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = CentralMapperConfig.class, componentModel = "spring")
public interface CourseMemberEntityMapper {

  CourseMember toDomain(CourseMemberEntity entity);

  CourseMemberEntity toEntity(CourseMember course);

  @InheritConfiguration(name = "toEntity")
  void updateFromDomain(CourseMember courseMember, @MappingTarget CourseMemberEntity existing);
}
