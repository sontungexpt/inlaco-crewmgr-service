package com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationAssignment;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.entity.CrewMobilizationAssignmentEntity;
import com.inlaco.crewmgrservice.shared.mapstruct.config.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    config = CentralMapperConfig.class,
    componentModel = "spring")
public interface CrewMobilizationAssignmentEntityMapper {

  CrewMobilizationAssignmentEntity toEntity(CrewMobilizationAssignment assignedCrew);

  CrewMobilizationAssignment toDomain(CrewMobilizationAssignmentEntity entity);

  CrewMobilizationAssignmentEntity updateFromCrewMobilizationAssignment(
      CrewMobilizationAssignment assignment,
      @MappingTarget CrewMobilizationAssignmentEntity entity);
}
