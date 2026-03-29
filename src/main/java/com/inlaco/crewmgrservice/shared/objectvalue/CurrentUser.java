package com.inlaco.crewmgrservice.shared.objectvalue;

import lombok.Builder;

@Builder
public record CurrentUser(String id, String username) {}
