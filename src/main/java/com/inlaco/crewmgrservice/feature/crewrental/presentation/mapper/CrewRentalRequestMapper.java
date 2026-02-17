package com.inlaco.crewmgrservice.feature.crewrental.presentation.mapper;

import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.feature.crewrental.presentation.dto.CrewRentalRequestResponse;
import com.inlaco.crewmgrservice.feature.crewrental.presentation.dto.NewCrewRentalRequest;
import com.inlaco.crewmgrservice.shared.mapstruct.mapper.AssetResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = AssetResponseMapper.class)
public interface CrewRentalRequestMapper {
  CrewRentalRequest toCrewRentalRequest(NewCrewRentalRequest newRequest);

  CrewRentalRequestResponse toCrewRentalRequestResponse(CrewRentalRequest request);
}
