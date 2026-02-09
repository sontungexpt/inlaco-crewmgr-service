package com.inlaco.crewmgrservice.feature.upload.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.upload.application.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadFactory;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
@Tag(name = "Upload", description = "Upload API")
public class UploadController {

  private final UploadFactory uploadFactory;

  @Operation(
      summary = "This endpoint is used to upload a file",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("")
  @RolesAllowed("USER")
  public Map<String, Object> getUploadOptions(
      @RequestParam UploadStrategy strategy, @RequestParam Map<String, String> params) {
    return uploadFactory.getUploadOptions(strategy, params);
  }
}
