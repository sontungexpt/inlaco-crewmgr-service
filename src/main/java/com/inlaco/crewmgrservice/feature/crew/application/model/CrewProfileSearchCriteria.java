package com.inlaco.crewmgrservice.feature.crew.application.model;

import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewOperationalStatus;
import java.util.Set;

public record CrewProfileSearchCriteria(
    String keyword,
    String professionalPosition,
    Boolean official,
    CrewOperationalStatus workStatus,

    // constrain to specific mobilization
    String mobilizationId,

    // constrain to specific schedule
    String scheduleId,

    // explicit exclude ids
    Set<String> excludedProfileIds,
    Set<String> excludedEmployeeCardIds) {}
