package com.inlaco.crewmgrservice.feature.apikey.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
import com.inlaco.crewmgrservice.feature.apikey.infrastructure.persistence.mongodb.entity.ApiKeyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ApiKeyEntityMapper {

  ApiKeyEntity toApiKeyEntity(ApiKey domain);

  ApiKey toApiKey(ApiKeyEntity entity);

  @Mapping(target = "id", ignore = true) // Don't update ID
  void updateFromApiKey(@MappingTarget ApiKeyEntity entity, ApiKey domain);
}
