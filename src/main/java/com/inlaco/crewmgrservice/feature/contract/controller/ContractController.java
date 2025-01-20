package com.inlaco.crewmgrservice.feature.contract.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.annotation.CurrentUser;
import com.inlaco.crewmgrservice.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.feature.contract.dto.ContractFilterable;
import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.model.ContractType;
import com.inlaco.crewmgrservice.feature.contract.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.model.SupplyContract;
import com.inlaco.crewmgrservice.feature.contract.service.ContractService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.validation.annotation.ObjectId;
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

  private final ContractService contractService;

  @Operation(
      summary = "Get contract detail",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
This API is used to get contract detail.

**Usecase**:
- UC_admin-tim-kiem-loc-hop-dong.

""")
  @RolesAllowed("ADMIN")
  @GetMapping("/{id}")
  public Contract getContractById(@ObjectId @PathVariable("id") String id) {
    return contractService.getContractById(id);
  }

  //   @Operation(
  //       summary = "Search conrtacts",
  //       security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
  //       description =
  //           """
  // This API is used to search contracts.
  //
  // **Usecase**:
  // - UC_admin-tim-kiem-loc-hop-dong.

  // """)
  //   @RolesAllowed("ADMIN")
  //   @GetMapping("/search")
  //   @PageableQueryParams
  //   public Page<? extends Contract> searchConrtacts(
  //       @RequestParam String q,
  //       @RequestParam(required = false) ContractType type,
  //       @RequestParam(required = false) Instant activationDateStart,
  //       @RequestParam(required = false) Instant activationDateEnd,
  //       @RequestParam(required = false) Instant expiredDateStart,
  //       @RequestParam(required = false) Instant expiredDateEnd,
  //       @RequestParam(defaultValue = "true") boolean signed,
  //       @PageableDefault(page = 0, size = 20) Pageable pageable) {
  //     return contractService.search(
  //         ContractFilterable.builder()
  //             .type(type)
  //             .activationDateStart(activationDateStart)
  //             .activationDateEnd(activationDateEnd)
  //             .expiredDateStart(expiredDateStart)
  //             .expiredDateEnd(expiredDateEnd)
  //             .signed(signed)
  //             .build(),
  //         pageable);
  //   }

  @Operation(
      summary = "Get all conrtacts",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
This API is used to get all contracts with short information.

**Usecase**:
- UC_admin-tim-kiem-loc-hop-dong.

""")
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
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Update contract if it is not freezed.

**Usecase**:
- UC_admin-tao-hop-dong-theo-template.

""")
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
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Get all contracts of sailor.

**Usecase**:

- UC_admin-tim-kiem-loc-hop-dong.

""")
  @GetMapping("/labors/{id}")
  @RolesAllowed({"ADMIN", "SAILOR"})
  public Contract getLaborContractByEmployeeId(@ObjectId @PathVariable("id") String id) {
    return contractService.getLaborContractByEmployeeId(id);
  }

  @Operation(
      summary = "Add contract for sailor",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Add contract for sailor.

**Usecase**:
- UC_admin-tao-hop-dong-theo-template.

""")
  @PostMapping("/labors/{id}")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.CREATED)
  public Contract createLaborContract(
      @ObjectId @PathVariable("id") String id,
      @CurrentUser User user,
      @RequestBody @Valid LaborContract contract) {
    return contractService.createLaborContract(id, contract, user);
  }

  @Operation(
      summary = "Add supply contract",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Add supply contract

**Usecase**:
- UC_admin-tao-hop-dong-theo-template.

""")
  @PostMapping("/supplies/{id}")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.CREATED)
  public Contract createSupplyContract(
      @ObjectId @PathVariable("id") String id,
      @CurrentUser User user,
      @RequestBody @Valid SupplyContract contract) {
    return contractService.createSupplyContract(id, contract, user);
  }

  @Operation(
      summary = "Active an contract by id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
          """
Active an contract by id.

**Usecase**:
- UC_admin-tao-hop-dong-theo-template.

""")
  @PostMapping("/active/{id}")
  @RolesAllowed("ADMIN")
  public Contract activeContract(@ObjectId @PathVariable("id") String id, @CurrentUser User user) {
    return contractService.activeContract(id, user);
  }
}
