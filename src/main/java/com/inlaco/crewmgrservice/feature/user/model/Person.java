package com.inlaco.crewmgrservice.feature.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Person implements Serializable {

  @Id
  @Schema(hidden = true)
  protected String id;

  @Schema(
      description = "The user account id",
      example = "60f1b3b3b3b3b3b3b3b3b3b3",
      requiredMode = RequiredMode.REQUIRED,
      type = "String")
  @com.inlaco.crewmgrservice.validation.annotation.ObjectId
  @CreatedBy
  protected ObjectId userAccountId;

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
