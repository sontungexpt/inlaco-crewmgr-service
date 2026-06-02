package com.inlaco.crewmgrservice.feature.company.application.service;

import com.inlaco.crewmgrservice.feature.company.application.model.CompanySearchCriteria;
import com.inlaco.crewmgrservice.feature.company.application.port.in.CompanyUseCase;
import com.inlaco.crewmgrservice.feature.company.application.port.out.CompanyRepository;
import com.inlaco.crewmgrservice.feature.company.domain.error.CompanyErrorCode;
import com.inlaco.crewmgrservice.feature.company.domain.exception.CompanyException;
import com.inlaco.crewmgrservice.feature.company.domain.model.Company;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService implements CompanyUseCase {

  private final CompanyRepository companyRepository;
  private final UploadDispatcher uploadDispatcher;

  @Override
  public Company createCompany(Company company, User authenticatedUser) {
    log.debug("Creating company with registration number: {}", company.getRegistrationNumber());

    validateCompanyData(company);
    checkDuplicateRegistrationNumber(company.getRegistrationNumber());

    enrichCompany(company, authenticatedUser);

    Company created = companyRepository.save(company);
    log.info("Company created successfully with ID: {}", created.getId());

    return created;
  }

  @Override
  public Company updateCompany(String companyId, Company company, User authenticatedUser) {
    log.debug("Updating company with ID: {}", companyId);

    Company existingCompany = getCompany(companyId);

    validateCompanyData(company);
    
    if (!existingCompany.getRegistrationNumber().equals(company.getRegistrationNumber())) {
      checkDuplicateRegistrationNumber(company.getRegistrationNumber());
    }

    updateCompanyFields(existingCompany, company, authenticatedUser);

    Company updated = companyRepository.save(existingCompany);
    log.info("Company updated successfully with ID: {}", updated.getId());

    return updated;
  }

  @Override
  public void deleteCompany(String companyId, User authenticatedUser) {
    log.debug("Deleting company with ID: {}", companyId);

    Company existingCompany = getCompany(companyId);
    
    // Business rule: Cannot delete active companies
    if (existingCompany.getStatus() == Company.CompanyStatus.ACTIVE) {
      throw new CompanyException(
          CompanyErrorCode.INVALID_COMPANY_STATUS,
          "Cannot delete active company. Please deactivate it first.");
    }

    companyRepository.deleteById(companyId);
    log.info("Company deleted successfully with ID: {}", companyId);
  }

  @Override
  public Company getCompany(String companyId) {
    log.debug("Fetching company with ID: {}", companyId);
    return companyRepository
        .findById(companyId)
        .orElseThrow(() -> new ResourceNotFoundException(Company.class, "id", companyId));
  }

  @Override
  public Page<Company> getCompanies(String search, Pageable pageable) {
    log.debug("Fetching companies with search: {}", search);
    
    CompanySearchCriteria criteria = CompanySearchCriteria.builder()
        .name(search)
        .build();
    
    return companyRepository.findAll(criteria, pageable);
  }

  @Override
  public List<Company> getActiveCompanies() {
    log.debug("Fetching active companies");
    return companyRepository.findByStatus(Company.CompanyStatus.ACTIVE);
  }

  @Override
  public Company changeCompanyStatus(String companyId, Company.CompanyStatus status, User authenticatedUser) {
    log.debug("Changing status of company {} to {}", companyId, status);

    Company existingCompany = getCompany(companyId);
    existingCompany.changeStatus(status);
    existingCompany.setUpdatedBy(authenticatedUser.getId());
    existingCompany.setUpdatedAt(Instant.now());

    Company updated = companyRepository.save(existingCompany);
    log.info("Company status changed successfully to {} for ID: {}", status, updated.getId());

    return updated;
  }

  private void validateCompanyData(Company company) {
    if (!StringUtils.hasText(company.getName())) {
      throw new CompanyException(CompanyErrorCode.COMPANY_NAME_REQUIRED, "Company name is required");
    }

    if (!StringUtils.hasText(company.getRegistrationNumber())) {
      throw new CompanyException(
          CompanyErrorCode.REGISTRATION_NUMBER_REQUIRED, "Registration number is required");
    }

    if (StringUtils.hasText(company.getEmail()) && !isValidEmail(company.getEmail())) {
      throw new CompanyException(CompanyErrorCode.INVALID_EMAIL_FORMAT, "Invalid email format");
    }

    if (StringUtils.hasText(company.getPhoneNumber()) && !isValidPhoneNumber(company.getPhoneNumber())) {
      throw new CompanyException(CompanyErrorCode.INVALID_PHONE_NUMBER, "Invalid phone number format");
    }
  }

  private void checkDuplicateRegistrationNumber(String registrationNumber) {
    if (companyRepository.existsByRegistrationNumber(registrationNumber)) {
      throw new CompanyException(
          CompanyErrorCode.COMPANY_ALREADY_EXISTS,
          "Company with registration number " + registrationNumber + " already exists");
    }
  }

  private void enrichCompany(Company company, User authenticatedUser) {
    company.setCreatedBy(authenticatedUser.getId());
    company.setCreatedAt(Instant.now());
    company.setUpdatedBy(authenticatedUser.getId());
    company.setUpdatedAt(Instant.now());

    if (company.getStatus() == null) {
      company.setStatus(Company.CompanyStatus.ACTIVE);
    }

    // Enrich logo asset if present
    if (company.getLogo() != null) {
      company.setLogo(uploadDispatcher.enrich(AssetType.COMPANY_LOGO, company.getLogo()));
    }
  }

  private void updateCompanyFields(Company existingCompany, Company company, User authenticatedUser) {
    existingCompany.setName(company.getName());
    existingCompany.setDescription(company.getDescription());
    existingCompany.setRegistrationNumber(company.getRegistrationNumber());
    existingCompany.setTaxId(company.getTaxId());
    existingCompany.setAddress(company.getAddress());
    existingCompany.setPhoneNumber(company.getPhoneNumber());
    existingCompany.setEmail(company.getEmail());
    existingCompany.setWebsite(company.getWebsite());

    if (company.getLogo() != null) {
      existingCompany.setLogo(uploadDispatcher.enrich(AssetType.COMPANY_LOGO, company.getLogo()));
    }

    existingCompany.setUpdatedBy(authenticatedUser.getId());
    existingCompany.setUpdatedAt(Instant.now());
  }

  private boolean isValidEmail(String email) {
    return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
  }

  private boolean isValidPhoneNumber(String phoneNumber) {
    return phoneNumber.matches("^[+]?[0-9]{10,15}$");
  }
}
