package com.inlaco.crewmgrservice.feature.company.presentation.rest.dto.request;

import com.inlaco.crewmgrservice.feature.company.domain.model.Company;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCompanyRequest {

  @NotBlank(message = "Company name is required")
  @Size(max = 200, message = "Company name must not exceed 200 characters")
  private String name;

  @Size(max = 1000, message = "Description must not exceed 1000 characters")
  private String description;

  @NotBlank(message = "Registration number is required")
  @Size(max = 50, message = "Registration number must not exceed 50 characters")
  private String registrationNumber;

  @Size(max = 50, message = "Tax ID must not exceed 50 characters")
  private String taxId;

  @Size(max = 500, message = "Address must not exceed 500 characters")
  private String address;

  @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
  private String phoneNumber;

  @Email(message = "Invalid email format")
  @Size(max = 100, message = "Email must not exceed 100 characters")
  private String email;

  @Size(max = 200, message = "Website must not exceed 200 characters")
  private String website;

  private Asset logo;

  private Company.CompanyStatus status;
}
