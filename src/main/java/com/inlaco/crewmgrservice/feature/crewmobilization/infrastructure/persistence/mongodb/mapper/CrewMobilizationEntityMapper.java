package com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.AssignedCrew;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.entity.AssignedCrewEntity;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.entity.CrewMobilizationEntity;
import com.inlaco.crewmgrservice.shared.mapstruct.config.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    config = CentralMapperConfig.class,
    componentModel = "spring")
public interface CrewMobilizationEntityMapper {

  CrewMobilization toCrewMobilizationSchedule(CrewMobilizationEntity entity);

  CrewMobilizationEntity toCrewMobilizationScheduleEntity(CrewMobilization schedule);

  AssignedCrewEntity toAssignedCrewEntity(AssignedCrew assignedCrew);

  AssignedCrew toAssignedCrew(AssignedCrewEntity entity);
}
