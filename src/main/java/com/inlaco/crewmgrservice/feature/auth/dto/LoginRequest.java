package com.inlaco.crewmgrservice.feature.auth.dto;

import com.inlaco.crewmgrservice.validation.annotation.Password;
import com.inlaco.crewmgrservice.validation.annotation.PhoneNumber;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
  @PhoneNumber private String phoneNumber;

  @Password private String password;
}
