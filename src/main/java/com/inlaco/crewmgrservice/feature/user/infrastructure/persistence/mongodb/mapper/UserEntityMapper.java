package com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring")
public interface UserEntityMapper {

  User toUser(UserEntity userEntity);

  UserEntity toUserEntity(User user);

  void updateFromUser(User source, @MappingTarget UserEntity target);
}
