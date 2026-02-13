package com.inlaco.crewmgrservice.feature.contracttemplate.presentation.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NewContractTemplate(
    @NotBlank String name, String description, @NotBlank String type) {}
