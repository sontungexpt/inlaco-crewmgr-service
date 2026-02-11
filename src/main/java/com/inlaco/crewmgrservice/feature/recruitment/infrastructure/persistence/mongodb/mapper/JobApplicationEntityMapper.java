package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.entity.JobApplicationEntity;
import com.inlaco.crewmgrservice.shared.mapper.ObjectIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ObjectIdMapper.class)
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
}
