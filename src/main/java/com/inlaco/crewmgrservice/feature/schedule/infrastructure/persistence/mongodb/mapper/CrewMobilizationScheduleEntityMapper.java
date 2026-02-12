package com.inlaco.crewmgrservice.feature.schedule.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.schedule.domain.model.AssignedCrew;
import com.inlaco.crewmgrservice.feature.schedule.domain.model.CrewMobilizationSchedule;
import com.inlaco.crewmgrservice.feature.schedule.infrastructure.persistence.mongodb.entity.AssignedCrewEntity;
import com.inlaco.crewmgrservice.feature.schedule.infrastructure.persistence.mongodb.entity.CrewMobilizationScheduleEntity;
import com.inlaco.crewmgrservice.shared.mapper.ObjectIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ObjectIdMapper.class)
public interface CrewMobilizationScheduleEntityMapper {

  CrewMobilizationSchedule toCrewMobilizationSchedule(CrewMobilizationScheduleEntity entity);

  CrewMobilizationScheduleEntity toCrewMobilizationScheduleEntity(
      CrewMobilizationSchedule schedule);

  @Mapping(target = "id", source = "id", qualifiedByName = "stringToObjectId")
  AssignedCrewEntity toAssignedCrewEntity(AssignedCrew assignedCrew);

  @Mapping(target = "id", source = "id", qualifiedByName = "objectIdToString")
  AssignedCrew toAssignedCrew(AssignedCrewEntity entity);
}
