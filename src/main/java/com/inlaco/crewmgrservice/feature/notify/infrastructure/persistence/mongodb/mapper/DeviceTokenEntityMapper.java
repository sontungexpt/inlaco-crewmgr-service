package com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.notify.domain.model.DeviceToken;
import com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.entity.DeviceTokenEntity;
import com.inlaco.crewmgrservice.shared.mapstruct.mapper.ObjectIdMapper;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {ObjectIdMapper.class})
public interface DeviceTokenEntityMapper {

  DeviceToken toDeviceToken(DeviceTokenEntity deviceTokenEntity);

  DeviceTokenEntity toDeviceTokenEntity(DeviceToken deviceToken);
}
