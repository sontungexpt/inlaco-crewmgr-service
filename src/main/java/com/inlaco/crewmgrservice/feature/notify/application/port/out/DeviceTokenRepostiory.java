package com.inlaco.crewmgrservice.feature.notify.application.port.out;

import com.inlaco.crewmgrservice.feature.notify.domain.model.DeviceToken;
import java.util.List;
import java.util.Optional;

public interface DeviceTokenRepostiory {

  DeviceToken save(DeviceToken deviceToken);

  void deleteByToken(String deviceToken);

  Optional<DeviceToken> findByToken(String token);

  List<DeviceToken> findByUserId(String userId);

  boolean existsByToken(String token);
}
