package com.inlaco.crewmgrservice.feature.notify.domain.objectvalue;

import com.inlaco.crewmgrservice.feature.notify.domain.model.NotificationPayload;

public record NewMobilizationScheduleNotificationPayload(String scheduleId)
    implements NotificationPayload {}
