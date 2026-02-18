package com.inlaco.crewmgrservice.feature.contract.domain.model;

import com.inlaco.crewmgrservice.shared.application.model.FieldUpdate;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class UpdateContractCommand {
  private FieldUpdate<String> title;
  private FieldUpdate<Instant> activationDate;
  private FieldUpdate<Instant> expiredDate;
  private FieldUpdate<Integer> contractFreezeDelayMinutes;
  private FieldUpdate<Map<String, FieldUpdate<Object>>> initiator;
  private FieldUpdate<List<Map<String, FieldUpdate<Object>>>> partners;
}
