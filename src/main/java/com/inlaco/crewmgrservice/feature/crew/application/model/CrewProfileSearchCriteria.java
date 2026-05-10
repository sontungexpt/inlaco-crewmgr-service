package com.inlaco.crewmgrservice.feature.crew.application.model;

import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewOperationalStatus;

public record CrewProfileSearchCriteria(
    String keyword,
    String professionalPosition,
    Boolean official,
    CrewOperationalStatus workStatus) {}
