package com.inlaco.crewmgrservice.feature.crew.presentation.mapper;

import com.inlaco.crewmgrservice.feature.crew.application.model.UpdateCrewProfileAdminCommand;
import com.inlaco.crewmgrservice.feature.crew.application.model.UpdateCrewProfileCrewCommand;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crew.presentation.dto.request.NewCrewProfile;
import com.inlaco.crewmgrservice.feature.crew.presentation.dto.request.update.CrewProfilePatchRequest;
import com.inlaco.crewmgrservice.feature.crew.presentation.dto.response.CrewProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CrewProfileMapper {

  public abstract CrewProfileResponse toCrewProfileResponse(CrewProfile profile);

  public abstract CrewProfile toCrewProfile(NewCrewProfile newCrewProfile);

  public abstract UpdateCrewProfileAdminCommand toUpdateCrewProfileAdminCommand(
      CrewProfilePatchRequest request);

  public abstract UpdateCrewProfileCrewCommand toUpdateCrewProfileCrewCommand(
      CrewProfilePatchRequest request);
}
