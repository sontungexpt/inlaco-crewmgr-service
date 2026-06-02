package com.inlaco.crewmgrservice.feature.apikey.presentation.mapper;

import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
import com.inlaco.crewmgrservice.feature.apikey.presentation.dto.response.ApiKeyResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApiKeyMapper {

  ApiKeyResponse toResponse(ApiKey apiKey);
}
