package com.inlaco.crewmgrservice.feature.contract.domain.model;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
<<<<<<< Updated upstream
import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractActivedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractExpiredEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractSignedEvent;
=======
>>>>>>> Stashed changes
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.Party;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.ContractStatusHistory;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
<<<<<<< Updated upstream
import java.util.Set;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractContract {

  private static final String SYSTEM = "SYSTEM";

  private final transient Set<Object> domainEvents = new HashSet<>();

  protected void registerEvent(Object event) {
    domainEvents.add(event);
  }

  public void broadcast(Consumer<Object> dispatcher) {
    domainEvents.forEach(dispatcher);
    domainEvents.clear();
  }

  private String id;
=======
import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public abstract class AbstractContract {

>>>>>>> Stashed changes
  private final ContractType type;

  protected AbstractContract(ContractType type) {
    this.type = type;
    this.status = ContractStatus.DRAFT;
  }

  // ======================
  // BASIC INFO
  // ======================

  private String title;
<<<<<<< Updated upstream
  private Party initiator;
  private List<Party> partners = new ArrayList<>();
=======

  private Party initiator;

  private List<Party> partners;

>>>>>>> Stashed changes
  private File contractFile;
  private List<File> attachments = new ArrayList<>();
  private List<String> terms = new ArrayList<>();
  private int version;

<<<<<<< Updated upstream
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
=======
  private List<File> attachments = new ArrayList<>();

  private List<String> terms;

  // ======================
  // STATUS
  // ======================

  private ContractStatus status;

  private final List<ContractStatusHistory> statusHistories = new ArrayList<>();

  private Instant lastStatusChangedAt;

>>>>>>> Stashed changes
  private String lastStatusChangedBy;

  // ======================
  // TIME
  // ======================

  private Instant activationDate;
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
  private Instant expiredDate;
  private int contractFreezeDelayMinutes = 5;

<<<<<<< Updated upstream
  // ======================
  // LIFECYCLE ACTIONS
  // ======================

  public void sign(String signedBy, Instant now) {
    transitionTo(ContractStatus.SIGNED, defaultIfNull(signedBy), "Contract signed", now);
    registerEvent(new ContractSignedEvent(this));
  }

  public void activate(Instant now) {
    if (!canActivate(now)) {
      throw new IllegalStateException("Activation date not reached");
    }
    transitionTo(ContractStatus.ACTIVE, SYSTEM, "Activation date reached", now);
    registerEvent(new ContractActivedEvent(this));
  }

  public void expire(Instant now) {
    if (!canExpire(now)) {
      throw new IllegalStateException("Expiration date not reached");
    }
    transitionTo(ContractStatus.EXPIRED, SYSTEM, "Contract expired", now);
    registerEvent(new ContractExpiredEvent(this));
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

  private void transitionTo(
      ContractStatus newStatus, String changedBy, String reason, Instant now) {

    if (status == newStatus) {
      return;
    }

    if (!status.canTransitionTo(newStatus)) {
      throw new IllegalStateException("Invalid transition from " + status + " to " + newStatus);
    }

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
=======
  private int contractFreezeDelay = 5;

  // ======================
  // BEHAVIOR
  // ======================

  public void sign(ObjectId userId) {
    changeStatus(
        ContractStatus.SIGNED, userId != null ? userId.toHexString() : "SYSTEM", "Contract signed");
  }

  public void activate() {
    if (activationDate == null || Instant.now().isBefore(activationDate)) {
      throw new IllegalStateException("Activation date not reached");
    }

    changeStatus(ContractStatus.ACTIVE, "SYSTEM", "Activation date reached");
  }

  public void expire() {
    if (expiredDate == null || Instant.now().isBefore(expiredDate)) {
      throw new IllegalStateException("Expiration date not reached");
    }

    changeStatus(ContractStatus.EXPIRED, "SYSTEM", "Contract expired");
  }

  public void cancel(ObjectId userId, String reason) {
    changeStatus(
        ContractStatus.CANCELLED, userId != null ? userId.toHexString() : "SYSTEM", reason);
  }

  private void changeStatus(ContractStatus newStatus, String changedBy, String reason) {

    if (this.status == newStatus) {
      return;
    }

    if (!this.status.canTransitionTo(newStatus)) {
      throw new IllegalStateException("Invalid transition from " + status + " to " + newStatus);
    }

    ContractStatusHistory history =
        ContractStatusHistory.builder()
            .fromStatus(this.status)
            .toStatus(newStatus)
            .changedBy(changedBy)
            .changedAt(Instant.now())
            .reason(reason)
            .build();

    this.statusHistories.add(history);
    this.status = newStatus;
    this.lastStatusChangedAt = history.getChangedAt();
    this.lastStatusChangedBy = changedBy;
  }

  // ======================
  // FREEZE LOGIC
  // ======================

  public boolean isFreezed() {
    return status == ContractStatus.ACTIVE
        && activationDate != null
        && Instant.now().isAfter(getFreezeDate());
  }

  public Instant getFreezeDate() {
    return activationDate == null ? null : activationDate.plusSeconds(contractFreezeDelay * 60L);
>>>>>>> Stashed changes
  }
}
