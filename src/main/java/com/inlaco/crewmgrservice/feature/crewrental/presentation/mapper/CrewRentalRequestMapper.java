package com.inlaco.crewmgrservice.feature.crewrental.presentation.mapper;

import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.feature.crewrental.presentation.dto.CrewRentalRequestResponse;
import com.inlaco.crewmgrservice.feature.crewrental.presentation.dto.NewCrewRentalRequest;
import com.inlaco.crewmgrservice.shared.mapstruct.mapper.AssetResponseMapper;
import com.inlaco.crewmgrservice.shared.mapstruct.mapper.ShipInfoResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {AssetResponseMapper.class, ShipInfoResponseMapper.class})
public abstract class CrewRentalRequestMapper {

  @Mapping(target = "shipInfo.image", ignore = true)
  @Mapping(target = "detailFile", ignore = true)
  public abstract CrewRentalRequest toCrewRentalRequest(NewCrewRentalRequest newRequest);

  public abstract CrewRentalRequestResponse toCrewRentalRequestResponse(CrewRentalRequest request);
}
