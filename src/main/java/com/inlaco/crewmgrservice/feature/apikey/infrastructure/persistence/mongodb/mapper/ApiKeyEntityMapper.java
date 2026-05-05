package com.inlaco.crewmgrservice.feature.apikey.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
import com.inlaco.crewmgrservice.feature.apikey.infrastructure.persistence.mongodb.entity.ApiKeyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ApiKeyEntityMapper {

  ApiKeyEntity toEntity(ApiKey domain);

  ApiKey toDomain(ApiKeyEntity entity);

  @Mapping(target = "id", ignore = true) // Don't update ID
  void updateEntity(@MappingTarget ApiKeyEntity entity, ApiKey domain);

  @Named("entityToDomainWithoutId")
  @Mapping(target = "id", ignore = true) // For cases where we want to ignore ID
  ApiKey toDomainWithoutId(ApiKeyEntity entity);
}
