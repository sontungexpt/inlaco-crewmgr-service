package com.inlaco.crewmgrservice.feature.recruitment.application.event;

import com.inlaco.crewmgrservice.feature.post.domain.model.RecruitmentPost;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;

public record JobApplicationSubmittedEvent(JobApplication application, RecruitmentPost post) {}
