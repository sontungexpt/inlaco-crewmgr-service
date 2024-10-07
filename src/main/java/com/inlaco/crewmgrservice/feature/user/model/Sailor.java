package com.inlaco.crewmgrservice.feature.user.model;

import java.time.Instant;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "sailors")
public class Sailor {

  private String id;

  private String fullName;

  private String email;

  private String phoneNumber;

  private String address;

  @CreatedDate private Instant joinedAt;

  @LastModifiedDate private Instant updatedAt;
}
