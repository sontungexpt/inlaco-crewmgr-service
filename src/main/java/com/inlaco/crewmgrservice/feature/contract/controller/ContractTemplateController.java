package com.inlaco.crewmgrservice.feature.contract.controller;

import com.inlaco.crewmgrservice.annotation.CurrentUser;
import com.inlaco.crewmgrservice.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.feature.contract.model.ContractTemplate;
import com.inlaco.crewmgrservice.feature.contract.service.ContractTemplateService;
import com.inlaco.crewmgrservice.feature.user.model.User;
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

@RestController
@RequestMapping("/api/v1/contract-templates")
@RequiredArgsConstructor
@Tag(name = "Template", description = "APIs for managing contracts")
public class ContractTemplateController {

  private final ContractTemplateService contractTemplateService;

  @Operation(
      summary = "Get all templates",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
"""
This API is used to get all templates.

**Usecase**:
- UC_admin-get-danh-sach-template.

""")
  @RolesAllowed("ADMIN")
  @GetMapping("")
  @PageableQueryParams
  public Page<ContractTemplate> getAllContractTemplates(
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return contractTemplateService.getAllTemplates(pageable);
  }

  @Operation(
      summary = "Upload template",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
"""
This api is used to upload template.

**usecase**:
- UC_admin-tao-template-hop-dong.

""")
  @RolesAllowed("ADMIN")
  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  public ContractTemplate uploadTemplate(
      @RequestBody @Valid ContractTemplate contractTemplate,
      @RequestParam String templateFileAssetId,
      @CurrentUser User user) {
    return contractTemplateService.uploadTemplate(templateFileAssetId, contractTemplate);
  }

  @Operation(
      summary = "Remove template",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)},
      description =
"""
This api is used to remove template.

**usecase**:
- UC_admin-xoa-template-hop-dong.

""")
  @RolesAllowed("ADMIN")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeTemplate(@PathVariable String id, @CurrentUser User user) {
    contractTemplateService.removeTemplate(id);
  }
}
