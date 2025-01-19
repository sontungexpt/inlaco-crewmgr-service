package com.inlaco.crewmgrservice.feature.contract.model;

import com.inlaco.crewmgrservice.common.model.File;
import java.time.Instant;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public interface Contract {

  String getTitle();

  Party getInitiator();

  List<Party> getSignedPartners();

  File getContractFile();

  List<File> getAttachments();

  List<String> getTerms();

  Instant getActivationDate();

  Instant getExpiredDate();

  boolean isSigned();

  ContractType getType();
}
