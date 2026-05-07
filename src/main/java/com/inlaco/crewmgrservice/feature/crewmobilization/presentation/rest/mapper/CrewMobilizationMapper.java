package com.inlaco.crewmgrservice.feature.crewmobilization.presentation.rest.mapper;

import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.AssignedCrewDetail;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationDetail;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.AssignedCrew;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;
import com.inlaco.crewmgrservice.feature.crewmobilization.presentation.rest.dto.request.AssignedCrewRequest;
import com.inlaco.crewmgrservice.feature.crewmobilization.presentation.rest.dto.request.CreateCrewMobilizationRequest;
import com.inlaco.crewmgrservice.feature.crewmobilization.presentation.rest.dto.response.AssignedCrewResponse;
import com.inlaco.crewmgrservice.feature.crewmobilization.presentation.rest.dto.response.CrewMobilizationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CrewMobilizationMapper {

  @Mapping(target = "shipInfo.image", ignore = true)
  CrewMobilization toCrewMobilizationSchedule(CreateCrewMobilizationRequest request);

  CrewMobilizationResponse toCrewMobilizationScheduleResponse(CrewMobilization schedule);

  CrewMobilizationResponse toCrewMobilizationScheduleResponse(CrewMobilizationDetail schedule);

  AssignedCrew toAssignedCrew(AssignedCrewRequest request);

  AssignedCrewResponse toAssignedCrewResponse(AssignedCrew assignedCrew);

  AssignedCrewResponse toAssignedCrewResponse(AssignedCrewDetail assignedCrew);
}
