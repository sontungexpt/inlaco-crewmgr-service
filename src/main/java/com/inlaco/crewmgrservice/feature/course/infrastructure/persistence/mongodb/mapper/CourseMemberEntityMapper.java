package com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.course.domain.model.CourseMember;
import com.inlaco.crewmgrservice.feature.course.infrastructure.persistence.mongodb.entity.CourseMemberEntity;
import com.inlaco.crewmgrservice.shared.mapstruct.config.CentralMapperConfig;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    config = CentralMapperConfig.class,
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring")
public interface CourseMemberEntityMapper {

  CourseMember toCourseMemeber(CourseMemberEntity entity);

  CourseMemberEntity toCourseMemberEntity(CourseMember course);

  @InheritConfiguration(name = "toCourseMemberEntity")
  void updateFromCourseMember(
      CourseMember courseMember, @MappingTarget CourseMemberEntity existing);
}
