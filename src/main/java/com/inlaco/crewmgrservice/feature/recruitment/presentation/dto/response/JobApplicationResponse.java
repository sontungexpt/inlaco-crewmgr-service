package com.inlaco.crewmgrservice.feature.recruitment.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import com.inlaco.crewmgrservice.shared.objectvalue.Gender;
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

  private Asset resume;

  private ApplicationStatus status;

  private Instant appliedAt;

  private Instant updatedAt;
}
