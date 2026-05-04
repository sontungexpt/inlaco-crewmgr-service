package com.inlaco.crewmgrservice.feature.crewmobilization.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in.CrewMobilizationCommandUseCase;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in.CrewMobilizationExcelExportUseCase;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in.CrewMobilizationQueryUseCase;
import com.inlaco.crewmgrservice.feature.crewmobilization.presentation.rest.dto.request.NewCrewMobilizationRequest;
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
      @CurrentUser User user, @RequestBody @Valid NewCrewMobilizationRequest newSchedule) {
    return mapper.toCrewMobilizationScheduleResponse(
        crewMobilizationScheduleCommandUseCase.createSchedule(
            mapper.toCrewMobilizationSchedule(newSchedule)));
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
        .findSchedules(criteria, pageable)
        .map(mapper::toCrewMobilizationScheduleResponse);
  }

  @Operation(
      summary = "Find schedule detail by id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/{id}")
  @RolesAllowed({"ADMIN", "SAILOR"})
  public CrewMobilizationResponse getScheduleDetail(@ObjectId @PathVariable("id") String id) {
    return mapper.toCrewMobilizationScheduleResponse(
        crewMobilizationScheduleQueryUseCase.findDetailSchedule(id));
  }

  @Operation(
      summary = "Find schedules of the current logged-in sailor (paginated)",
      description = "Fetch schedules that include the current user's crew cardId",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("SAILOR")
  @PageableQueryParams
  @GetMapping("/mine")
  public Page<CrewMobilizationResponse> getMySchedules(
      @Filter CrewMobilizationSearchCriteria criteria,
      @CurrentUser User user,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    if (criteria == null) {
      criteria = new CrewMobilizationSearchCriteria();
    }
    criteria.setAccountId(user.getId());
    return crewMobilizationScheduleQueryUseCase
        .findSchedules(criteria, pageable)
        .map(mapper::toCrewMobilizationScheduleResponse);
  }

  @GetMapping("/{id}/export")
  @Operation(
      summary = "Export schedule to excel",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed({"ADMIN", "SAILOR"})
  public ResponseEntity<byte[]> export(@PathVariable String id) {

    byte[] data = exportUseCase.exportSchedule(id);

    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=schedule.xlsx")
        .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        .body(data);
  }
}
