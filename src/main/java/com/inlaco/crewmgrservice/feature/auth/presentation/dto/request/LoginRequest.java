package com.inlaco.crewmgrservice.feature.auth.presentation.dto.request;

import java.io.Serializable;

public record LoginRequest(String username, String password) implements Serializable {}
