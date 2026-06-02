package com.inlaco.crewmgrservice.feature.ship.presentation.mapper;

import com.inlaco.crewmgrservice.feature.ship.domain.model.Ship;
import com.inlaco.crewmgrservice.feature.ship.presentation.rest.dto.request.CreateShipRequest;
import com.inlaco.crewmgrservice.feature.ship.presentation.rest.dto.request.UpdateShipRequest;
import com.inlaco.crewmgrservice.feature.ship.presentation.rest.dto.response.ShipResponse;
import com.inlaco.crewmgrservice.shared.mapstruct.mapper.AssetResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {AssetResponseMapper.class})
public interface ShipMapper {
  ShipMapper INSTANCE = Mappers.getMapper(ShipMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Ship toDomain(CreateShipRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Ship toDomain(UpdateShipRequest request);

  ShipResponse toResponse(Ship ship);
}
