package com.inlaco.crewmgrservice.feature.crewmobilization.presentation.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ShipInfoRequest(
    @NotBlank String countryISO,
    @NotBlank String name,
    String description,
    @NotBlank String image,
    @NotBlank String type) {}
