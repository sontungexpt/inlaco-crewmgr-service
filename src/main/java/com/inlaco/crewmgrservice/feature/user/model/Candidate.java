package com.inlaco.crewmgrservice.feature.user.model;

import java.time.Instant;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "sailors")
public class Candidate {

  private String id;

  private String fullName;

  private String email;

  private String phoneNumber;

  private String address;

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
