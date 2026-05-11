package com.inlaco.crewmgrservice.feature.shipschedule.presentation.mapper;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request.CreateShipScheduleRequest;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.ShipScheduleResponse;
import com.inlaco.crewmgrservice.shared.mapstruct.config.CentralMapperConfig;
import com.inlaco.crewmgrservice.shared.mapstruct.mapper.ShipInfoResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    config = CentralMapperConfig.class,
    uses = {ShipInfoResponseMapper.class})
public interface ShipScheduleMapper {

  ShipSchedule toShipSchedule(CreateShipScheduleRequest request);

  ShipScheduleResponse toShipScheduleResponse(ShipSchedule domain);

  ShipScheduleCrewAssignment toShipScheduleCrewAssignment(
      CreateShipScheduleRequest.AssignedCrew entity);
}
