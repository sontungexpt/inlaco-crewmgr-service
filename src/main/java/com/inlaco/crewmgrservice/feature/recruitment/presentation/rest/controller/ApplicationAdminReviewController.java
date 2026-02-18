package com.inlaco.crewmgrservice.feature.recruitment.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.recruitment.application.port.in.RecruitmentReviewUseCase;
import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "Application - Admin Review")
@RolesAllowed("ADMIN")
@RequestMapping("/api/v1/admin/applications")
@RestController
public class ApplicationAdminReviewController {

  private final RecruitmentReviewUseCase reviewUseCase;

  @PostMapping("/{id}/review")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void reviewApplication(
      @PathVariable("id") String id, @RequestParam ApplicationStatus status) {
    if (status == ApplicationStatus.HIRED
        || status == ApplicationStatus.CONTRACT_SIGNED
        || status == ApplicationStatus.CONTRACT_PENDING_SIGNATURE) {
      throw new IllegalArgumentException("Invalid status: " + status);
    }
    reviewUseCase.reviewApplication(id, status);
  }
}
