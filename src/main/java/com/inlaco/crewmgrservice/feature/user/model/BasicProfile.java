package com.inlaco.crewmgrservice.feature.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.patch.JsonPatchIgnore;
import com.inlaco.crewmgrservice.infrastructure.web.validation.phone.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Getter
@Setter
@JsonIgnoreProperties(
    value = {"id", "accountId"},
    allowGetters = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BasicProfile implements Serializable {

  @Id
  @Null
  @JsonPatchIgnore
  @Schema(hidden = true)
  protected String id;

  @Schema(
      description = "The user account id",
      example = "60f1b3b3b3b3b3b3b3b3b3b3",
      hidden = true,
      requiredMode = RequiredMode.REQUIRED,
      type = "String")
  @Indexed(unique = true)
  @JsonIgnore
  @JsonPatchIgnore
  protected ObjectId accountId;

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
  @Email
  @NotBlank
  protected String email;

  @Schema(
      description = "The phone number of the person",
      requiredMode = RequiredMode.REQUIRED,
      example = "+84392211343",
      type = "String")
  @PhoneNumber
  protected String phoneNumber;

  @Schema(
      description = "The address of the person",
      example = "123 Street, City, Country",
      requiredMode = RequiredMode.REQUIRED,
      type = "String")
  protected String address;

  @Schema(
      description = "The gender of the person",
      example = "MALE",
      enumAsRef = true,
      requiredMode = RequiredMode.REQUIRED,
      type = "enum")
  @NotNull
  protected Gender gender;
}
