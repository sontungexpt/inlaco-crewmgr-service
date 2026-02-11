package com.inlaco.crewmgrservice.feature.crew.application.model;

import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewStatus;

public record CrewProfileSearchCriteria(
    String keyword, String professionalPosition, Boolean official, CrewStatus workStatus) {}
