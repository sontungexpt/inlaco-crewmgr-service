package com.inlaco.crewmgrservice.feature.user.controller;

import com.inlaco.crewmgrservice.annotation.BearerToken;
import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.endpoint.APIEndpointMap;
import com.inlaco.crewmgrservice.endpoint.APIEndpointName;
import com.inlaco.crewmgrservice.feature.auth.dto.JwtResponse;
import com.inlaco.crewmgrservice.feature.auth.dto.NewPasswordRequest;
import com.inlaco.crewmgrservice.feature.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
      security = @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME),
      description =
          """
Change the password for an account
**Required**:
- Refresh token is passed in the header as a Bearer token

**Usecase**:
- UC_account-doi-mat-khau

**Note**
- The new password must be different from the old password

""")
  @PostMapping("/new-password")
  @APIEndpointMap(
      name = APIEndpointName.USER_CHANGE_PASSWORD,
      displayName = "Change password",
      description = "Change the password for an account")
  public JwtResponse changePassword(
      @BearerToken String refreshToken, @Valid @RequestBody NewPasswordRequest newPasswordRequest) {
    return userService.changePassword(refreshToken, newPasswordRequest);
  }
}
