package com.inlaco.crewmgrservice.feature.contract.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.annotation.CurrentUser;
import com.inlaco.crewmgrservice.feature.contract.dto.ContractFilterRequest;
import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.model.ContractType;
import com.inlaco.crewmgrservice.feature.contract.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.service.ContractService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.validation.annotation.ObjectId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
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
      summary = "Get all conrtacts",
      description =
          """
This API is used to get all contracts with short information.

**Usecase**:
- UC_admin-tim-kiem-loc-hop-dong.

""")
  @RolesAllowed("ADMIN")
  @GetMapping("")
  public Page<? extends Contract> getAllContracts(
      @ObjectId @PathVariable("id") String id,
      @RequestParam(required = false) ContractType type,
      @RequestParam(required = false) Instant activationDateStart,
      @RequestParam(required = false) Instant activationDateEnd,
      @RequestParam(required = false) Instant expiredDateStart,
      @RequestParam(required = false) Instant expiredDateEnd,
      @RequestParam(required = false) Boolean signed,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return contractService.getAllContracts(
        ContractFilterRequest.builder()
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
      description =
          """
Update contract if it is not freezed.

**Usecase**:
- UC_admin-tao-hop-dong-theo-template.

""")
  @RolesAllowed("ADMIN")
  @PatchMapping("/{id}")
  public void updateContract(@ObjectId @PathVariable("id") String id, @RequestBody JsonNode patch) {
    contractService.updateContract(id, patch);
  }

  @Operation(
      summary = "Get all contracts of sailor",
      description =
          """
Get all contracts of sailor.

**Usecase**:

- UC_admin-tim-kiem-loc-hop-dong.

""")
  @GetMapping("/sailors/{id}")
  @RolesAllowed({"ADMIN", "SAILOR"})
  public Page<? extends Contract> getSailorContracts(
      @ObjectId @PathVariable("id") String id,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return contractService.getSailorContracts(id, pageable);
  }

  @Operation(
      summary = "Add contract for sailor",
      description =
          """
Add contract for sailor.

**Usecase**:
- UC_admin-tao-hop-dong-theo-template.

""")
  @PostMapping("/sailors/{id}")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.CREATED)
  public void createSailorLaborContract(
      @ObjectId @PathVariable("id") String id,
      @CurrentUser User user,
      @RequestBody LaborContract contract) {
    contractService.createSailorLaborContract(id, contract, user);
  }
}
