package com.inlaco.crewmgrservice.feature.auth.application.model.result;

import java.util.List;

public record AuthUserResult(String name, String avatar, List<String> roles) {}
