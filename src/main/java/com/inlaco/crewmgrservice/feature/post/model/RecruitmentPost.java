package com.inlaco.crewmgrservice.feature.post.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.inlaco.crewmgrservice.common.model.Address;
import com.inlaco.crewmgrservice.common.payload.TimeFrame;
import com.inlaco.crewmgrservice.feature.post.enums.PostType;
import com.inlaco.crewmgrservice.validation.annotation.Range;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.util.Pair;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(
    description =
        "Details of a recruitment post with expected salary, work location, and recruitment"
            + " period.")
@JsonTypeName(PostType.Fields.RECRUITMENT)
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class RecruitmentPost extends Post implements TimeFrame {

  @Schema(description = "Position title", example = "Software Engineer")
  @NotBlank
  private String position;

  @Schema(description = "Expected salary range", example = "[3000, 5000]")
  @Default
  @Range
  private double[] expectedSalary = new double[2];

  @Schema(description = "Indicates if the post is active", example = "true")
  @Default
  private boolean disabled = false;

  public boolean isActive() {
    return !disabled
        && recruitmentStartDate.isBefore(Instant.now())
        && (recruitmentEndDate == null || recruitmentEndDate.isAfter(Instant.now()));
  }

  @Schema(description = "Work location for the position")
  private Address workLocation;

  @Schema(description = "Date when recruitment starts (UTC)", example = "2023-11-01T08:00:00Z")
  @FutureOrPresent
  @Default
  @DateTimeFormat
  private Instant recruitmentStartDate = Instant.now().plusSeconds(30);

  @Schema(description = "Date when recruitment ends (UTC)", example = "2023-11-30T17:00:00Z")
  @DateTimeFormat
  private Instant recruitmentEndDate;

  @Override
  public List<Pair> getTimeFrames() {
    return List.of(Pair.of(recruitmentStartDate, recruitmentEndDate, true, false));
  }
}
