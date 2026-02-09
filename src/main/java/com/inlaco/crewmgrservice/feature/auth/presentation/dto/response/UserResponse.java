package com.inlaco.crewmgrservice.feature.auth.presentation.dto.response;

import java.util.List;

public record UserResponse(String name, String avatar, List<String> roles) {}
