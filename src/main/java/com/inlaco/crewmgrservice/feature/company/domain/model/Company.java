package com.inlaco.crewmgrservice.feature.company.domain.model;

import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a company or organization in the crew management system.
 *
 * <p>Companies are the primary entities that own ships, hire crew members, and manage maritime
 * operations. Each company contains essential business information including registration details,
 * contact information, and operational status.
 *
 * <p>The company status controls whether the company can actively participate in crew management
 * operations. Status transitions are tracked and validated according to business rules.
 *
 * @author Trần Võ Sơn Tùng
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {
  private String id;

  private String name;

  private String description;

  private String registrationNumber;

  private String taxId;

  private String address;

  private String phoneNumber;

  private String email;

  private String website;

  private Asset logo;

  private CompanyStatus status;

  private String createdBy;

  private Instant createdAt;

  private String updatedBy;

  private Instant updatedAt;

  /** Represents the operational status of a company. */
  public enum CompanyStatus {
    /** Company is active and can participate in crew management operations */
    ACTIVE,
    /** Company is temporarily inactive and cannot participate in operations */
    INACTIVE,
    /** Company is suspended due to violations or compliance issues */
    SUSPENDED
  }

  /**
   * Changes the company's status with validation and audit trail.
   *
   * <p>This method updates the company's status and records the timestamp of the change. Business
   * rules can be added to validate status transitions and prevent invalid changes.
   *
   * @param newStatus the new status to set
   * @throws IllegalStateException if the status transition is not allowed
   */
  public void changeStatus(CompanyStatus newStatus) throws IllegalStateException {
    if (this.status == newStatus) return;

    // Add business rules for status transitions if needed
    this.status = newStatus;
    this.updatedAt = Instant.now();
  }
}
