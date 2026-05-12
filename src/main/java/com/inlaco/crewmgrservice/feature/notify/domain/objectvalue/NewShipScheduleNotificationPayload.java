package com.inlaco.crewmgrservice.feature.notify.domain.objectvalue;

import com.inlaco.crewmgrservice.feature.notify.domain.model.NotificationPayload;

public record NewShipScheduleNotificationPayload(String scheduleId)
    implements NotificationPayload {}
