package com.inlaco.crewmgrservice.feature.crewhiring.controller;

import com.inlaco.crewmgrservice.annotation.CurrentUser;
import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.feature.crewhiring.dto.CrewRentalRequestFilter;
import com.inlaco.crewmgrservice.feature.crewhiring.enums.CrewRentalRequestStatus;
import com.inlaco.crewmgrservice.feature.crewhiring.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.feature.crewhiring.service.CrewRentalRequestService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.validation.annotation.ObjectId;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/crew-rental-requests")
@RequiredArgsConstructor
@Tag(name = "Crew Rental Request", description = "APIs for managing crew rental requests")
public class CrewRentalRequestController {

  private CrewRentalRequestService crewRentalRequestService;

  @PostMapping("")
  @RolesAllowed("USER")
  @Operation(
      summary = "Create a new crew rental request",
      description =
          """
Create a new crew rental request

**Usecase**:

- UC_general-user-gui-yeu-cau-thuyen-vien.
""",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  public ResponseEntity<CrewRentalRequest> createRequest(
      @Valid @RequestBody CrewRentalRequest request) {
    CrewRentalRequest createdRequest = crewRentalRequestService.createRequest(request);
    return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
  }

  @Operation(
      summary = "Admin review a new crew rental request",
      description =
          """
Admin review a new crew rental request

**Usecase**:

- UC_admin-xem-yeu-cau-thue-thuyen-vien.
""",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("ADMIN")
  @PostMapping("/{id}/review")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void reviewRequest(
      @ObjectId @PathVariable("id") String id,
      @CurrentUser User reviewer,
      @RequestParam boolean accepted) {
    crewRentalRequestService.reviewRequest(id, accepted, reviewer);
  }

  @Operation(
      summary = "Find all requests",
      description =
          """
Find all requests

**Usecase**:

- UC_admin-xem-yeu-cau-thue-thuyen-vien.
""",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("ADMIN")
  @GetMapping("")
  public Page<CrewRentalRequest> findAllRequests(
      @RequestParam(required = false) CrewRentalRequestStatus status,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return crewRentalRequestService.findAllRequest(
        CrewRentalRequestFilter.builder().status(status).build(), pageable);
  }

  @Operation(
      summary = "Find a request by id",
      description =
          """
Find a request by id

**Usecase**:

- UC_admin-xem-yeu-cau-thue-thuyen-vien.
""",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("ADMIN")
  @GetMapping("/{id}")
  public CrewRentalRequest findRequestById(@PathVariable("id") @ObjectId String id) {
    return crewRentalRequestService.getRequestById(id);
  }
}
