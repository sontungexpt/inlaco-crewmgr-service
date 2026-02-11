package com.inlaco.crewmgrservice.feature.crew.presentation.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.crew.application.model.CrewProfileSearchCriteria;
import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crew.presentation.dto.response.CrewProfileResponse;
import com.inlaco.crewmgrservice.feature.crew.presentation.mapper.CrewProfileMapper;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.Filter;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.infrastructure.web.validation.annotation.ObjectId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sailors")
@Tag(name = "Crew Profile", description = "Sailor management")
public class CrewController {

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
      summary = "Find crew profile by id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
  @RolesAllowed("ADMIN")
  public CrewProfile updateCrewProfile(
      @PathVariable("id") @ObjectId String sailorId, @RequestBody JsonNode patch) {
    throw new UnsupportedOperationException(
        "This endpoint is deprecated, use /api/v1/sailors/{id} instead");
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

  // @Operation(
  //     summary = "Find sailor profile of current user",
  //     security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
  //     description = "This API is used to get the sailor profile of the current user")
  // @GetMapping("/profile/me")
  // @RolesAllowed("SAILOR")
  // public SailorProfile findMySailorProfile(@CurrentUser User sailor) {
  //   return sailorService.findMySailorProfile(sailor);
  // }

  // @Operation(
  //     summary = "Find sailor profile of user with account id",
  //     security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  // @GetMapping("/users/{id}")
  // @RolesAllowed("ADMIN")
  // public SailorProfile findSailorProfileByAccountId(
  //     @PathVariable("id") @ObjectId String accountId) {
  //   return sailorService.findSailorProfileById(accountId);
  // }
}
