package com.inlaco.crewmgrservice.feature.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BasicProfileDTO {

  @Schema(hidden = true)
  protected String id;

  @Schema(
      description = "The user account id",
      example = "60f1b3b3b3b3b3b3b3b3b3b3",
      requiredMode = RequiredMode.REQUIRED,
      type = "String")
  protected String accountId;

  @Schema(
      description = "The full name of the person",
      example = "John Doe",
      requiredMode = RequiredMode.REQUIRED,
      type = "String")
  protected String fullName;

  @Schema(
      description = "The email of the person",
      example = "email@gmail.com",
      requiredMode = RequiredMode.REQUIRED,
      type = "String")
  protected String email;

  @Schema(
      description = "The phone number of the person",
      requiredMode = RequiredMode.REQUIRED,
      example = "+84392211343",
      type = "String")
  protected String phoneNumber;

  @Schema(
      description = "The address of the person",
      example = "123 Street, City, Country",
      requiredMode = RequiredMode.REQUIRED,
      type = "String")
  protected String address;
}
