package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.entity.JobApplicationEntity;
import com.inlaco.crewmgrservice.shared.mapper.ObjectIdMapper;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    componentModel = "spring",
    uses = ObjectIdMapper.class)
public interface JobApplicationEntityMapper {

  @Mapping(target = "accountId", source = "accountId", qualifiedByName = "objectIdToString")
  @Mapping(
      target = "recruitmentPostId",
      source = "recruitmentPostId",
      qualifiedByName = "objectIdToString")
  JobApplication toJobApplication(JobApplicationEntity jobApplicationEntity);

  @Mapping(target = "accountId", source = "accountId", qualifiedByName = "stringToObjectId")
  @Mapping(
      target = "recruitmentPostId",
      source = "recruitmentPostId",
      qualifiedByName = "stringToObjectId")
  JobApplicationEntity toJobApplicationEntity(JobApplication jobApplication);

  @InheritConfiguration(name = "toJobApplicationEntity")
  JobApplicationEntity updateFromJobApplication(
      JobApplication jobApplication, @MappingTarget JobApplicationEntity entity);
}
