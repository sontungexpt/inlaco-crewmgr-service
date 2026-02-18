package com.inlaco.crewmgrservice.feature.contract.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.contract.application.port.in.CreateSupplyContractUseCase;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.create.NewCrewSupplyContract;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.ContractResponse;
import com.inlaco.crewmgrservice.feature.contract.presentation.mapper.ContractMapper;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import com.inlaco.crewmgrservice.infrastructure.web.validation.annotation.ObjectId;
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
@RequestMapping("/api/v1/contracts/supplies")
@RequiredArgsConstructor
@Tag(name = "Labor Contract")
public class CrewSupplyContractController {

  private final CreateSupplyContractUseCase supplyContractUseCase;
  private final ContractMapper contractMapper;

  @Operation(
      summary = "Add contract for sailor",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("/{requestId}")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.CREATED)
  public ContractResponse create(
      @ObjectId @PathVariable("requestId") String requestId,
      @RequestParam String contractFileAssetId,
      @RequestParam String shipImageAssetId,
      @CurrentUser User creator,
      @RequestBody @Valid NewCrewSupplyContract contract) {
    return contractMapper.toContractResponse(
        supplyContractUseCase.create(
            requestId,
            contractMapper.toCrewSupplyContract(contract),
            contractFileAssetId,
            shipImageAssetId,
            creator));
  }
}
