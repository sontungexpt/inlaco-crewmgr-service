package com.inlaco.crewmgrservice.feature.crew.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crew.infrastructure.persistence.mongodb.entity.CrewProfileEntity;
import com.inlaco.crewmgrservice.shared.mapper.CentralMapperConfig;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    config = CentralMapperConfig.class,
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring")
public interface CrewProfileEntityMapper {

  CrewProfile toCrewProfile(CrewProfileEntity entity);

  CrewProfileEntity toCrewProfileEntity(CrewProfile profile);

  @InheritConfiguration(name = "toCrewProfileEntity")
  void updateFromCrewProfile(CrewProfile profile, @MappingTarget CrewProfileEntity entity);
}
