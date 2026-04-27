package com.inlaco.crewmgrservice.feature.attendance.presentation.rest;

import com.inlaco.crewmgrservice.feature.attendance.application.dto.CheckInRequest;
import com.inlaco.crewmgrservice.feature.attendance.application.dto.CheckOutRequest;
import com.inlaco.crewmgrservice.feature.attendance.application.port.in.CheckInUseCase;
import com.inlaco.crewmgrservice.feature.attendance.application.port.in.CheckOutUseCase;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

  private final CheckInUseCase checkInUseCase;
  private final CheckOutUseCase checkOutUseCase;

  @PostMapping("/check-in")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void checkIn(@RequestBody CheckInRequest request, @CurrentUser User user) {
    checkInUseCase.checkIn(user.getId(), request);
  }

  @PostMapping("/check-out")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void checkOut(@RequestBody CheckOutRequest request, @CurrentUser User user) {
    checkOutUseCase.checkOut(user.getId(), request);
  }
}
