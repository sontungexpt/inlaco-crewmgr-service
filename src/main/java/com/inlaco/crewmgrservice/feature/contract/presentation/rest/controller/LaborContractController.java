package com.inlaco.crewmgrservice.feature.contract.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.contract.application.port.in.CreateLaborContractUseCase;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.LaborContractRequest;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.AbstractContractResponse;
import com.inlaco.crewmgrservice.feature.contract.presentation.mapper.ContractMapper;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contracts/labors")
@RequiredArgsConstructor
@Tag(name = "Labor Contract")
public class LaborContractController {

  private final CreateLaborContractUseCase laborContractUseCase;
  private final ContractMapper contractMapper;

  @Operation(
      summary = "Add contract for sailor",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("/{applicationId}")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.CREATED)
  public AbstractContractResponse create(
      @PathVariable("applicationId") String applicationId,
      @RequestParam(required = false) String contractFileAssetId,
      @CurrentUser User user,
      @RequestBody @Valid LaborContractRequest request) {
    return contractMapper.toContractResponse(
        laborContractUseCase.create(
            applicationId, contractMapper.toLaborContract(request), contractFileAssetId, user));
  }
}
