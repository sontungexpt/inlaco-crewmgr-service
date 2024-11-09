package com.inlaco.crewmgrservice.feature.post.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.inlaco.crewmgrservice.common.model.Address;
import com.inlaco.crewmgrservice.feature.post.enums.PostType;
import com.inlaco.crewmgrservice.validation.annotation.Range;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Schema(
    description =
        "Details of a recruitment post with expected salary, work location, and recruitment"
            + " period.")
@JsonTypeName(PostType.Fields.RECRUITMENT)
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class RecruitmentPost extends Post {

  @Schema(description = "Expected salary range", example = "[3000, 5000]")
  @Default
  @Range
  private double[] expectedSalary = new double[2];

  @Schema(description = "Indicates if the post is active", example = "true")
  @Default
  private boolean actived = false;

  @Schema(description = "Work location for the position")
  private Address workLocation;

  @Schema(description = "Date when recruitment starts (UTC)", example = "2023-11-01T08:00:00Z")
  private Instant recruitmentStartDate;

  @Schema(description = "Date when recruitment ends (UTC)", example = "2023-11-30T17:00:00Z")
  private Instant recruitmentEndDate;
}
