package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.entity.JobApplicationEntity;
import com.inlaco.crewmgrservice.shared.mapstruct.config.CentralMapperConfig;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    componentModel = "spring",
    config = CentralMapperConfig.class)
public interface JobApplicationEntityMapper {

  JobApplication toJobApplication(JobApplicationEntity jobApplicationEntity);

  JobApplicationEntity toJobApplicationEntity(JobApplication jobApplication);

  @InheritConfiguration(name = "toJobApplicationEntity")
  JobApplicationEntity updateFromJobApplication(
      JobApplication jobApplication, @MappingTarget JobApplicationEntity entity);
}
