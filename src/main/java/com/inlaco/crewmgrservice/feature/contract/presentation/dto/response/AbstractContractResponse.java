package com.inlaco.crewmgrservice.feature.contract.presentation.dto.response;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.Party;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractContractResponse {

  private String id;

  private final ContractType type;

  protected AbstractContractResponse(ContractType type) {
    this.type = type;
  }

  private String title;

  private Party initiator;

  private List<Party> partners;

  private File contractFile;

  private List<File> attachments = new ArrayList<>();

  private List<String> terms;

  private int version;

  private ContractStatus status;

  private Instant activationDate;

  private Instant expiredDate;

  public ContractStatus getEffectiveStatus() {
    Instant now = Instant.now();
    if (status == ContractStatus.SIGNED
        && activationDate != null
        && !now.isBefore(activationDate)) {
      return ContractStatus.ACTIVE;
    }
    if (status == ContractStatus.ACTIVE && expiredDate != null && !now.isBefore(expiredDate)) {
      return ContractStatus.EXPIRED;
    }
    return status;
  }

  public boolean isDraft() {
    return status == ContractStatus.DRAFT;
  }

  private boolean freezed;

  // public boolean isFreezed() {
  //   return isSigned() && activationDate != null && Instant.now().isAfter(getFreezeDate());
  // }

  public boolean isSigned() {
    return getEffectiveStatus() == ContractStatus.SIGNED;
  }

  public boolean isActive() {
    return getEffectiveStatus() == ContractStatus.ACTIVE;
  }

  public boolean isExpired() {
    return getEffectiveStatus() == ContractStatus.EXPIRED;
  }

  public boolean isCancelled() {
    return status == ContractStatus.CANCELLED;
  }
}
