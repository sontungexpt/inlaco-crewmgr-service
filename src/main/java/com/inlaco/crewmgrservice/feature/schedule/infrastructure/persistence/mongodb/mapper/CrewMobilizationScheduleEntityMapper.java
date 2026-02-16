package com.inlaco.crewmgrservice.feature.schedule.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.schedule.domain.model.AssignedCrew;
import com.inlaco.crewmgrservice.feature.schedule.domain.model.CrewMobilizationSchedule;
import com.inlaco.crewmgrservice.feature.schedule.infrastructure.persistence.mongodb.entity.AssignedCrewEntity;
import com.inlaco.crewmgrservice.feature.schedule.infrastructure.persistence.mongodb.entity.CrewMobilizationScheduleEntity;
import com.inlaco.crewmgrservice.shared.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    config = CentralMapperConfig.class,
    componentModel = "spring")
public interface CrewMobilizationScheduleEntityMapper {

  CrewMobilizationSchedule toCrewMobilizationSchedule(CrewMobilizationScheduleEntity entity);

  CrewMobilizationScheduleEntity toCrewMobilizationScheduleEntity(
      CrewMobilizationSchedule schedule);

  AssignedCrewEntity toAssignedCrewEntity(AssignedCrew assignedCrew);

  AssignedCrew toAssignedCrew(AssignedCrewEntity entity);
}
