package com.inlaco.crewmgrservice.feature.post.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.inlaco.crewmgrservice.common.model.Address;
import com.inlaco.crewmgrservice.feature.post.enums.PostType;
import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PostType.Fields.RECRUITMENT)
public class RecruitmentPost extends Post {

  private double[] expectedSalary = new double[2];

  private boolean actived = false;

  private Address workLocation;

  private Instant recruitmentStartDate;

  private Instant recruitmentEndDate;
}
