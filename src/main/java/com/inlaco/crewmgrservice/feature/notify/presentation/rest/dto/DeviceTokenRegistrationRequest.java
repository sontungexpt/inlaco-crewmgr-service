package com.inlaco.crewmgrservice.feature.notify.presentation.rest.dto;

import com.inlaco.crewmgrservice.feature.notify.domain.enums.DeviceType;
import jakarta.validation.constraints.NotBlank;

public record DeviceTokenRegistrationRequest(
    @NotBlank String token, @NotBlank DeviceType deviceType) {}
