package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.ShipScheduleCrewAssignmentEntity;
import com.inlaco.crewmgrservice.shared.mapstruct.config.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    config = CentralMapperConfig.class,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    componentModel = "spring")
public interface ShipScheduleCrewAssignmentEntityMapper {

  ShipScheduleCrewAssignment toShipScheduleCrewAssignment(ShipScheduleCrewAssignmentEntity entity);

  ShipScheduleCrewAssignmentEntity toShipScheduleCrewAssignmentEntity(
      ShipScheduleCrewAssignment domain);

  ShipScheduleCrewAssignmentEntity updateFromShipScheduleCrewAssignment(
      ShipScheduleCrewAssignment domain, @MappingTarget ShipScheduleCrewAssignmentEntity entity);
}
