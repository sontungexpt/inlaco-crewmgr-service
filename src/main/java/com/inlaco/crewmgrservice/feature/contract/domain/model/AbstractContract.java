package com.inlaco.crewmgrservice.feature.contract.domain.model;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.Party;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.ContractStatusHistory;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.DynamicAttribute;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractContract {

  private static final String SYSTEM = "SYSTEM";

  private String id;
  private final ContractType type;

  protected AbstractContract(ContractType type) {
    this.type = type;
  }

  // ======================
  // BASIC INFO
  // ======================

  private String title;
  private Party initiator;
  private List<Party> partners = new ArrayList<>();
  private Asset contractFile;
  private List<Asset> attachments = new ArrayList<>();
  private List<String> terms = new ArrayList<>();
  private int version;
  private List<DynamicAttribute> customAttributes = new ArrayList<>();

  public void incrementVersion() {
    version++;
  }

  // ======================
  // STATUS
  // ======================

  @Setter(AccessLevel.PRIVATE)
  private ContractStatus status = ContractStatus.DRAFT;

  private final List<ContractStatusHistory> statusHistories = new ArrayList<>();

  @Setter(AccessLevel.PRIVATE)
  private Instant lastStatusChangedAt;

  @Setter(AccessLevel.PRIVATE)
  private String lastStatusChangedBy;

  // ======================
  // TIME
  // ======================

  private Instant activationDate;
  private Instant expiredDate;
  private int contractFreezeDelayMinutes = 5;

  // ======================
  // LIFECYCLE ACTIONS
  // ======================

  public void sign(String signedBy, Instant now) {
    transitionTo(ContractStatus.SIGNED, defaultIfNull(signedBy), "Contract signed", now);
  }

  public void activate(Instant now) {
    if (!canActivate(now)) {
      throw new IllegalStateException("Activation date not reached");
    }
    transitionTo(ContractStatus.ACTIVE, SYSTEM, "Activation date reached", now);
  }

  public void expire(Instant now) {
    if (!canExpire(now)) {
      throw new IllegalStateException("Expiration date not reached");
    }
    transitionTo(ContractStatus.EXPIRED, SYSTEM, "Contract expired", now);
  }

  public void cancel(String cancelledBy, String reason, Instant now) {
    transitionTo(ContractStatus.CANCELLED, defaultIfNull(cancelledBy), reason, now);
  }

  // ======================
  // AUTO REFRESH
  // ======================

  public void refreshStatusIfNeeded() {
    ContractStatus effective = getEffectiveStatus();
    if (effective != status) {
      transitionTo(effective, SYSTEM, "Auto transition by temporal rule", Instant.now());
    }
  }

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

  // ======================
  // TRANSITION CORE
  // ======================

  private void transitionTo(ContractStatus newStatus, String changedBy, String reason, Instant now)
      throws IllegalStateException {
    if (status == newStatus) return;
    status.validateTransition(newStatus);

    statusHistories.add(
        ContractStatusHistory.builder()
            .fromStatus(status)
            .toStatus(newStatus)
            .changedBy(changedBy)
            .changedAt(now)
            .reason(reason)
            .build());

    status = newStatus;
    lastStatusChangedAt = now;
    lastStatusChangedBy = changedBy;
  }

  // ======================
  // STATE CHECKS (PURE)
  // ======================

  public boolean canActivate(Instant now) {
    return status == ContractStatus.SIGNED
        && activationDate != null
        && !now.isBefore(activationDate);
  }

  public boolean canExpire(Instant now) {
    return status == ContractStatus.ACTIVE && expiredDate != null && !now.isBefore(expiredDate);
  }

  public boolean isFreezed(Instant now) {
    return status == ContractStatus.SIGNED
        && activationDate != null
        && !now.isBefore(getFreezeDate());
  }

  public Instant getFreezeDate() {
    Instant signedAt =
        status == ContractStatus.SIGNED
            ? lastStatusChangedAt
            : statusHistories.stream()
                .filter(history -> history.getToStatus() == ContractStatus.SIGNED)
                .findFirst()
                .map(history -> history.getChangedAt())
                .orElse(null);

    return signedAt == null ? null : signedAt.plusSeconds(contractFreezeDelayMinutes * 60L);
  }

  public boolean isDraft() {
    return status == ContractStatus.DRAFT;
  }

  public boolean isSigned() {
    return status == ContractStatus.SIGNED;
  }

  public boolean isActive() {
    return status == ContractStatus.ACTIVE;
  }

  public boolean isExpired() {
    return status == ContractStatus.EXPIRED;
  }

  public boolean isCancelled() {
    return status == ContractStatus.CANCELLED;
  }

  public List<ContractStatusHistory> getStatusHistories() {
    return Collections.unmodifiableList(statusHistories);
  }

  // ======================
  // UTIL
  // ======================

  private String defaultIfNull(String value) {
    return value != null ? value : SYSTEM;
  }
}
