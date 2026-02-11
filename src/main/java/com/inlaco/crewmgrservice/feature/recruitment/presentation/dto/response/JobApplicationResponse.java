package com.inlaco.crewmgrservice.feature.recruitment.presentation.dto.response;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import com.inlaco.crewmgrservice.feature.user.domain.enums.Gender;
import java.time.Instant;
import lombok.Data;

@Data
public class JobApplicationResponse {

  private String id;

  private String accountId;

  private String recruitmentPostId;

  private String fullName;

  private String email;

  private String phoneNumber;

  private String address;

  private Gender gender;

  private String languageSkills;

  private String experiences;

  private File resume;

  private ApplicationStatus status = ApplicationStatus.APPLIED;

  private Instant appliedAt;

  private Instant updatedAt;
}
