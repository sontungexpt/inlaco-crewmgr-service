package com.inlaco.crewmgrservice.feature.course.presentation.dto.request;

import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame;
import com.inlaco.crewmgrservice.shared.application.model.Patch;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import lombok.Data;

@Data
public class CoursePatchRequest implements TimeFrame {
  Patch<String> name = Patch.unchanged();
  Patch<String> trainingProviderName = Patch.unchanged();
  Patch<String> trainingProviderLogo = Patch.unchanged();
  Patch<String> teacherName = Patch.unchanged();
  Patch<String> archivedPosition = Patch.unchanged();
  Patch<Boolean> certified = Patch.unchanged();
  Patch<String> wallpaper = Patch.unchanged();
  Patch<String> description = Patch.unchanged();
  Patch<Instant> startRegistrationAt = Patch.unchanged();
  Patch<Instant> endRegistrationAt = Patch.unchanged();
  Patch<Instant> startDate = Patch.unchanged();
  Patch<Instant> endDate = Patch.unchanged();
  Patch<Integer> limitStudent = Patch.unchanged();

  @Override
  public List<Range> getTimeFrames() {
    return List.of(
            unwrapRange(startRegistrationAt, endRegistrationAt), unwrapRange(startDate, endDate))
        .stream()
        .filter(Objects::nonNull)
        .toList();
  }

  private Range unwrapRange(Patch<Instant> start, Patch<Instant> end) {

    boolean startUpdated = start.isUpdated();
    boolean endUpdated = end.isUpdated();

    if (!startUpdated && !endUpdated) {
      return null;
    }

    Instant startValue = start.ifUpdatedOrElse(v -> v, () -> null);
    Instant endValue = end.ifUpdatedOrElse(v -> v, () -> null);
    return Range.bothRequiredIfEitherPresent(startValue, endValue);
  }
}
