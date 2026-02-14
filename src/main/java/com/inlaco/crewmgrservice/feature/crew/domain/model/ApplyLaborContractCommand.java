package com.inlaco.crewmgrservice.feature.crew.domain.model;

import java.time.Instant;

public record ApplyLaborContractCommand(
    String accountId,
    String fullName,
    String address,
    String phone,
    String email,
    Instant birthDate,
    String position) {}
