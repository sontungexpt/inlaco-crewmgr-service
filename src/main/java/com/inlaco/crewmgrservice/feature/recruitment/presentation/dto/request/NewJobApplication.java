package com.inlaco.crewmgrservice.feature.recruitment.presentation.dto.request;

import com.inlaco.crewmgrservice.common.model.Asset;
import com.inlaco.crewmgrservice.feature.user.domain.enums.Gender;
import com.inlaco.crewmgrservice.infrastructure.web.validation.phone.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewJobApplication {

  @NotBlank private String fullName;

  @Email @NotBlank private String email;

  @PhoneNumber private String phoneNumber;

  private String address;
  private Gender gender;
  private String languageSkills;
  private String experiences;

  private Asset resume;
}
