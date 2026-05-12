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

/**
 * Abstract base class representing a contract in the crew management system.
 *
 * <p>Contracts are legal agreements that define the terms and conditions for crew employment, ship
 * operations, and other business relationships. This abstract class provides the common structure
 * and lifecycle management for all contract types.
 *
 * <p>Contracts follow a defined lifecycle from DRAFT through various operational states, with
 * automatic status transitions based on time-based rules and manual actions. All changes are
 * tracked with audit trails for compliance and legal requirements.
 *
 * <p>The contract system supports flexible crew mobilization, allowing multiple mobilization
 * batches per contract according to business needs, without being constrained by the initial
 * request quantities.
 *
 * @author Trần Võ Sơn Tùng
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public abstract class Contract {

  /** Constant used to indicate system-initiated actions */
  private static final String SYSTEM = "SYSTEM";

  /** Unique identifier for the contract */
  private String id;

  /** Type of contract (e.g., CREW_RENTAL, SHIP_CHARTER, etc.) */
  private final ContractType type;

  /**
   * Constructs a new Contract with specified type.
   *
   * @param type the type of contract being created
   */
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

  /**
   * Increments the contract version number with timestamp.
   *
   * <p>This method creates a new version of the contract with an incremented version number and
   * current timestamp, typically used when contract is modified or when creating a new version from
   * an existing one.
   *
   * @param now the timestamp for the new version
   */
  public void incrementVersion(Instant now) {
    version = new Version(version.num() + 1, now);
  }

  /**
   * Restarts the contract lifecycle to DRAFT status.
   *
   * <p>This method resets the contract to DRAFT status, clears the contract file and status
   * history, and records the transition. This is typically used when creating a new version of an
   * existing contract.
   *
   * @param now the timestamp when the lifecycle is restarted
   */
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

  /**
   * Signs the contract, transitioning it to SIGNED status.
   *
   * <p>This method marks the contract as signed by the specified party, recording the signature
   * action in the status history.
   *
   * @param signedBy the party or user who signed the contract
   * @param now the timestamp when the contract was signed
   */
  public void sign(String signedBy, Instant now) {
    transitionTo(ContractStatus.SIGNED, defaultIfNull(signedBy), "Contract signed", now);
  }

  /**
   * Activates the contract when activation date is reached.
   *
   * <p>This method automatically transitions the contract to ACTIVE status when the activation date
   * has been reached. It validates that the activation conditions are met before making the
   * transition.
   *
   * @param now the current timestamp to check against activation date
   * @throws IllegalStateException if activation date has not been reached
   */
  public void activate(Instant now) {
    if (!canActivate(now)) {
      throw new IllegalStateException("Activation date not reached");
    }
    transitionTo(ContractStatus.ACTIVE, SYSTEM, "Activation date reached", now);
  }

  /**
   * Expires the contract when expiration date is reached.
   *
   * <p>This method automatically transitions the contract to EXPIRED status when the expiration
   * date has been reached. It validates that the expiration conditions are met before making the
   * transition.
   *
   * @param now the current timestamp to check against expiration date
   * @throws IllegalStateException if expiration date has not been reached
   */
  public void expire(Instant now) {
    if (!canExpire(now)) {
      throw new IllegalStateException("Expiration date not reached");
    }
    transitionTo(ContractStatus.EXPIRED, SYSTEM, "Contract expired", now);
  }

  /**
   * Cancels the contract with specified reason.
   *
   * <p>This method manually transitions the contract to CANCELLED status, recording the party
   * responsible for cancellation and the reason provided.
   *
   * @param cancelledBy the party or user who cancelled the contract
   * @param reason the reason for contract cancellation
   * @param now the timestamp when the contract was cancelled
   */
  public void cancel(String cancelledBy, String reason, Instant now) {
    transitionTo(ContractStatus.CANCELLED, defaultIfNull(cancelledBy), reason, now);
  }

  /**
   * Refreshes the contract status based on current time.
   *
   * <p>This method checks if the contract should automatically transition to a new status based on
   * temporal rules (activation/expiration dates) and updates the status accordingly. This is
   * typically called by scheduled jobs.
   */
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

  /**
   * Checks if the contract can be activated at the specified time.
   *
   * <p>A contract can be activated if it's currently SIGNED and the activation date has been
   * reached. This method does not modify the contract state.
   *
   * @param now the timestamp to check against activation date
   * @return true if the contract can be activated, false otherwise
   */
  public boolean canActivate(Instant now) {
    return status == ContractStatus.SIGNED
        && activationDate != null
        && !now.isBefore(activationDate);
  }

  /**
   * Checks if the contract can be expired at the specified time.
   *
   * <p>A contract can be expired if it's currently ACTIVE and the expiration date has been reached.
   * This method does not modify the contract state.
   *
   * @param now the timestamp to check against expiration date
   * @return true if the contract can be expired, false otherwise
   */
  public boolean canExpire(Instant now) {
    return status == ContractStatus.ACTIVE && expiredDate != null && !now.isBefore(expiredDate);
  }

  /**
   * Checks if the contract is currently frozen.
   *
   * <p>A contract is considered frozen if it has been signed and the activation date has passed,
   * but it's still within the freeze delay period. During freeze period, certain operations may be
   * restricted.
   *
   * @param now the timestamp to check against freeze conditions
   * @return true if the contract is frozen, false otherwise
   */
  public boolean isFreezed(Instant now) {
    return isSigned() && activationDate != null && !now.isBefore(getFreezedDate());
  }

  /**
   * Calculates the freeze end date for the contract.
   *
   * <p>The freeze period starts when a contract is signed and lasts for the configured number of
   * minutes. During this period, the contract cannot be activated.
   *
   * @return the timestamp when the freeze period ends, or null if never signed
   */
  public Instant getFreezedDate() {
    return statusHistories.stream()
        .filter(history -> history.toStatus() == ContractStatus.SIGNED)
        .findFirst()
        .map(history -> history.changedAt().plusSeconds(contractFreezeDelayMinutes * 60L))
        .orElse(null);
  }

  /**
   * Checks if the contract is in DRAFT status.
   *
   * @return true if the contract is a draft, false otherwise
   */
  public boolean isDraft() {
    return status == ContractStatus.DRAFT;
  }

  /**
   * Checks if the contract has been signed (any status except DRAFT).
   *
   * @return true if the contract is signed, false otherwise
   */
  public boolean isSigned() {
    return !isDraft();
  }

  /**
   * Checks if the contract is currently ACTIVE.
   *
   * @return true if the contract is active, false otherwise
   */
  public boolean isActive() {
    return status == ContractStatus.ACTIVE;
  }

  /**
   * Checks if the contract is EXPIRED.
   *
   * @return true if the contract is expired, false otherwise
   */
  public boolean isExpired() {
    return status == ContractStatus.EXPIRED;
  }

  /**
   * Checks if the contract is CANCELLED.
   *
   * @return true if the contract is cancelled, false otherwise
   */
  public boolean isCancelled() {
    return status == ContractStatus.CANCELLED;
  }

  /**
   * Returns an unmodifiable view of the contract's status history.
   *
   * @return list of status history entries in chronological order
   */
  public List<ContractStatusHistory> getStatusHistories() {
    return Collections.unmodifiableList(statusHistories);
  }

  /**
   * Validates that activation date is before expiration date.
   *
   * <p>This method performs basic date validation to ensure that the contract timeline is logical.
   * If either date is null, no validation is performed.
   *
   * @throws ContractValidationException if activation date is after expiration date
   */
  public void validateDate() {
    if (activationDate == null || expiredDate == null) {
      return;
    }

    if (!activationDate.isBefore(expiredDate)) {
      throw new ContractValidationException(
          "Contract activation date must be before contract expiration date", "activationDate");
    }
  }

  /**
   * Validates that the contract is ready for signing.
   *
   * <p>This method performs comprehensive validation of all required fields and business rules
   * before a contract can be signed. It validates status, basic information, parties, and
   * time-related constraints.
   *
   * @throws ContractValidationException if any validation rule is violated
   */
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

  /**
   * Returns the provided value or SYSTEM constant if value is null.
   *
   * <p>This utility method is used to ensure that system-initiated actions are properly attributed
   * when no specific user or party is available.
   *
   * @param value the value to check for null
   * @return the original value or SYSTEM constant if null
   */
  private String defaultIfNull(String value) {
    return value != null ? value : SYSTEM;
  }

  /**
   * Utility class providing contract validation methods.
   *
   * <p>This static utility class provides common validation methods used throughout the contract
   * domain to ensure data integrity and business rule compliance. All validation failures throw
   * ContractValidationException with appropriate messages.
   */
  public static final class ContractValidator {
    /**
     * Validates that a condition is true, throwing exception if not.
     *
     * @param condition the condition to validate
     * @param message error message to include in exception
     * @param field optional field name that caused the validation failure
     * @throws ContractValidationException if condition is false
     */
    public static void require(boolean condition, String message, String field) {
      if (!condition) throw new ContractValidationException(message, field);
    }

    /**
     * Validates that a condition is true, throwing exception if not.
     *
     * @param condition the condition to validate
     * @param message error message to include in exception
     * @throws ContractValidationException if condition is false
     */
    public static void require(boolean condition, String message) {
      require(condition, message, null);
    }

    /**
     * Validates that a string is not null or blank.
     *
     * @param value the string to validate
     * @param message error message to include in exception
     * @param field optional field name that caused the validation failure
     * @throws ContractValidationException if value is null or blank
     */
    public static void notBlank(String value, String message, String field) {
      require(value != null && !value.isBlank(), message, field);
    }

    /**
     * Validates that a string is not null or blank.
     *
     * @param value the string to validate
     * @param message error message to include in exception
     * @throws ContractValidationException if value is null or blank
     */
    public static void notBlank(String value, String message) {
      notBlank(value, message, null);
    }

    /**
     * Validates that an object is not null.
     *
     * @param value the object to validate
     * @param message error message to include in exception
     * @param field optional field name that caused the validation failure
     * @throws ContractValidationException if value is null
     */
    public static void notNull(Object value, String message, String field) {
      require(value != null, message, field);
    }

    /**
     * Validates that an object is not null.
     *
     * @param value the object to validate
     * @param message error message to include in exception
     * @throws ContractValidationException if value is null
     */
    public static void notNull(Object value, String message) {
      notNull(value, message, null);
    }

    /**
     * Validates that a list is not null or empty.
     *
     * @param list the list to validate
     * @param message error message to include in exception
     * @param field optional field name that caused the validation failure
     * @throws ContractValidationException if list is null or empty
     */
    public static void notEmpty(List<?> list, String message, String field) {
      require(list != null && !list.isEmpty(), message, field);
    }

    /**
     * Validates that a list is not null or empty.
     *
     * @param list the list to validate
     * @param message error message to include in exception
     * @throws ContractValidationException if list is null or empty
     */
    public static void notEmpty(List<?> list, String message) {
      notEmpty(list, message, null);
    }
  }
}
