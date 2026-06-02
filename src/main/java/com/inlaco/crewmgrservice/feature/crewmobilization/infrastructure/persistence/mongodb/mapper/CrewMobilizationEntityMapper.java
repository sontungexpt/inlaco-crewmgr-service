package com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.entity.CrewMobilizationEntity;
import com.inlaco.crewmgrservice.shared.mapstruct.config.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    config = CentralMapperConfig.class,
    componentModel = "spring")
public interface CrewMobilizationEntityMapper {

  CrewMobilization toCrewMobilization(CrewMobilizationEntity entity);

  CrewMobilizationEntity toCrewMobilizationEntity(CrewMobilization schedule);

  CrewMobilizationEntity updateFromCrewMobilization(
      CrewMobilization schedule, @MappingTarget CrewMobilizationEntity entity);
}
