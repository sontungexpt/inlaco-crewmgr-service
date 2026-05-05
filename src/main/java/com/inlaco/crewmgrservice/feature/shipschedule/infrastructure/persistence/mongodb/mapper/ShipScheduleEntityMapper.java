package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.ShipScheduleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ShipScheduleEntityMapper {
  
  @Mapping(target = "id", ignore = true) // Let MongoDB generate ID
  ShipScheduleEntity toEntity(ShipSchedule domain);
  
  @Mapping(target = "id", source = "id") // Keep ID when mapping back
  ShipSchedule toDomain(ShipScheduleEntity entity);
  
  @Mapping(target = "id", ignore = true) // Don't update ID
  void updateEntity(@MappingTarget ShipScheduleEntity entity, ShipSchedule domain);
  
  @Named("entityToDomainWithoutId")
  @Mapping(target = "id", ignore = true) // For cases where we want to ignore ID
  ShipSchedule toDomainWithoutId(ShipScheduleEntity entity);
}
