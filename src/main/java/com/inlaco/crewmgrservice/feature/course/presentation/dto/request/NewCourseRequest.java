package com.inlaco.crewmgrservice.feature.course.presentation.dto.request;

import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class NewCourseRequest implements Serializable, TimeFrame {

  @NotBlank private String name;

  private String trainingProviderName;

  private String trainingProviderLogo;

  private String teacherName;

  private String wallpaper;

  private String archivedPosition;

  private boolean certified;

  @Min(1)
  private int limitStudent = Integer.MAX_VALUE;

  @NotBlank private String description;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant startRegistrationAt = Instant.now();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant endRegistrationAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @NotNull
  private Instant startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @NotNull
  private Instant endDate;

  @Override
  public List<Range> getTimeFrames() {
    return List.of(
        Range.startRequired(startRegistrationAt, endRegistrationAt),
        Range.bothRequired(startDate, endDate));
  }
}
