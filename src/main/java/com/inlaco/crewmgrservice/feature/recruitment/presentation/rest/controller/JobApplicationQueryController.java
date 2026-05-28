package com.inlaco.crewmgrservice.feature.recruitment.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.recruitment.application.model.JobApplicationSearchCriteria;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.in.JobApplicationQueryUseCase;
import com.inlaco.crewmgrservice.feature.recruitment.presentation.dto.response.JobApplicationResponse;
import com.inlaco.crewmgrservice.feature.recruitment.presentation.mapper.JobApplicationMapper;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.Filter;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.PageableQueryParams;
import com.inlaco.crewmgrservice.infrastructure.web.validation.annotation.ObjectId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
@Tag(name = "Application - Admin Query")
public class JobApplicationQueryController {

  private final JobApplicationQueryUseCase queryUseCase;
  private final JobApplicationMapper jobApplicationMapper;

  @Operation(
      summary = "Retrieve all applications profiles",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("")
  @PageableQueryParams
  @RolesAllowed("ADMIN")
  public Page<JobApplicationResponse> getAllApplications(
      @Filter JobApplicationSearchCriteria criteria,
      @PageableDefault(size = 10, page = 0) Pageable pageable) {
    return queryUseCase
        .getAllApplications(criteria, pageable)
        .map(jobApplicationMapper::toJobApplicationResponse);
  }

  @Operation(
      summary = "Retrieve my applications profiles",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/mine")
  @PageableQueryParams
  @RolesAllowed("USER")
  public Page<JobApplicationResponse> getMyApplications(
      @CurrentUser User user, @Filter JobApplicationSearchCriteria criteria, Pageable pageable) {
    if (criteria == null) {
      criteria = new JobApplicationSearchCriteria();
    }
    criteria.setAccountId(user.getId());
    return queryUseCase
        .getAllApplications(criteria, pageable)
        .map(jobApplicationMapper::toJobApplicationResponse);
  }

  @Operation(summary = "Retrieve a detail candidate profile from the server by id")
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @RolesAllowed("USER")
  public JobApplicationResponse getApplicationDetail(@ObjectId @PathVariable("id") String id) {
    return jobApplicationMapper.toJobApplicationResponse(queryUseCase.getApplicationDetail(id));
  }
}
