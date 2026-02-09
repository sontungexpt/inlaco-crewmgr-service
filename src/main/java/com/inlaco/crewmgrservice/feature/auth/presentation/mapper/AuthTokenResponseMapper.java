package com.inlaco.crewmgrservice.feature.auth.presentation.mapper;

import com.inlaco.crewmgrservice.feature.auth.application.model.result.AuthTokenResult;
import com.inlaco.crewmgrservice.feature.auth.presentation.dto.response.AuthTokenResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AuthTokenResponseMapper {

  AuthTokenResponse toDTO(AuthTokenResult result);
}
