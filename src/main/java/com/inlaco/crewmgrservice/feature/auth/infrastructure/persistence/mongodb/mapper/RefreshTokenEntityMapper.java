package com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.auth.domain.model.RefreshToken;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.entity.RefreshTokenEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenEntityMapper {

  RefreshToken toDomain(RefreshTokenEntity entity);

  RefreshTokenEntity toEntity(RefreshToken refreshToken);
}
