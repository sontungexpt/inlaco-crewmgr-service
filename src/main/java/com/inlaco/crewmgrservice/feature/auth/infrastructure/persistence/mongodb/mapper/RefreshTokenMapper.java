package com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.auth.domain.model.RefreshToken;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.entity.RefreshTokenEntity;
import com.inlaco.crewmgrservice.shared.mapper.ObjectIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ObjectIdMapper.class)
public interface RefreshTokenMapper {

  @Mapping(target = "userId", source = "userId", qualifiedByName = "objectIdToString")
  RefreshToken toDomain(RefreshTokenEntity entity);

  @Mapping(target = "userId", source = "userId", qualifiedByName = "stringToObjectId")
  RefreshTokenEntity toEntity(RefreshToken domain);
}
