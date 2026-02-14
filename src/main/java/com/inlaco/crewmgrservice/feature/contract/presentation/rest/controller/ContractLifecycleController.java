package com.inlaco.crewmgrservice.feature.contract.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.contract.application.port.in.SignContractUseCase;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import com.inlaco.crewmgrservice.infrastructure.web.validation.annotation.ObjectId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
@Tag(name = "Contract Lifecycle")
public class ContractLifecycleController {

  private final SignContractUseCase signContractUseCase;

  @Operation(
      summary = "Active an contract by id",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("/active/{id}")
  @RolesAllowed("ADMIN")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void activate(@ObjectId @PathVariable String id, @CurrentUser User user) {
    signContractUseCase.sign(id, user);
  }
}
