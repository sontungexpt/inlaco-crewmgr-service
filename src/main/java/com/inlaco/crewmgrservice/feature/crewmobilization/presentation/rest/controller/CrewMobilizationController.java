package com.inlaco.crewmgrservice.feature.crewmobilization.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in.CrewMobilizationCommandUseCase;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in.CrewMobilizationExcelExportUseCase;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in.CrewMobilizationQueryUseCase;
import com.inlaco.crewmgrservice.feature.crewmobilization.presentation.rest.dto.request.CreateCrewMobilizationRequest;
import com.inlaco.crewmgrservice.feature.crewmobilization.presentation.rest.dto.response.CrewMobilizationResponse;
import com.inlaco.crewmgrservice.feature.crewmobilization.presentation.rest.mapper.CrewMobilizationMapper;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.Filter;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.infrastructure.web.validation.annotation.ObjectId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class CrewMobilizationController {

  private final CrewMobilizationCommandUseCase crewMobilizationScheduleCommandUseCase;
  private final CrewMobilizationQueryUseCase crewMobilizationScheduleQueryUseCase;
  private final CrewMobilizationExcelExportUseCase exportUseCase;
  private final CrewMobilizationMapper mapper;

  @Operation(
      summary = "Create a new schedule",
      description = "Create a new schedule with the given data.",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.CREATED)
  public CrewMobilizationResponse createSchedule(
      @CurrentUser User user, @RequestBody @Valid CreateCrewMobilizationRequest newMobilization) {

    return mapper.toCrewMobilizationResponse(
        crewMobilizationScheduleCommandUseCase.createMobilization(
            mapper.toCrewMobilization(newMobilization),
            newMobilization.crews().stream().map(mapper::toCrewMobilizationAssignment).toList(),
            newMobilization.shipInfo().image(),
            user));
  }

  @Operation(
      summary = "Fetch pagination schedules",
      description = "Fetch pagination schedules",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("")
  @RolesAllowed("ADMIN")
  @PageableQueryParams
  public Page<CrewMobilizationResponse> getAllSchedules(
      @Filter CrewMobilizationSearchCriteria criteria,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return crewMobilizationScheduleQueryUseCase
        .findMobilizations(criteria, pageable)
        .map(mapper::toCrewMobilizationResponse);
  }

  @Operation(
      summary = "Find schedule detail by id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/{id}")
  @RolesAllowed({"ADMIN", "SAILOR", "USER"})
  public CrewMobilizationResponse getMobilizationDetail(@ObjectId @PathVariable("id") String id) {
    return mapper.toCrewMobilizationResponse(
        crewMobilizationScheduleQueryUseCase.findDetailMobilization(id));
  }

  @Operation(
      summary = "Find schedules of the current logged-in sailor (paginated)",
      description = "Fetch schedules that include the current user's crew cardId",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed({"SAILOR", "USER"})
  @PageableQueryParams
  @GetMapping("/mine")
  public Page<CrewMobilizationResponse> getMyMobilizations(
      @Filter CrewMobilizationSearchCriteria criteria,
      @CurrentUser User user,
      Authentication authentication,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    if (criteria == null) {
      criteria = new CrewMobilizationSearchCriteria();
    }

    boolean isSailor =
        authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("SAILOR"));
    if (isSailor) {
      criteria.setAccountId(user.getId());
    } else {
      criteria.setClientId(user.getId());
    }
    return crewMobilizationScheduleQueryUseCase
        .findMobilizations(criteria, pageable)
        .map(mapper::toCrewMobilizationResponse);
  }

  @GetMapping("/{id}/export")
  @Operation(
      summary = "Export schedule to excel",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed({"ADMIN", "SAILOR"})
  public ResponseEntity<byte[]> export(@PathVariable String id) {

    byte[] data = exportUseCase.exportMobilization(id);

    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=schedule.xlsx")
        .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        .body(data);
  }
}
