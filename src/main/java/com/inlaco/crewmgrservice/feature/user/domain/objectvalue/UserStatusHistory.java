package com.inlaco.crewmgrservice.feature.user.domain.objectvalue;

import com.inlaco.crewmgrservice.feature.user.domain.enums.UserStatus;
import java.time.Instant;

public record UserStatusHistory(
    UserStatus from,
    UserStatus to,
    String changedBy, // adminId hoặc system
    String reason,
    Instant changedAt) {}
