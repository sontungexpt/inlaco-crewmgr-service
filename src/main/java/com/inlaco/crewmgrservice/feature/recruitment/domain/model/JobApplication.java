package com.inlaco.crewmgrservice.feature.recruitment.domain.model;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import com.inlaco.crewmgrservice.feature.user.domain.enums.Gender;
import java.time.Instant;
import java.util.Set;
import lombok.Data;

@Data
public class JobApplication {

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

  private String position;

  private File resume;

  private ApplicationStatus status = ApplicationStatus.APPLIED;

  private Instant appliedAt;

  private Instant updatedAt;

  public void changeStatus(ApplicationStatus newStatus) throws IllegalStateException {
    if (this.status == newStatus) return;
    if (!isValidTransition(this.status, newStatus)) {
      throw new IllegalStateException(
          "Invalid status transition from " + this.status + " to " + newStatus);
    }
    this.status = newStatus;
  }

  private boolean isValidTransition(ApplicationStatus from, ApplicationStatus to) {
    return switch (from) {
      case APPLIED ->
          Set.of(ApplicationStatus.WAIT_FOR_INTERVIEW, ApplicationStatus.REJECTED).contains(to);
      case WAIT_FOR_INTERVIEW ->
          Set.of(ApplicationStatus.HIRED, ApplicationStatus.REJECTED).contains(to);
      default -> false;
    };
  }
}
