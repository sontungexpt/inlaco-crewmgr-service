package com.inlaco.crewmgrservice.feature.recruitment.presentation.mapper;

import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.recruitment.presentation.dto.request.NewJobApplication;
import com.inlaco.crewmgrservice.feature.recruitment.presentation.dto.response.JobApplicationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {

  JobApplication toJobApplication(NewJobApplication newJobApplication);

  JobApplicationResponse toJobApplicationResponse(JobApplication jobApplication);
}
