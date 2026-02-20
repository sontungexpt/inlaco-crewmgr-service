package com.inlaco.crewmgrservice.feature.post.presentation.dto.request.update;

import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame;
import com.inlaco.crewmgrservice.shared.application.model.Patch;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecruitmentPostPatchRequest extends PostPatchRequest implements TimeFrame {
  Patch<@NotBlank String> position = Patch.unchanged();
  Patch<@NotBlank String> expectedSalary = Patch.unchanged();
  Patch<@NotBlank String> workLocation = Patch.unchanged();
  Patch<Instant> recruitmentStartDate = Patch.unchanged();
  Patch<Instant> recruitmentEndDate = Patch.unchanged();

  @Override
  public List<Range> getTimeFrames() {
    if (recruitmentStartDate.isUnchanged() && recruitmentEndDate.isUnchanged()) return List.of();

    return List.of(
        Range.bothRequiredIfEitherPresent(
            ((Patch.Updated<Instant>) recruitmentStartDate).asOptional().orElse(null),
            ((Patch.Updated<Instant>) recruitmentEndDate).asOptional().orElse(null)));
  }
}
