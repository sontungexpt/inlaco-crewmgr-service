package com.inlaco.crewmgrservice.feature.contract.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.contract.application.port.in.UpdateContractUseCase;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.update.ContractPatchRequest;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.ContractResponse;
import com.inlaco.crewmgrservice.feature.contract.presentation.mapper.ContractMapper;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.validation.annotation.ObjectId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
public class ContractCommandController {

  private final UpdateContractUseCase updateContractUseCase;
  private final ContractMapper contractMapper;

  @Operation(
      summary = "Update contract",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("ADMIN")
  @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
  public ContractResponse updateContract(
      @ObjectId @PathVariable("id") String id, @RequestBody ContractPatchRequest patch) {
    return contractMapper.toContractResponse(
        updateContractUseCase.update(id, contractMapper.toUpdateContractCommand(patch)));
  }
}
