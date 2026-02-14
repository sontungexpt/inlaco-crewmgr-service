package com.inlaco.crewmgrservice.feature.recruitment.domain.event;

import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;

public record ApplicationStatusChangedEvent(JobApplication application) {}
