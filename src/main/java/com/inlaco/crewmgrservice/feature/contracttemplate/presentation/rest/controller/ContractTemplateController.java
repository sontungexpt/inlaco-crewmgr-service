package com.inlaco.crewmgrservice.feature.contracttemplate.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.contracttemplate.application.model.ContractTemplateSearchCriteria;
import com.inlaco.crewmgrservice.feature.contracttemplate.application.port.in.ContractTemplateUseCase;
import com.inlaco.crewmgrservice.feature.contracttemplate.domain.model.ContractTemplate;
import com.inlaco.crewmgrservice.feature.contracttemplate.presentation.rest.dto.request.NewContractTemplate;
import com.inlaco.crewmgrservice.feature.contracttemplate.presentation.rest.dto.response.ContractTemplateResponse;
import com.inlaco.crewmgrservice.feature.contracttemplate.presentation.rest.mapper.ContractTemplateMapper;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.Filter;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.PageableQueryParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Template", description = "APIs for managing contracts")
@RequiredArgsConstructor
@RequestMapping("/api/v1/contract-templates")
@RestController
public class ContractTemplateController {

  private final ContractTemplateUseCase contractTemplateService;
  private final ContractTemplateMapper contractTemplateMapper;

  @Operation(
      summary = "Get all templates",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("ADMIN")
  @GetMapping("")
  @PageableQueryParams
  public Page<ContractTemplateResponse> getAllContractTemplates(
      @Filter ContractTemplateSearchCriteria criteria,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return contractTemplateService
        .getAllTemplates(criteria, pageable)
        .map(contractTemplateMapper::toContractTemplateResponse);
  }

  @Operation(
      summary = "Upload template",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("ADMIN")
  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  public ContractTemplate uploadTemplate(
      @RequestBody @Valid NewContractTemplate contractTemplate,
      @RequestParam String templateFileAssetId) {
    return contractTemplateService.uploadTemplate(
        templateFileAssetId, contractTemplateMapper.toContractTemplate(contractTemplate));
  }

  @Operation(
      summary = "Remove template",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("ADMIN")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeTemplate(@PathVariable String id) {
    contractTemplateService.removeTemplate(id);
  }
}
