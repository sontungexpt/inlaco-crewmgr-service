package com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.notify.domain.model.Notification;
import com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.entity.NotificationEntity;
import com.inlaco.crewmgrservice.shared.mapstruct.config.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    config = CentralMapperConfig.class,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface NotificationEntityMapper {

  Notification toNotification(NotificationEntity notificationEntity);

  NotificationEntity toNotificationEntity(Notification notification);

  NotificationEntity updateFromNotification(
      Notification notification, @MappingTarget NotificationEntity notificationEntity);
}
