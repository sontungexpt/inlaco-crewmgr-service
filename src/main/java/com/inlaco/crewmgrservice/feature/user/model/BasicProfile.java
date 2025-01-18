package com.inlaco.crewmgrservice.feature.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import com.inlaco.crewmgrservice.validation.annotation.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Past;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(
    value = {"id", "accountId"},
    allowGetters = true)
@SuperBuilder
@NoArgsConstructor
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
  @JsonPatchIgnore
  @Indexed(unique = true)
  @JsonSerialize(using = ToStringSerializer.class)
  protected ObjectId accountId;

  @Schema(
      description = "The birth date of the person",
      example = "2000-01-01T00:00:00Z",
      requiredMode = RequiredMode.REQUIRED,
      type = "String")
  @Past
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Instant birthDate;

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
