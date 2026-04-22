package com.inlaco.crewmgrservice.feature.notify.presentation.rest.dto;

import com.inlaco.crewmgrservice.feature.notify.domain.enums.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeviceTokenRegistrationRequest(
    @NotBlank String token, @NotNull DeviceType deviceType) {}
