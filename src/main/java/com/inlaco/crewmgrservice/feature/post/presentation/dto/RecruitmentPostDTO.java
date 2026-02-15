package com.inlaco.crewmgrservice.feature.post.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class RecruitmentPostDTO extends PostDTO implements TimeFrame {

  public RecruitmentPostDTO() {
    super(PostType.RECRUITMENT);
  }

  @NotBlank private String position;

  private String expectedSalary;

  private boolean active;

  private String workLocation;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @NotNull
  private Instant recruitmentStartDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @NotNull
  private Instant recruitmentEndDate;

  @Override
  @JsonIgnore
  public List<Pair> getTimeFrames() {
    return List.of(Pair.of(recruitmentStartDate, recruitmentEndDate, true, false));
  }
}
