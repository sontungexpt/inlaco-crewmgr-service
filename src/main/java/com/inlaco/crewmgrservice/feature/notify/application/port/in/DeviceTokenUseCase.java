package com.inlaco.crewmgrservice.feature.notify.application.port.in;

import com.inlaco.crewmgrservice.feature.notify.domain.enums.DeviceType;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;

public interface DeviceTokenUseCase {

  void registerDeviceToken(User user, String deviceToken, DeviceType deviceType);

  void unregisterDeviceToken(User user, String deviceToken, DeviceType deviceType);
}
