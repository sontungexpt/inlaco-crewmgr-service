package com.inlaco.crewmgrservice.feature.crew.presentation.mapper;

import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crew.presentation.dto.request.NewCrewProfile;
import com.inlaco.crewmgrservice.feature.crew.presentation.dto.response.CrewProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CrewProfileMapper {

  CrewProfileResponse toCrewProfileResponse(CrewProfile profile);

  CrewProfile toCrewProfile(NewCrewProfile newCrewProfile);
}
