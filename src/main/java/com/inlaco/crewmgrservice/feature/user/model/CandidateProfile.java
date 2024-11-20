package com.inlaco.crewmgrservice.feature.user.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "candidates")
public class CandidateProfile extends BasicProfile {

  private String resume;

  private int interviewScore;

  public enum Status {
    APPLIED,
    WAIT_FOR_INTERVIEW,
    REJECTED,
    HIRED
  }

  private Status status;

  @CreatedDate private Instant appliedAt;

  @LastModifiedDate private Instant updatedAt;
}
