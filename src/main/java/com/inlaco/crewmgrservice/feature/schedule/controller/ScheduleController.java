package com.inlaco.crewmgrservice.feature.schedule.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.annotation.CurrentUser;
import com.inlaco.crewmgrservice.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.feature.schedule.dto.MobilizationResponse;
import com.inlaco.crewmgrservice.feature.schedule.dto.SailorScheduleResponse;
import com.inlaco.crewmgrservice.feature.schedule.dto.ScheduleFilterable;
import com.inlaco.crewmgrservice.feature.schedule.model.AssignedMobilization;
import com.inlaco.crewmgrservice.feature.schedule.service.ScheduleService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.utils.ConsoleUtils;
import com.inlaco.crewmgrservice.validation.annotation.ObjectId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

  private final ScheduleService scheduleService;

  @Operation(
      summary = "Create a new schedule",
      description = "Create a new schedule with the given data.",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.CREATED)
  public AssignedMobilization createSchedule(
      @CurrentUser User user, @RequestBody @Valid AssignedMobilization schedule) {
    return scheduleService.createSchedule(schedule);
  }

  @Operation(
      summary = "Fetch pagination schedules",
      description = "Fetch pagination schedules",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("")
  @RolesAllowed("ADMIN")
  @PageableQueryParams
  public Page<AssignedMobilization> fetchPaginationSchedules(
      ScheduleFilterable filterable, @PageableDefault(page = 0, size = 20) Pageable pageable) {
    ConsoleUtils.log(filterable);
    return scheduleService.findPaginationSchedules(filterable, pageable);
  }

  @Operation(
      summary = "Find schedules with the given data",
      description =
"""

Find schedules with the given data.

If status is provided, filter the schedules by the given status.
If startDate is set, filter schedules that start from the specified startDate to future dates.
If estimatedEndDate is provided, adjust the filter to include schedules that match the estimated end date.

**Usecase**:

- UC_admin-xem-danh-sach-cac-dieu-dong.

""",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/all")
  @RolesAllowed("ADMIN")
  @PageableQueryParams
  public List<AssignedMobilization> fetchSchedules(
      @RequestParam(required = false) AssignedMobilization.Status status,
      @RequestParam(required = false) Instant startDate,
      @RequestParam(required = false) Instant endDate) {
    return scheduleService.findSchedules(
        ScheduleFilterable.builder().status(status).startDate(startDate).endDate(endDate).build());
  }

  @Operation(
      summary = "Find schedules of a sailor with the given cardId",
      description =
"""

Find schedules of a sailor with the given cardId.

If status is provided, filter the schedules by the given status.
If startDate is set, filter schedules that start from the specified startDate to future dates.
If estimatedEndDate is provided, adjust the filter to include schedules that match the estimated end date.

**Usecase**:

- UC_admin-xem-danh-sach-cac-dieu-dong.

""",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/sailors/{cardId}/pagination")
  @RolesAllowed({"ADMIN", "SAILOR"})
  @PageableQueryParams
  public Page<SailorScheduleResponse> fetchPaginationSchedulesByCardId(
      @PathVariable("cardId") String cardId,
      ScheduleFilterable filterable,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return scheduleService.findPaginationSchedulesByCardId(cardId, filterable, pageable);
  }

  @Operation(
      summary = "Find schedules of a sailor with the given cardId",
      description =
"""

Find schedules of a sailor with the given cardId.

If status is provided, filter the schedules by the given status.
If startDate is set, filter schedules that start from the specified startDate to future dates.
If estimatedEndDate is provided, adjust the filter to include schedules that match the estimated end date.

**Usecase**:

- UC_admin-xem-danh-sach-cac-dieu-dong.

""",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/sailors/{cardId}")
  @RolesAllowed({"ADMIN", "SAILOR"})
  public List<SailorScheduleResponse> fetchSchedulesByCardId(
      @PathVariable("cardId") String cardId,
      @RequestParam(required = false) AssignedMobilization.Status status,
      ScheduleFilterable filterable,
      @RequestParam(required = false) Instant startDate,
      @RequestParam(required = false) Instant estimatedEndDate) {
    return scheduleService.findSchedulesByCardId(cardId, filterable);
  }

  @Operation(
      summary = "Find schedule detail by id",
      description =
"""

Find schedule detail by id.

**Usecase**:

- UC_admin-xem-danh-sach-cac-dieu-dong.

""",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/{id}")
  @RolesAllowed({"ADMIN", "SAILOR"})
  public MobilizationResponse findDetailSchedule(@ObjectId @PathVariable("id") String id) {
    return scheduleService.findDetailScheduleById(id);
  }

  @Operation(
      summary = "Update schedule by id",
      description =
"""

Update schedule by id.

**Usecase**:

- UC_admin-xem-danh-sach-cac-dieu-dong.

""",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
  @RolesAllowed({"ADMIN"})
  public MobilizationResponse updateSchedule(
      @ObjectId @PathVariable("id") String id, @RequestBody JsonNode patch) {
    return scheduleService.updateSchedule(id, patch);
  }
}
