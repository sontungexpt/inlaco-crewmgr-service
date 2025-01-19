package com.inlaco.crewmgrservice.feature.upload.controller;

import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadOptions;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadRequest;
import com.inlaco.crewmgrservice.feature.upload.dto.UploadType;
import com.inlaco.crewmgrservice.feature.upload.service.UploadFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
      description =
          """
UploadType:
{
  name: "CANDIDATE_PROFILE",
  nestedTypeAlloweds:{
  }
}
{
  name: "SAILOR_PROFILE",
  nestedTypeAlloweds:{
    SOCIAL_INSURANCE,
    ACCIDENT_INSURANCE
  }
}
""",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("")
  @RolesAllowed("USER")
  public UploadOptions getUploadOptions(
      @RequestBody UploadType type, @RequestParam(required = false) String id) {
    return uploadFactory.getUploadOptions(type, id);
  }

  @Operation(
      summary = "This endpoint is used to upload a file",
      description = "Upload a file",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("")
  public void uploadFile(@RequestBody UploadRequest request) {
    uploadFactory.uploadFile(request.getType(), request.getTokens());
  }
}
