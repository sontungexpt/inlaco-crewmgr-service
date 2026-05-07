package com.inlaco.crewmgrservice.feature.shipschedule.presentation.mapper;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request.CreateShipScheduleRequest;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request.UpdateShipScheduleRequest;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.ShipScheduleResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShipScheduleMapper {

  @Mapping(target = "id", ignore = true) // MongoDB will auto-generate ID
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  ShipSchedule toDomain(CreateShipScheduleRequest request);

  @Mapping(target = "id", ignore = true) // Don't update ID
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  void updateDomain(@MappingTarget ShipSchedule domain, UpdateShipScheduleRequest request);

  ShipScheduleResponse toResponse(ShipSchedule domain);

  List<ShipScheduleResponse> toResponseList(List<ShipSchedule> domains);
}
