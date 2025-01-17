package com.inlaco.crewmgrservice.feature.schedule.controller;

import com.inlaco.crewmgrservice.annotation.CurrentUser;
import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.feature.schedule.dto.SailorScheduleResponse;
import com.inlaco.crewmgrservice.feature.schedule.dto.ScheduleFilterable;
import com.inlaco.crewmgrservice.feature.schedule.dto.ScheduleResponse;
import com.inlaco.crewmgrservice.feature.schedule.model.AssigmentSchedule;
import com.inlaco.crewmgrservice.feature.schedule.service.ScheduleService;
import com.inlaco.crewmgrservice.feature.user.model.User;
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
      description =
          """
Create a new schedule with the given data.

**Usecase**:

- UC_admin-dieu-dong.

""",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.CREATED)
  public AssigmentSchedule createSchedule(
      @CurrentUser User user, @RequestBody @Valid AssigmentSchedule schedule) {
    return scheduleService.createSchedule(schedule);
  }

  @Operation(
      summary = "Fetch pagination schedules",
      description =
          """

Fetch pagination schedules

If status is provided, filter the schedules by the given status.
If startDate is set, filter schedules that start from the specified startDate to future dates.
If estimatedEndDate is provided, adjust the filter to include schedules that match the estimated end date.

**Usecase**:

- UC_admin-xem-danh-sach-cac-dieu-dong.

""",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/pagination")
  @RolesAllowed("ADMIN")
  public Page<AssigmentSchedule> fetchPaginationSchedules(
      @RequestParam(required = false) AssigmentSchedule.Status status,
      @RequestParam(required = false) Instant startDate,
      @RequestParam(required = false) Instant estimatedEndDate,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return scheduleService.findPaginationSchedules(
        ScheduleFilterable.builder()
            .status(status)
            .startDate(startDate)
            .estimatedEndDate(estimatedEndDate)
            .build(),
        pageable);
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
  @GetMapping("")
  @RolesAllowed("ADMIN")
  public List<AssigmentSchedule> fetchSchedules(
      @RequestParam(required = false) AssigmentSchedule.Status status,
      @RequestParam(required = false) Instant startDate,
      @RequestParam(required = false) Instant estimatedEndDate) {
    return scheduleService.findSchedules(
        ScheduleFilterable.builder()
            .status(status)
            .startDate(startDate)
            .estimatedEndDate(estimatedEndDate)
            .build());
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
  public Page<SailorScheduleResponse> fetchPaginationSchedulesByCardId(
      @PathVariable("cardId") String cardId,
      @RequestParam(required = false) AssigmentSchedule.Status status,
      @RequestParam(required = false) Instant startDate,
      @RequestParam(required = false) Instant estimatedEndDate,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return scheduleService.findPaginationSchedulesByCardId(
        cardId,
        ScheduleFilterable.builder()
            .status(status)
            .startDate(startDate)
            .estimatedEndDate(estimatedEndDate)
            .build(),
        pageable);
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
      @RequestParam(required = false) AssigmentSchedule.Status status,
      @RequestParam(required = false) Instant startDate,
      @RequestParam(required = false) Instant estimatedEndDate) {
    return scheduleService.findSchedulesByCardId(
        cardId,
        ScheduleFilterable.builder()
            .status(status)
            .startDate(startDate)
            .estimatedEndDate(estimatedEndDate)
            .build());
  }

  @Operation(
      summary = "Find schedule detail by id",
      description =
          """

Find schedule detail by id.

If status is provided, filter the schedules by the given status.
If startDate is set, filter schedules that start from the specified startDate to future dates.
If estimatedEndDate is provided, adjust the filter to include schedules that match the estimated end date.

**Usecase**:

- UC_admin-xem-danh-sach-cac-dieu-dong.

""",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/{id}")
  @RolesAllowed({"ADMIN", "SAILOR"})
  public ScheduleResponse findDetailSchedule(@ObjectId @PathVariable("id") String id) {
    return scheduleService.findDetailScheduleById(id);
  }
}
