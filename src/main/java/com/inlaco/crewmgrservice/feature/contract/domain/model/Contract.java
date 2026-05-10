package com.inlaco.crewmgrservice.feature.contract.domain.model;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.exception.ContractValidationException;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.Party;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.ContractStatusHistory;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.DynamicAttribute;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.Version;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Contract {

  private static final String SYSTEM = "SYSTEM";

  private String id;
  private final ContractType type;

  protected Contract(ContractType type) {
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
  private Version version = new Version(1, Instant.now());
  private List<DynamicAttribute> customAttributes = new ArrayList<>();

  // ======================
  // STATUS
  // ======================
  private ContractStatus status = ContractStatus.DRAFT;
  private List<ContractStatusHistory> statusHistories = new ArrayList<>();

  // ======================
  // TIME
  // ======================

  private Instant activationDate;
  private Instant expiredDate;
  private int contractFreezeDelayMinutes = 5;

  public void incrementVersion(Instant now) {
    version = new Version(version.num() + 1, now);
  }

  public void restartLifecycle(Instant now) {
    contractFile = null;
    statusHistories.clear();
    ContractStatus oldStatus = status;
    status = ContractStatus.DRAFT;
    statusHistories.add(
        new ContractStatusHistory(oldStatus, status, SYSTEM, now, "New version created"));
  }

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

  public void refreshStatus() {
    transitionTo(
        getEffectiveStatus(Instant.now()),
        SYSTEM,
        "Auto transition by temporal rule",
        Instant.now());
  }

  public ContractStatus getEffectiveStatus(Instant now) {
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
    statusHistories.add(new ContractStatusHistory(status, newStatus, changedBy, now, reason));
    status = newStatus;
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
    return isSigned() && activationDate != null && !now.isBefore(getFreezedDate());
  }

  public Instant getFreezedDate() {
    return statusHistories.stream()
        .filter(history -> history.toStatus() == ContractStatus.SIGNED)
        .findFirst()
        .map(history -> history.changedAt().plusSeconds(contractFreezeDelayMinutes * 60L))
        .orElse(null);
  }

  public boolean isDraft() {
    return status == ContractStatus.DRAFT;
  }

  public boolean isSigned() {
    return !isDraft();
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

  public void validateDate() {
    if (activationDate == null || expiredDate == null) {
      return;
    }

    if (!activationDate.isBefore(expiredDate)) {
      throw new ContractValidationException(
          "Contract activation date must be before contract expiration date", "activationDate");
    }
  }

  public void validateForSigning() {
    // ===== STATUS =====
    ContractValidator.require(isDraft(), "Only draft contracts can be signed", "status");

    // ===== BASIC =====
    ContractValidator.notNull(contractFile, "Contract file is required", "contract");
    ContractValidator.notBlank(title, "Title is required", "title");
    ContractValidator.notNull(type, "Type is required");
    ContractValidator.notBlank(id, "Id is required");
    ContractValidator.notNull(version, "Version is required");

    // ===== PARTY =====
    ContractValidator.notNull(initiator, "Initiator is required");
    ContractValidator.notEmpty(partners, "At least one partner is required");
    for (int i = 0; i < partners.size(); i++) {
      ContractValidator.notNull(partners.get(i), "Partner[" + i + "] is required");
    }

    // ===== TIME =====
    ContractValidator.notNull(activationDate, "Activation date is required");
    ContractValidator.notNull(expiredDate, "Expired date is required");
    if (activationDate.isAfter(expiredDate)) {
      throw new ContractValidationException(
          "Activation date must be before expired date", "activationDate");
    }
    ContractValidator.require(
        expiredDate.isAfter(activationDate), "Expired date must be after activation date");
  }

  // ======================
  // UTIL
  // ======================

  private String defaultIfNull(String value) {
    return value != null ? value : SYSTEM;
  }

  public static final class ContractValidator {
    public static void require(boolean condition, String message, String field) {
      if (!condition) throw new ContractValidationException(message, field);
    }

    public static void require(boolean condition, String message) {
      require(condition, message, null);
    }

    public static void notBlank(String value, String message, String field) {
      require(value != null && !value.isBlank(), message, field);
    }

    public static void notBlank(String value, String message) {
      notBlank(value, message, null);
    }

    public static void notNull(Object value, String message, String field) {
      require(value != null, message, field);
    }

    public static void notNull(Object value, String message) {
      notNull(value, message, null);
    }

    public static void notEmpty(List<?> list, String message, String field) {
      require(list != null && !list.isEmpty(), message, field);
    }

    public static void notEmpty(List<?> list, String message) {
      notEmpty(list, message, null);
    }
  }
}
