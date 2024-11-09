package com.inlaco.crewmgrservice.feature.post.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.inlaco.crewmgrservice.common.model.Address;
import com.inlaco.crewmgrservice.feature.post.enums.PostType;
import java.time.Instant;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@JsonTypeName(PostType.Fields.RECRUITMENT)
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class RecruitmentPost extends Post {

  @Default private double[] expectedSalary = new double[2];

  @Default private boolean actived = false;

  private Address workLocation;

  private Instant recruitmentStartDate;

  private Instant recruitmentEndDate;
}
