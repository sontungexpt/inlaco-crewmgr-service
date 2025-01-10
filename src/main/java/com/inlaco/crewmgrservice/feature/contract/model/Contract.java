package com.inlaco.crewmgrservice.feature.contract.model;

import com.inlaco.crewmgrservice.common.model.Attachment;
import java.time.Instant;
import java.util.List;

public interface Contract {

  String getTitle();

  List<Party> getParties();

  List<PaperContract> getPaperContracts();

  List<Attachment> getAttachments();

  List<String> getTerms();

  Instant getActivationDate();

  Instant getExpiredDate();

  boolean isFreeze();

  ContractType getType();
}
