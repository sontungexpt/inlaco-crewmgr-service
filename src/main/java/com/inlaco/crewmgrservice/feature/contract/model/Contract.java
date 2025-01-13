package com.inlaco.crewmgrservice.feature.contract.model;

import com.inlaco.crewmgrservice.common.model.File;
import java.time.Instant;
import java.util.List;

public interface Contract {

  String getTitle();

  List<Party> getParties();

  List<PaperContract> getPaperContracts();

  List<File> getAttachments();

  List<String> getTerms();

  Instant getActivationDate();

  Instant getExpiredDate();

  boolean isFreeze();

  boolean isSigned();

  ContractType getType();
}
