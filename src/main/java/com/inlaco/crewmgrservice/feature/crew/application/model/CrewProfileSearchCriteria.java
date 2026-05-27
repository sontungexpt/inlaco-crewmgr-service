package com.inlaco.crewmgrservice.feature.crew.application.model;

import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewOperationalStatus;
import java.time.Instant;
import java.util.Set;

public record CrewProfileSearchCriteria(
    String keyword,
    String professionalPosition,
    Boolean official,
    CrewOperationalStatus workStatus,

    // availability
    Instant availableFrom,
    Instant availableTo,

    // filter occupancy
    Boolean excludeMobilized,
    Boolean excludeOnBoard,

    // constrain to specific mobilization
    String mobilizationId,

    // constrain to specific schedule
    String scheduleId,

    // explicit exclude ids
    Set<String> excludedProfileIds,
    Set<String> excludedEmployeeCardIds) {}
