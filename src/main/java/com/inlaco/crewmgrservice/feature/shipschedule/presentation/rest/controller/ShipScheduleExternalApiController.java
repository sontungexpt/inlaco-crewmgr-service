package com.inlaco.crewmgrservice.feature.shipschedule.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleSearchCriteria;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.ShipScheduleUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.ShipScheduleResponse;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.mapper.ShipScheduleMapper;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.Filter;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.PublicEndpoint;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/external/ship-schedules")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Ship Schedule External API", description = "External API for accessing ship schedules")
@SecurityRequirement(name = "apiKey")
@PublicEndpoint(auth = PublicEndpoint.AuthMode.OPTIONAL)
public class ShipScheduleExternalApiController {

  private final ShipScheduleUseCase shipScheduleUseCase;
  private final ShipScheduleMapper mapper;

  @GetMapping("")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Get all ship schedules for authenticated client",
      description =
          "Retrieve all current ship schedules belonging to authenticated client using API key"
              + " authentication")
  public Page<ShipScheduleResponse> getMySchedules(
      @Filter ShipScheduleSearchCriteria criteria,
      @PageableDefault(page = 0, size = 10) Pageable pageable,
      @CurrentUser User user) {
    if (criteria == null) {
      criteria = new ShipScheduleSearchCriteria();
    }
    criteria.setVesselOwnerId(user.getId());
    return shipScheduleUseCase.getSchedules(criteria, pageable).map(mapper::toShipScheduleResponse);
  }
}
