package com.inlaco.crewmgrservice.feature.schedule.controller;

import com.inlaco.crewmgrservice.annotation.CurrentUser;
import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.feature.schedule.model.Schedule;
import com.inlaco.crewmgrservice.feature.schedule.service.ScheduleService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
  public Schedule createSchedule(@CurrentUser User user, @RequestBody @Valid Schedule schedule) {
    return scheduleService.createSchedule(schedule);
  }
}
