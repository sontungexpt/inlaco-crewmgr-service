package com.inlaco.crewmgrservice.feature.notify.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.notify.application.port.in.DeviceTokenUseCase;
import com.inlaco.crewmgrservice.feature.notify.presentation.rest.dto.DeviceTokenRegistrationRequest;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/device-tokens")
@RequiredArgsConstructor
public class DeviceTokenController {

  private final DeviceTokenUseCase deviceTokenUseCase;

  @PostMapping("")
  public void registerToken(
      @CurrentUser User user, @Valid @RequestBody DeviceTokenRegistrationRequest request) {
    deviceTokenUseCase.registerDeviceToken(user, request.token(), request.deviceType());
  }

  @DeleteMapping("")
  public void unregisterToken(
      @CurrentUser User user, @Valid @RequestBody DeviceTokenRegistrationRequest request) {
    deviceTokenUseCase.registerDeviceToken(user, request.token(), request.deviceType());
  }
}
