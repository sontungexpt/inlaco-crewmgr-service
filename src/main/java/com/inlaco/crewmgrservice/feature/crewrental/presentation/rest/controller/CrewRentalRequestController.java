package com.inlaco.crewmgrservice.feature.crewrental.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.crewrental.application.model.CrewRentalRequestSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewrental.application.port.in.CrewRentalRequestCommandUseCase;
import com.inlaco.crewmgrservice.feature.crewrental.application.port.in.CrewRentalRequestQueryUseCase;
import com.inlaco.crewmgrservice.feature.crewrental.presentation.dto.CrewRentalRequestResponse;
import com.inlaco.crewmgrservice.feature.crewrental.presentation.dto.NewCrewRentalRequest;
import com.inlaco.crewmgrservice.feature.crewrental.presentation.mapper.CrewRentalRequestMapper;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.Filter;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Crew Rental Request", description = "APIs for managing crew rental requests")
@RequestMapping("/api/v1/crew-rental-requests")
public class CrewRentalRequestController {

  private final CrewRentalRequestQueryUseCase crewRentalRequestUseCase;
  private final CrewRentalRequestCommandUseCase crewRentalRequestCommandUseCase;
  private final CrewRentalRequestMapper crewRentalRequestMapper;

  @Operation(
      summary = "Create a new crew rental request",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("")
  @RolesAllowed("USER")
  public CrewRentalRequestResponse createRequest(
      @Valid @RequestBody NewCrewRentalRequest newRequest, @CurrentUser User user) {

    return crewRentalRequestMapper.toCrewRentalRequestResponse(
        crewRentalRequestCommandUseCase.create(
            crewRentalRequestMapper.toCrewRentalRequest(newRequest),
            newRequest.getDetailFile(),
            newRequest.getShipInfo().image(),
            user));
  }

  @Operation(
      summary = "Admin review a new crew rental request",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("ADMIN")
  @PostMapping("/{supplyRequestId}/review")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void reviewRequest(
      @ObjectId @PathVariable("supplyRequestId") String supplyRequestId,
      @CurrentUser User reviewer,
      @RequestParam boolean accepted) {
    crewRentalRequestCommandUseCase.review(supplyRequestId, accepted, reviewer);
  }

  @Operation(
      summary = "Find all requests",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("ADMIN")
  @GetMapping("")
  public Page<CrewRentalRequestResponse> getAllRequests(
      @Filter CrewRentalRequestSearchCriteria criteria,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return crewRentalRequestUseCase
        .getRequests(criteria, pageable)
        .map(crewRentalRequestMapper::toCrewRentalRequestResponse);
  }

  @Operation(
      summary = "Find a request by id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("ADMIN")
  @GetMapping("/{id}")
  public CrewRentalRequestResponse getCrewRentalRequest(@PathVariable("id") @ObjectId String id) {
    return crewRentalRequestMapper.toCrewRentalRequestResponse(
        crewRentalRequestUseCase.getRequest(id));
  }
}
