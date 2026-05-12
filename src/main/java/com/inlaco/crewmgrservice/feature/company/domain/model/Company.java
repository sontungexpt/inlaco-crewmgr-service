package com.inlaco.crewmgrservice.feature.company.domain.model;

import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

  public enum CompanyStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED
  }

  public void changeStatus(CompanyStatus newStatus) throws IllegalStateException {
    if (this.status == newStatus) return;
    
    // Add business rules for status transitions if needed
    this.status = newStatus;
    this.updatedAt = Instant.now();
  }
}
