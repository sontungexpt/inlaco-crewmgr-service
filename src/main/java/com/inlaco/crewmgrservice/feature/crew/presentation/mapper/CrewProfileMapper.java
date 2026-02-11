package com.inlaco.crewmgrservice.feature.crew.presentation.mapper;

import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crew.presentation.dto.request.NewCrewProfile;
import com.inlaco.crewmgrservice.feature.crew.presentation.dto.response.CrewProfileResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CrewProfileMapper {

  CrewProfileResponse toCrewProfileResponse(CrewProfile profile);

  CrewProfile toCrewProfile(NewCrewProfile newCrewProfile);
}
