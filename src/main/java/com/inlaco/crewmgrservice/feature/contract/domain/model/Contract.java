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
import java.util.function.Function;
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

  private void incrementVersion() {
    version = new Version(version.num() + 1, Instant.now());
  }

  protected <T extends UpdateContractCommand> boolean applyChanges(T patch) {
    return patch.getTitle().ifUpdated(this::setTitle)
        | patch.getActivationDate().ifUpdated(this::setActivationDate)
        | patch.getExpiredDate().ifUpdated(this::setExpiredDate)
        | patch.getContractFreezeDelayMinutes().ifUpdated(this::setContractFreezeDelayMinutes)
        | patch.getInitiator().ifUpdated(this::setInitiator)
        | patch.getPartners().ifUpdated(this::setPartners);
  }

  protected <T extends UpdateContractCommand> boolean applyAssetChanges(
      T patch,
      Function<String, Asset> contractFileConverter,
      Function<List<String>, List<Asset>> attachmentsConverter) {
    boolean updated = false;

    String contractFileId = patch.getContractFile();
    if (contractFileId == null || contractFileId.isBlank()) {
      updated = updated | contractFile != null;
      contractFile = null;
    } else {
      Asset newContractFile = contractFileConverter.apply(contractFileId);
      updated = updated | this.contractFile != newContractFile;
      this.contractFile = newContractFile;
    }

    List<String> attachmentIds = patch.getAttachments();
    if (attachmentIds == null || attachmentIds.isEmpty()) {
      updated = updated | attachments != null;
      attachments = null;
    } else {
      List<Asset> newAttachments = attachmentsConverter.apply(patch.getAttachments());
      updated = updated | this.attachments != newAttachments;
      this.attachments = newAttachments;
    }
    return updated;
  }

  public <T extends UpdateContractCommand> void amend(
      T patch,
      Instant now,
      Function<String, Asset> contractFileConverter,
      Function<List<String>, List<Asset>> attachmentsConverter) {

    if (!(applyChanges(patch)
        | applyAssetChanges(patch, contractFileConverter, attachmentsConverter))) return;
    if (isFreezed(now)) {
      restartLifecycle(now);
      incrementVersion();
    }
  }

  private void restartLifecycle(Instant now) {
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
    return status == ContractStatus.SIGNED
        && activationDate != null
        && !now.isBefore(getFreezedDate());
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

  public void validateForSigning() {
    // ===== STATUS =====
    ContractValidator.require(isDraft(), "Only draft contracts can be signed");

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
