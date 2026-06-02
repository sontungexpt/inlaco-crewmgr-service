package com.inlaco.crewmgrservice.feature.company.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.company.application.port.in.CompanyUseCase;
import com.inlaco.crewmgrservice.feature.company.domain.model.Company;
import com.inlaco.crewmgrservice.feature.company.presentation.mapper.CompanyMapper;
import com.inlaco.crewmgrservice.feature.company.presentation.rest.dto.request.CreateCompanyRequest;
import com.inlaco.crewmgrservice.feature.company.presentation.rest.dto.request.UpdateCompanyRequest;
import com.inlaco.crewmgrservice.feature.company.presentation.rest.dto.response.CompanyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Company Management", description = "API for managing companies")
public class CompanyController {

  private final CompanyUseCase companyUseCase;
  private final CompanyMapper companyMapper;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Create a new company")
  public ResponseEntity<CompanyResponse> createCompany(
      @Valid @RequestBody CreateCompanyRequest request) {
    log.info("Creating company with registration number: {}", request.getRegistrationNumber());

    Company company = companyMapper.toDomain(request);
    Company created = companyUseCase.createCompany(company, null);

    return ResponseEntity.status(HttpStatus.CREATED).body(companyMapper.toResponse(created));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Update an existing company")
  public ResponseEntity<CompanyResponse> updateCompany(
      @PathVariable String id, @Valid @RequestBody UpdateCompanyRequest request) {
    log.info("Updating company with ID: {}", id);

    Company company = companyMapper.toDomain(request);
    Company updated = companyUseCase.updateCompany(id, company, null);

    return ResponseEntity.ok(companyMapper.toResponse(updated));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Delete a company")
  public ResponseEntity<Void> deleteCompany(@PathVariable String id) {
    log.info("Deleting company with ID: {}", id);
    companyUseCase.deleteCompany(id, null);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get company by ID")
  public ResponseEntity<CompanyResponse> getCompany(@PathVariable String id) {
    log.debug("Fetching company with ID: {}", id);
    Company company = companyUseCase.getCompany(id);
    return ResponseEntity.ok(companyMapper.toResponse(company));
  }

  @GetMapping
  @Operation(summary = "Get all companies with pagination")
  public ResponseEntity<Page<CompanyResponse>> getCompanies(
      @Parameter(description = "Search term for company name") @RequestParam(required = false)
          String search,
      Pageable pageable) {
    log.debug("Fetching companies with search: {}", search);

    Page<Company> companies = companyUseCase.getCompanies(search, pageable);
    Page<CompanyResponse> responses = companies.map(companyMapper::toResponse);

    return ResponseEntity.ok(responses);
  }

  @GetMapping("/active")
  @Operation(summary = "Get all active companies")
  public ResponseEntity<List<CompanyResponse>> getActiveCompanies() {
    log.debug("Fetching active companies");
    List<Company> companies = companyUseCase.getActiveCompanies();
    List<CompanyResponse> responses = companies.stream().map(companyMapper::toResponse).toList();
    return ResponseEntity.ok(responses);
  }

  @PatchMapping("/{id}/status")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Change company status")
  public ResponseEntity<CompanyResponse> changeCompanyStatus(
      @PathVariable String id, @RequestParam Company.CompanyStatus status) {
    log.info("Changing status of company {} to {}", id, status);

    Company updated = companyUseCase.changeCompanyStatus(id, status, null);
    return ResponseEntity.ok(companyMapper.toResponse(updated));
  }
}
