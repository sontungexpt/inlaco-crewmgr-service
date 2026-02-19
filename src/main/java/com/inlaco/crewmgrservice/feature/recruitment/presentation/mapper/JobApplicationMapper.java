package com.inlaco.crewmgrservice.feature.recruitment.presentation.mapper;

import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.recruitment.presentation.dto.request.NewJobApplication;
import com.inlaco.crewmgrservice.feature.recruitment.presentation.dto.response.JobApplicationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobApplicationMapper {

  JobApplication toJobApplication(NewJobApplication newJobApplication);

  JobApplicationResponse toJobApplicationResponse(JobApplication jobApplication);
}
