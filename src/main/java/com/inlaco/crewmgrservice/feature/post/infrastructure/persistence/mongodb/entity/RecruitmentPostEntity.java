package com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity;

import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecruitmentPostEntity extends PostEntity {

  private String position;

  private String expectedSalary;

  private boolean canceled = false;

  private String workLocation;

  private Instant recruitmentStartDate;

  private Instant recruitmentEndDate;
}
