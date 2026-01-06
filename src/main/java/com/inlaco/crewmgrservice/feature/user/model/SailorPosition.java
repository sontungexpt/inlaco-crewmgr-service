package com.inlaco.crewmgrservice.feature.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "sailor_positions")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(
    value = {"id"},
    allowGetters = true)
public class SailorPosition implements Serializable {

  @Id
  @Schema(hidden = true)
  private String id;

  @Schema(
      description = "The name of the sailor position",
      example = "Captain",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String name;

  @NotBlank
  @Schema(
      description = "The description of the sailor position",
      example = "The captain of the ship.",
      requiredMode = RequiredMode.REQUIRED)
  private String description;
}
