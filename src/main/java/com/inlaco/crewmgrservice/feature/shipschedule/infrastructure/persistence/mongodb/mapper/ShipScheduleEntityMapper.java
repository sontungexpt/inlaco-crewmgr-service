package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.ShipScheduleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShipScheduleEntityMapper {

  ShipScheduleEntity toEntity(ShipSchedule domain);

  ShipSchedule toDomain(ShipScheduleEntity entity);

  @Mapping(target = "id", ignore = true) // For cases where we want to ignore ID
  ShipSchedule toDomainWithoutId(ShipScheduleEntity entity);

  @Mapping(target = "id", ignore = true) // Don't update ID
  void updateFromShipSchedule(ShipSchedule domain, @MappingTarget ShipScheduleEntity entity);
}
