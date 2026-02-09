package com.inlaco.crewmgrservice.feature.user.controller;

import com.inlaco.crewmgrservice.endpoint.APIEndpointMap;
import com.inlaco.crewmgrservice.endpoint.APIEndpointName;
import com.inlaco.crewmgrservice.feature.auth.presentation.dto.request.NewPasswordRequest;
import com.inlaco.crewmgrservice.feature.auth.presentation.dto.response.AuthTokenResponse;
import com.inlaco.crewmgrservice.feature.user.dto.UserProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.service.UserService;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.BearerToken;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import com.inlaco.crewmgrservice.utils.ConsoleUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "A collection endpoints of user")
public class UserController {
  private final UserService userService;

  @Operation(
      summary = "Change the password for an account",
      security = @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME))
  @PostMapping("/new-password")
  @APIEndpointMap(
      name = APIEndpointName.USER_CHANGE_PASSWORD,
      displayName = "Change password",
      description = "Change the password for an account")
  public AuthTokenResponse changePassword(
      @BearerToken String refreshToken, @Valid @RequestBody NewPasswordRequest newPasswordRequest) {
    return userService.changePassword(refreshToken, newPasswordRequest);
  }

  @Operation(
      summary = "Get the user profile",
      security = @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME))
  @GetMapping("/me")
  @RolesAllowed("USER")
  public UserProfile getUserProfile(@CurrentUser User currentUser) {
    ConsoleUtils.prettyPrint(currentUser);
    return userService.getUserProfile(currentUser);
  }
}
