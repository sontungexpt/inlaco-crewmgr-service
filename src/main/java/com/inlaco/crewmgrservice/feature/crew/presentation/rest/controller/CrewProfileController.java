package com.inlaco.crewmgrservice.feature.crew.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.crew.application.model.CrewProfileSearchCriteria;
import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crew.presentation.dto.request.update.CrewProfilePatchRequest;
import com.inlaco.crewmgrservice.feature.crew.presentation.dto.response.CrewProfileResponse;
import com.inlaco.crewmgrservice.feature.crew.presentation.mapper.CrewProfileMapper;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.Filter;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.infrastructure.web.validation.annotation.ObjectId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crew-profiles")
@Tag(name = "Crew Profile", description = "Sailor management")
public class CrewProfileController {

  private final CrewUseCase crewUseCase;
  private final CrewProfileMapper crewProfileMapper;

  @Operation(
      summary = "Find crew profile by id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/{profileId}")
  @RolesAllowed("ADMIN")
  public CrewProfileResponse getCrewProfile(@ObjectId @PathVariable("profileId") String profileId) {
    return crewProfileMapper.toCrewProfileResponse(crewUseCase.getProfile(profileId));
  }

  @Operation(
      summary = "Update crew profile",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
  @RolesAllowed({"ADMIN", "SAILOR"})
  public CrewProfile updateCrewProfile(
      @CurrentUser User user,
      @PathVariable("id") @ObjectId String sailorId,
      @Valid @RequestBody CrewProfilePatchRequest patch) {
    var authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      return crewUseCase.adminUpdateProfile(
          sailorId, crewProfileMapper.toUpdateCrewProfileAdminCommand(patch), user);
    }
    return crewUseCase.crewUpdateProfile(
        sailorId, crewProfileMapper.toUpdateCrewProfileCrewCommand(patch), user);
  }

  @Operation(
      summary = "Fetch all crew profiles",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("")
  @RolesAllowed("ADMIN")
  @PageableQueryParams
  public Page<CrewProfileResponse> getAllCrewProfiles(
      @Filter CrewProfileSearchCriteria criteria,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return crewUseCase
        .getProfiles(criteria, pageable)
        .map(crewProfileMapper::toCrewProfileResponse);
  }

  @Operation(
      summary = "Fetch all mobilized crew profiles",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/my-mobilized")
  @RolesAllowed("USER")
  @PageableQueryParams
  public Page<CrewProfileResponse> getMyMobilizedCrewProfiles(
      @Filter CrewProfileSearchCriteria criteria,
      @PageableDefault(page = 0, size = 20) Pageable pageable,
      @CurrentUser User user) {
    return crewUseCase
        .getMyMobilizedCrewProfiles(criteria, pageable, user)
        .map(crewProfileMapper::toCrewProfileResponse);
  }

  @Operation(
      summary = "Find sailor profile of current user",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/me")
  @RolesAllowed("SAILOR")
  public CrewProfileResponse findMySailorProfile(@CurrentUser User crew) {
    return crewProfileMapper.toCrewProfileResponse(crewUseCase.getProfileForAccount(crew.getId()));
  }

  @Operation(
      summary = "Find sailor profile of user with account id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/accounts/{id}")
  @RolesAllowed("ADMIN")
  public CrewProfileResponse findSailorProfileByAccountId(
      @PathVariable("id") @ObjectId String accountId) {
    return crewProfileMapper.toCrewProfileResponse(crewUseCase.getProfileForAccount(accountId));
  }
}
