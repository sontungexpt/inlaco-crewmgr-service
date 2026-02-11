package com.inlaco.crewmgrservice.feature.recruitment.application.event;

import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;

public record ApplicationReviewedEvent(JobApplication application) {}
