package com.inlaco.crewmgrservice.feature.auth.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.auth.application.port.in.TwoStepVerificationUseCase;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.PublicEndpoint;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Two Step Verification")
@RestController
@RequestMapping("/api/v1/auth/two-step-verification")
@PublicEndpoint
public record TwoStepVerificationController(TwoStepVerificationUseCase twoStepVerificationUseCase) {

  @Operation(summary = "Verify the two step verification")
  @GetMapping("")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void verifyTwoStepVerification(@RequestParam("token") String token) {
    twoStepVerificationUseCase.verify(token);
  }

  @Operation(summary = "Resend the two step verification")
  @PostMapping("/resend")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void resendTwoStepVerification(@CurrentUser User user) {
    twoStepVerificationUseCase.resend(user);
  }
}
