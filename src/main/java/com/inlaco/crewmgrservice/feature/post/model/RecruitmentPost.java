package com.inlaco.crewmgrservice.feature.post.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inlaco.crewmgrservice.common.payload.TimeFrame;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(
    description =
        "Details of a recruitment post with expected salary, work location, and recruitment"
            + " period.")
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class RecruitmentPost extends Post implements TimeFrame {

  @Schema(description = "Position title", example = "Software Engineer")
  @NotBlank
  private String position;

  @Schema(description = "Expected salary range", example = "1000000-2000000")
  private String expectedSalary;

  @Schema(description = "Indicates if the post is canceled", example = "true")
  @Default
  @JsonIgnore
  @Getter(AccessLevel.PRIVATE)
  @Setter(AccessLevel.PRIVATE)
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
  @Schema(hidden = true)
  public boolean isActive() {
    return !canceled
        && recruitmentStartDate.isBefore(Instant.now())
        && (recruitmentEndDate == null || recruitmentEndDate.isAfter(Instant.now()));
  }

  @Schema(description = "Work location for the position")
  private String workLocation;

  @Schema(description = "Date when recruitment starts (UTC)", example = "2023-11-01T08:00:00Z")
  @Default
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant recruitmentStartDate = Instant.now().plusSeconds(30);

  @Schema(description = "Date when recruitment ends (UTC)", example = "2023-11-30T17:00:00Z")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant recruitmentEndDate;

  @Override
  @Schema(hidden = true)
  public List<Pair> getTimeFrames() {
    return List.of(Pair.of(recruitmentStartDate, recruitmentEndDate, true, false));
  }
}
