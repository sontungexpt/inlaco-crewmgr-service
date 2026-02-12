package com.inlaco.crewmgrservice.feature.crewrental.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.feature.crewrental.infrastructure.persistence.mongodb.entity.CrewRentalRequestEntity;
import com.inlaco.crewmgrservice.shared.mapper.ObjectIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ObjectIdMapper.class)
public interface CrewRentalRequestEntityMapper {

  @Mapping(target = "reviewedBy", source = "reviewedBy", qualifiedByName = "objectIdToString")
  @Mapping(target = "contractId", source = "contractId", qualifiedByName = "objectIdToString")
  CrewRentalRequest toCrewRentalRequest(CrewRentalRequestEntity entity);

  @Mapping(target = "reviewedBy", source = "reviewedBy", qualifiedByName = "stringToObjectId")
  @Mapping(target = "contractId", source = "contractId", qualifiedByName = "stringToObjectId")
  CrewRentalRequestEntity toCrewRentalRequestEntity(CrewRentalRequest model);
}
