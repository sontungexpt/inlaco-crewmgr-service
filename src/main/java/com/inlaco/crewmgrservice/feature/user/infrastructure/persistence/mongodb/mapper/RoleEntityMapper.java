package com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.user.domain.model.Role;
import com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring")
public interface RoleEntityMapper {

  Role toRole(RoleEntity roleEntity);

  RoleEntity toRoleEntity(Role role);

  void updateFromRole(Role role, @MappingTarget RoleEntity roleEntity);
}
