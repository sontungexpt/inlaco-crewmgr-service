package com.inlaco.crewmgrservice.feature.contract.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ShipInfoRequest(
    @NotBlank String imoNumber,
    @NotBlank String registrationNumber,
    @NotBlank String countryISO,
    @NotBlank String name,
    @NotBlank String description,
    @NotBlank String image,
    @NotBlank String type) {}
