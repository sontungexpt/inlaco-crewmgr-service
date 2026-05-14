package com.inlaco.crewmgrservice.feature.crew.application.model;

import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewOperationalStatus;
import java.time.Instant;

public record CrewProfileSearchCriteria(
    String keyword,
    String professionalPosition,
    Boolean official,
    CrewOperationalStatus workStatus,
    Boolean onBoard,
    Boolean mobilized,
    Instant mobilizationStart,
    Instant mobilizationEnd) {}
