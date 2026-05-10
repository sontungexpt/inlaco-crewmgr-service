package com.inlaco.crewmgrservice.feature.shipschedule.presentation.mapper;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request.CreateShipScheduleRequest;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.ShipScheduleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShipScheduleMapper {

  ShipSchedule toShipSchedule(CreateShipScheduleRequest request);

  ShipScheduleResponse toShipScheduleResponse(ShipSchedule domain);
}
