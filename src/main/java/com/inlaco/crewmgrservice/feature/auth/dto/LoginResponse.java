package com.inlaco.crewmgrservice.feature.auth.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
  private String name;

  private String avatar;

  private List<String> roles;

  private JwtResponse jwt;
}
