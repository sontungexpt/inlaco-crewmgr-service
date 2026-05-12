package com.inlaco.crewmgrservice.feature.company.presentation.rest.dto.response;

import com.inlaco.crewmgrservice.feature.company.domain.model.Company;
import com.inlaco.crewmgrservice.shared.objectvalue.AssetResponse;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponse {

  private String id;
  private String name;
  private String description;
  private String registrationNumber;
  private String taxId;
  private String address;
  private String phoneNumber;
  private String email;
  private String website;
  private AssetResponse logo;
  private Company.CompanyStatus status;
  private String createdBy;
  private String updatedBy;
  private Instant createdAt;
  private Instant updatedAt;
}
