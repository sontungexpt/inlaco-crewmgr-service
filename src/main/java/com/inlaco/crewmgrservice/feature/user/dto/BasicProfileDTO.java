package com.inlaco.crewmgrservice.feature.user.dto;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.user.model.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class BasicProfileDTO {

  @Schema(hidden = true)
  protected String id;

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

  @Schema(
      description = "The imporant file of the person",
      requiredMode = RequiredMode.REQUIRED,
      type = "File")
  protected File file;

  @Schema(
      description = "The gender of the person",
      requiredMode = RequiredMode.REQUIRED,
      type = "String")
  protected Gender gender;

  @Schema(
      description = "The user account id",
      example = "60f1b3b3b3b3b3b3b3b3b3b3",
      hidden = true,
      requiredMode = RequiredMode.REQUIRED,
      type = "String")
  protected ObjectId accountId;

  @Schema(
      description = "The birth date of the person",
      example = "2000-01-01T00:00:00Z",
      requiredMode = RequiredMode.REQUIRED,
      type = "String")
  protected Instant birthDate;
}
