package com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.auth.domain.model.RefreshToken;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.entity.RefreshTokenEntity;
import com.inlaco.crewmgrservice.shared.mapper.ObjectIdMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ObjectIdMapper.class)
public interface RefreshTokenEntityMapper {

  RefreshToken toDomain(RefreshTokenEntity entity);

  RefreshTokenEntity toEntity(RefreshToken refreshToken);
}
