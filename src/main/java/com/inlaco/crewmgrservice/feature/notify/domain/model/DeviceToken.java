package com.inlaco.crewmgrservice.feature.notify.domain.model;

import com.inlaco.crewmgrservice.feature.notify.domain.enums.DeviceType;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeviceToken {

  private String id;

  private String token;

  private String userId;

  private DeviceType deviceType;

  private Instant createdAt;

  private Instant updatedAt;
}
