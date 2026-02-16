package com.inlaco.crewmgrservice.feature.crewrental.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.feature.crewrental.infrastructure.persistence.mongodb.entity.CrewRentalRequestEntity;
import com.inlaco.crewmgrservice.shared.mapper.CentralMapperConfig;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring",
    config = CentralMapperConfig.class)
public interface CrewRentalRequestEntityMapper {

  CrewRentalRequest toCrewRentalRequest(CrewRentalRequestEntity entity);

  CrewRentalRequestEntity toCrewRentalRequestEntity(CrewRentalRequest model);

  @InheritConfiguration(name = "toCrewRentalRequestEntity")
  void updateFromCrewRentalRequest(
      CrewRentalRequest source, @MappingTarget CrewRentalRequestEntity target);
}
