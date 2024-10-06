package com.inlaco.crewmgrservice.feature.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.inlaco.crewmgrservice.validation.annotation.OptimizedName;
import com.inlaco.crewmgrservice.validation.annotation.Password;
import com.inlaco.crewmgrservice.validation.annotation.PhoneNumber;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

  @PhoneNumber
  @JsonAlias("username")
  private String phoneNumber;

  @Password private String password;

  @OptimizedName private String name;

  @Email private String email;
}
