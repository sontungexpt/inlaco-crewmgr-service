package com.inlaco.crewmgrservice.feature.post.domain.model;

import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RecruitmentPost extends Post {

  private String position;

  private String expectedSalary;

  private boolean canceled = false;

  public void reopenUntil(Instant until) {
    if (until.isBefore(recruitmentStartDate)) {
      return;
    }
    canceled = false;
    recruitmentEndDate = until;
  }

  public void cancel() {
    canceled = true;
  }

  @Override
  public boolean isActive() {
    return !canceled
        && recruitmentStartDate.isBefore(Instant.now())
        && (recruitmentEndDate == null || recruitmentEndDate.isAfter(Instant.now()));
  }

  private String workLocation;

  private Instant recruitmentStartDate;

  private Instant recruitmentEndDate;
}
