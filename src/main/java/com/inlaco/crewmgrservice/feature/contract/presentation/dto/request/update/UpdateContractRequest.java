package com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.update;

import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame;
import com.inlaco.crewmgrservice.shared.application.model.FieldUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class UpdateContractRequest implements TimeFrame, Serializable {
  private FieldUpdate<@NotBlank String> title = FieldUpdate.unchanged();
  private FieldUpdate<Instant> activationDate = FieldUpdate.unchanged();
  private FieldUpdate<Instant> expiredDate = FieldUpdate.unchanged();
  private FieldUpdate<@Min(0) Integer> contractFreezeDelayMinutes = FieldUpdate.unchanged();
  private FieldUpdate<Map<String, FieldUpdate<Object>>> initiator;
  private FieldUpdate<List<Map<String, FieldUpdate<Object>>>> partners;

  @Override
  public List<Range> getTimeFrames() {
    return List.of(Range.of(activationDate.orElse(null), expiredDate.orElse(null), true));
  }
}
