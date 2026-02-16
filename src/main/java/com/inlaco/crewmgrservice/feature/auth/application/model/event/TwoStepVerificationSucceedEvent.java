package com.inlaco.crewmgrservice.feature.auth.application.model.event;

import com.inlaco.crewmgrservice.feature.auth.domain.model.VerificationToken;

public record TwoStepVerificationSucceedEvent(VerificationToken token) {}
