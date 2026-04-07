package com.inlaco.crewmgrservice.feature.crewrental.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record ShipInfoRequest(
    @NotBlank String imoNumber,
    @NotBlank String countryISO,
    @NotBlank String name,
    String description,
    @NotBlank String image,
    @NotBlank String type) {}
