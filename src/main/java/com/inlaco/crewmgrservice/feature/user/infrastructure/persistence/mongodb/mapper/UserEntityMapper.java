package com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.entity.UserEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserEntityMapper {

  User toUser(UserEntity entity);

  UserEntity toUserEntity(User user);

  @InheritConfiguration(name = "toUserEntity")
  void updateFromUser(User source, @MappingTarget UserEntity target);
}
