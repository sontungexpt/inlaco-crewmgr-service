package com.inlaco.crewmgrservice.feature.auth.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public record LoginRequest(@NotBlank String username, @NotBlank String password)
    implements Serializable {}
