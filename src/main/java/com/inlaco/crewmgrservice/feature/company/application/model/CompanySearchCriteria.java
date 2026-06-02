package com.inlaco.crewmgrservice.feature.company.application.model;

import com.inlaco.crewmgrservice.feature.company.domain.model.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanySearchCriteria {
  private String name;
  private String registrationNumber;
  private Company.CompanyStatus status;
  private String email;
  private String phoneNumber;
}
