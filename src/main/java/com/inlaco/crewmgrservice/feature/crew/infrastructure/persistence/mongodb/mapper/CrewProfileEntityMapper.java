package com.inlaco.crewmgrservice.feature.crew.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crew.infrastructure.persistence.mongodb.entity.CrewProfileEntity;
import com.inlaco.crewmgrservice.shared.mapper.ObjectIdMapper;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring",
    uses = ObjectIdMapper.class)
public interface CrewProfileEntityMapper {

  @Mapping(target = "accountId", source = "accountId", qualifiedByName = "objectIdToString")
  CrewProfile toCrewProfile(CrewProfileEntity entity);

  @Mapping(target = "accountId", source = "accountId", qualifiedByName = "stringToObjectId")
  CrewProfileEntity toCrewProfileEntity(CrewProfile profile);

  @InheritConfiguration(name = "toCrewProfileEntity")
  void updateFromCrewProfile(CrewProfile profile, @MappingTarget CrewProfileEntity entity);
}
