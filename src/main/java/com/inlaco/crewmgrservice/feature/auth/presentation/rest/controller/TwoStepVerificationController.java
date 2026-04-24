package com.inlaco.crewmgrservice.feature.auth.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.auth.application.port.in.TwoStepVerificationUseCase;
import com.inlaco.crewmgrservice.feature.user.application.port.in.UserUseCase;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.PublicEndpoint;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Two Step Verification")
@RestController
@RequestMapping("/api/v1/auth/two-step-verification")
@PublicEndpoint
@RequiredArgsConstructor
@Slf4j
public class TwoStepVerificationController {

  private final TwoStepVerificationUseCase twoStepVerificationUseCase;
  private final UserUseCase userUseCase;

  @Value("${inlaco.client.endpoint.login}")
  private String LOGIN_CLIENT_URL;

  @Operation(summary = "Verify the two step verification")
  @GetMapping("")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void verifyTwoStepVerification(
      HttpServletResponse response,
      @RequestParam("token") String token,
      @RequestHeader(value = "User-Agent", required = false) String userAgent)
      throws IOException {
    twoStepVerificationUseCase.verify(token);
    response.sendRedirect(LOGIN_CLIENT_URL);
    if (userAgent != null && userAgent.contains("Mobile")) {
      response.sendRedirect("myapp://verify-success");
    } else {
      response.sendRedirect(LOGIN_CLIENT_URL);
    }
  }

  @Operation(summary = "Resend the two step verification")
  @PostMapping("/resend")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void resendTwoStepVerification(@RequestParam("username") String username) {
    User user = userUseCase.findByUsername(username);
    twoStepVerificationUseCase.resend(user);
  }
}
