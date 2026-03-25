package com.inlaco.crewmgrservice.feature.contract.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.contract.application.model.ContractSearchCriteria;
import com.inlaco.crewmgrservice.feature.contract.application.port.in.ContractQueryUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.in.LaborContractQueryUseCase;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.ContractResponse;
import com.inlaco.crewmgrservice.feature.contract.presentation.mapper.ContractMapper;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
@Tag(name = "Contract Query")
public class ContractQueryController {

  private final ContractQueryUseCase contractQueryUseCase;
  private final ContractMapper contractMapper;
  private final LaborContractQueryUseCase laborContractQueryUseCase;

  @Operation(
      summary = "Get contract detail",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/{id}")
  @RolesAllowed("ADMIN")
  public ContractResponse getContract(
      @PathVariable String id, @RequestParam(required = false) Integer version) {
    return contractMapper.toContractResponse(contractQueryUseCase.getContract(id, version));
  }

  @GetMapping("/{id}/old-versions")
  public List<Contract> getOldContractVersions(@PathVariable String id) {
    return contractQueryUseCase.getOldContractVersions(id);
  }

  @Operation(
      summary = "Get contract detail for application",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/applications/{applicationId}")
  @RolesAllowed("ADMIN")
  public ContractResponse getContractForApplication(@PathVariable String applicationId) {
    return contractMapper.toContractResponse(
        laborContractQueryUseCase.getContractByApplicationId(applicationId));
  }

  @Operation(
      summary = "Get all conrtacts",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping
  @RolesAllowed("ADMIN")
  public Page<ContractResponse> getContracts(
      @Filter ContractSearchCriteria criteria,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {

    return contractQueryUseCase
        .getContracts(criteria, pageable)
        .map(contractMapper::toContractResponse);
  }
}
