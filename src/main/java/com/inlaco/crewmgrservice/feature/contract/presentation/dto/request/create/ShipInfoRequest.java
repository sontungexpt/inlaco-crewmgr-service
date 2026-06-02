package com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.create;

import jakarta.validation.constraints.NotBlank;

public record ShipInfoRequest(
    @NotBlank String imoNumber,
    @NotBlank String countryISO,
    @NotBlank String name,
    String description,
    @NotBlank String image,
    @NotBlank String type) {}
