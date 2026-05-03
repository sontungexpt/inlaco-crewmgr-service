package com.inlaco.crewmgrservice.feature.recruitment.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.recruitment.application.port.in.JobApplicationCommandUseCase;
import com.inlaco.crewmgrservice.feature.recruitment.presentation.dto.request.NewJobApplication;
import com.inlaco.crewmgrservice.feature.recruitment.presentation.dto.response.JobApplicationResponse;
import com.inlaco.crewmgrservice.feature.recruitment.presentation.mapper.JobApplicationMapper;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/applications")
@Tag(name = "Application")
public class JobApplicationUserController {

  private final JobApplicationCommandUseCase jobApplicationUseCase;
  private final JobApplicationMapper jobApplicationMapper;

  @Operation(
      summary = "Apply for a job",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("/recruitment/{recruitmentPostId}")
  @ResponseStatus(HttpStatus.CREATED)
  @RolesAllowed("USER")
  public JobApplicationResponse apply(
      @ObjectId @PathVariable("recruitmentPostId") String recruitmentPostId,
      @RequestParam String resumeAssetId,
      @CurrentUser User user,
      @RequestBody @Valid NewJobApplication newJobApplication) {
    return jobApplicationMapper.toJobApplicationResponse(
        jobApplicationUseCase.apply(
            recruitmentPostId,
            jobApplicationMapper.toJobApplication(newJobApplication),
            resumeAssetId,
            user));
  }
}
