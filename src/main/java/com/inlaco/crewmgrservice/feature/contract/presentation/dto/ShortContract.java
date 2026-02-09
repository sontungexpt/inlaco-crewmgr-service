package com.inlaco.crewmgrservice.feature.contract.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.model.ContractType;
import com.inlaco.crewmgrservice.feature.contract.model.Party;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShortContract implements Contract {

  private String id;

  private String title;

  private List<Party> signedPartners;

  private Party initiator;

  private File file;

  private ContractType type;

  private Instant freezedAt;

  private Instant activationAt;

  private Instant expiredAt;

  private boolean signed;

  @Override
  public File getContractFile() {
    return null;
  }

  @Override
  public List<File> getAttachments() {
    return null;
  }

  @Override
  public List<String> getTerms() {
    return null;
  }

  @Override
  public Instant getActivationDate() {
    return activationAt;
  }

  @Override
  public Instant getExpiredDate() {
    return expiredAt;
  }

  @Override
  public Party getInitiator() {
    return initiator;
  }

  @Override
  public List<Party> getPartners() {
    return signedPartners;
  }
}
