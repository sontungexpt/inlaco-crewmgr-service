package com.inlaco.crewmgrservice.feature.contract.domain.model;

import com.inlaco.crewmgrservice.feature.contract.domain.model.party.Party;
import com.inlaco.crewmgrservice.shared.application.model.Patch;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class UpdateContractCommand {
  Patch<String> title;
  Patch<Instant> activationDate;
  Patch<Instant> expiredDate;
  Patch<Integer> contractFreezeDelayMinutes;
  Patch<Party> initiator;
  Patch<List<Party>> partners;
  String contractFile;
  List<String> attachments;
}
