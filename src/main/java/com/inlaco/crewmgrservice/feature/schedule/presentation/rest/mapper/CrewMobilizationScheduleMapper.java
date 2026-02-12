package com.inlaco.crewmgrservice.feature.schedule.presentation.rest.mapper;

import com.inlaco.crewmgrservice.feature.schedule.application.model.AssignedCrewDetail;
import com.inlaco.crewmgrservice.feature.schedule.application.model.CrewMobilizationScheduleDetail;
import com.inlaco.crewmgrservice.feature.schedule.domain.model.AssignedCrew;
import com.inlaco.crewmgrservice.feature.schedule.domain.model.CrewMobilizationSchedule;
import com.inlaco.crewmgrservice.feature.schedule.presentation.rest.dto.request.AssignedCrewRequest;
import com.inlaco.crewmgrservice.feature.schedule.presentation.rest.dto.request.NewCrewMobilizationScheduleRequest;
import com.inlaco.crewmgrservice.feature.schedule.presentation.rest.dto.response.AssignedCrewResponse;
import com.inlaco.crewmgrservice.feature.schedule.presentation.rest.dto.response.CrewMobilizationScheduleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CrewMobilizationScheduleMapper {

  CrewMobilizationSchedule toCrewMobilizationSchedule(NewCrewMobilizationScheduleRequest request);

  CrewMobilizationScheduleResponse toCrewMobilizationScheduleResponse(
      CrewMobilizationSchedule schedule);

  CrewMobilizationScheduleResponse toCrewMobilizationScheduleResponse(
      CrewMobilizationScheduleDetail schedule);

  AssignedCrew toAssignedCrew(AssignedCrewRequest request);

  AssignedCrewResponse toAssignedCrewResponse(AssignedCrew assignedCrew);

  AssignedCrewResponse toAssignedCrewResponse(AssignedCrewDetail assignedCrew);
}
