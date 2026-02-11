package com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

  User toUser(UserEntity userEntity);

  UserEntity toUserEntity(User user);
}
