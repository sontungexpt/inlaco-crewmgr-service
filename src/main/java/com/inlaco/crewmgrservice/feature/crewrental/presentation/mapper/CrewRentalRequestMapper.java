package com.inlaco.crewmgrservice.feature.crewrental.presentation.mapper;

import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.feature.crewrental.presentation.dto.CrewRentalRequestResponse;
import com.inlaco.crewmgrservice.feature.crewrental.presentation.dto.NewCrewRentalRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CrewRentalRequestMapper {
  CrewRentalRequest toCrewRentalRequest(NewCrewRentalRequest newRequest);

  CrewRentalRequestResponse toCrewRentalRequestResponse(CrewRentalRequest request);
}
