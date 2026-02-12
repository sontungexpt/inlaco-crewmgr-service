package com.inlaco.crewmgrservice.feature.crewrental.application.model;

import com.inlaco.crewmgrservice.feature.crewrental.domain.enums.CrewRentalRequestStatus;

public record CrewRentalRequestSearchCriteria(String keyword, CrewRentalRequestStatus status) {}
