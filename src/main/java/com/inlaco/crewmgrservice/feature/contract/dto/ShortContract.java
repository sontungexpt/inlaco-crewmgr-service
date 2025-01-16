package com.inlaco.crewmgrservice.feature.contract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.model.ContractType;
import com.inlaco.crewmgrservice.feature.contract.model.PaperContract;
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

  private Instant createdAt;

  private Instant updatedAt;

  private boolean signed;

  @Override
  public List<PaperContract> getPaperContracts() {
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
    return createdAt;
  }

  @Override
  public Instant getExpiredDate() {
    return updatedAt;
  }

  @Override
  public Party getInitiator() {
    return initiator;
  }

  @Override
  public List<Party> getSignedPartners() {
    return signedPartners;
  }
}
