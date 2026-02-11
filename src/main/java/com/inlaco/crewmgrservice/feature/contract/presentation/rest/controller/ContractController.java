package com.inlaco.crewmgrservice.feature.contract.presentation.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.contract.application.port.in.ContractUseCase;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.SupplyContract;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.ContractFilterable;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.infrastructure.web.validation.annotation.ObjectId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
@Tag(name = "Contract", description = "APIs for managing contracts")
public class ContractController {

  private final ContractUseCase contractService;

  @Operation(
      summary = "Get contract detail",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description = "This API is used to get contract detail.")
  @RolesAllowed("ADMIN")
  @GetMapping("/{id}")
  public Contract getContractById(@ObjectId @PathVariable("id") String id) {
    return contractService.getContractById(id);
  }

  @Operation(
      summary = "Get all conrtacts",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("ADMIN")
  @GetMapping("")
  @PageableQueryParams
  public Page<? extends Contract> getAllContracts(
      @RequestParam(required = false) ContractType type,
      @RequestParam(required = false) Instant activationDateStart,
      @RequestParam(required = false) Instant activationDateEnd,
      @RequestParam(required = false) Instant expiredDateStart,
      @RequestParam(required = false) Instant expiredDateEnd,
      @RequestParam(defaultValue = "true") boolean signed,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return contractService.getAllContracts(
        ContractFilterable.builder()
            .type(type)
            .activationDateStart(activationDateStart)
            .activationDateEnd(activationDateEnd)
            .expiredDateStart(expiredDateStart)
            .expiredDateEnd(expiredDateEnd)
            .signed(signed)
            .build(),
        pageable);
  }

  @Operation(
      summary = "Update contract",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("ADMIN")
  @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
  public Contract updateContract(
      @RequestParam(defaultValue = "false") boolean newVersion,
      @ObjectId @PathVariable("id") String id,
      @RequestBody JsonNode patch) {
    return contractService.updateContract(id, patch, newVersion);
  }

  @Operation(
      summary = "Get all contracts of sailor",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/labors/{id}")
  @RolesAllowed({"ADMIN", "SAILOR"})
  public Contract getLaborContractByEmployeeId(@ObjectId @PathVariable("id") String id) {
    return contractService.getLaborContractByEmployeeId(id);
  }

  @Operation(
      summary = "Add contract for sailor",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("/labors/{candidateProfileId}")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.CREATED)
  public Contract createLaborContract(
      @ObjectId @PathVariable("candidateProfileId") String candidateProfileId,
      @RequestParam(required = false) String contractFileAssetId,
      @CurrentUser User user,
      @RequestBody @Valid LaborContract contract) {
    return contractService.createLaborContract(
        candidateProfileId, contract, contractFileAssetId, user);
  }

  @Operation(
      summary = "Add supply contract",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("/supplies/{id}")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.CREATED)
  public Contract createSupplyContract(
      @ObjectId @PathVariable("id") String requestId,
      @RequestParam String contractFileAssetId,
      @RequestParam String shipImageAssetId,
      @CurrentUser User user,
      @RequestBody @Valid SupplyContract contract) {
    return contractService.createSupplyContract(
        requestId, contract, contractFileAssetId, shipImageAssetId, user);
  }

  @Operation(
      summary = "Active an contract by id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("/active/{id}")
  @RolesAllowed("ADMIN")
  public Contract activeContract(@ObjectId @PathVariable("id") String id, @CurrentUser User user) {
    return contractService.signContract(id, user);
  }
}
