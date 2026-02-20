package com.inlaco.crewmgrservice.feature.post.domain.model;

import com.inlaco.crewmgrservice.shared.application.model.Patch;
import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecruitmentPostUpdateCommand extends PostUpdateCommand {
  Patch<String> position;
  Patch<String> expectedSalary;
  Patch<String> workLocation;
  Patch<Instant> recruitmentStartDate;
  Patch<Instant> recruitmentEndDate;
}
