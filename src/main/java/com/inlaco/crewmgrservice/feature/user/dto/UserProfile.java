package com.inlaco.crewmgrservice.feature.user.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfile {

  private String name;

  private String avatar;

  private List<String> roles;
}
