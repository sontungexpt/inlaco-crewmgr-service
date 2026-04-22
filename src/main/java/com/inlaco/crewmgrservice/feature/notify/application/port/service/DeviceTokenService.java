package com.inlaco.crewmgrservice.feature.notify.application.port.service;

import com.inlaco.crewmgrservice.feature.notify.application.port.in.DeviceTokenUseCase;
import com.inlaco.crewmgrservice.feature.notify.application.port.out.DeviceTokenRepostiory;
import com.inlaco.crewmgrservice.feature.notify.domain.enums.DeviceType;
import com.inlaco.crewmgrservice.feature.notify.domain.model.DeviceToken;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceTokenService implements DeviceTokenUseCase {

  private final DeviceTokenRepostiory deviceTokenRepostiory;

  @Override
  public void registerDeviceToken(User user, String token, DeviceType deviceType) {
    if (user == null || token == null || token.isBlank()) {
      log.warn("Invalid register request user={} token={}", user, token);
      return;
    }

    if (deviceTokenRepostiory.existsByToken(token)) {
      log.warn("Token already exists: {}", token);
      return;
    }

    DeviceToken newToken =
        DeviceToken.builder().token(token).userId(user.getId()).deviceType(deviceType).build();

    deviceTokenRepostiory.save(newToken);

    log.info("Registered new device token for user={}", user.getId());
  }

  @Override
  public void unregisterDeviceToken(User user, String token, DeviceType deviceType) {

    if (token == null || token.isBlank()) {
      log.warn("Invalid unregister request token={}", token);
      return;
    }

    var existing = deviceTokenRepostiory.findByToken(token).orElse(null);

    if (existing == null) {
      log.warn("Token not found: {}", token);
      return;
    }

    if (user != null && !existing.getUserId().equals(user.getId())) {
      log.warn("User {} trying to remove token of user {}", user.getId(), existing.getUserId());
      return;
    }

    deviceTokenRepostiory.deleteByToken(token);

    log.info("Unregistered device token for user={}", existing.getUserId());
  }
}
