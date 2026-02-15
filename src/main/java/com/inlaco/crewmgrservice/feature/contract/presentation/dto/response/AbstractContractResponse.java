package com.inlaco.crewmgrservice.feature.contract.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.Party;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractContractResponse {

  private String id;

  private final ContractType type;

  protected AbstractContractResponse(ContractType type) {
    this.type = type;
  }

  private String title;

  private Party initiator;
  private List<Party> partners;

  private List<String> terms;

  private File contractFile;
  private List<File> attachments;

  private int version;

  private ContractStatus status;

  private Instant activationDate;
  private Instant expiredDate;

  private boolean draft;
  private boolean freezed;
  private boolean signed;
  private boolean active;
  private boolean expired;
  private boolean cancelled;
}
