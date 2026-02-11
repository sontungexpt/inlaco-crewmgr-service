package com.inlaco.crewmgrservice.feature.recruitment.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.recruitment.application.port.in.RecruitmentReviewUseCase;
import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/applications")
@RequiredArgsConstructor
@Tag(name = "Application - Admin Review")
@RolesAllowed("ADMIN")
public class ApplicationAdminReviewController {

  private final RecruitmentReviewUseCase reviewUseCase;

  @PostMapping("/{id}/review")
  public void reviewApplication(@PathVariable String id, @RequestParam ApplicationStatus status) {
    reviewUseCase.reviewApplication(id, status);
  }
}
