package com.inlaco.crewmgrservice.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "Detailed information about the ship")
public class ShipInfo implements Serializable {

  @Schema(
      description = "IMO number of the ship (International Maritime Organization)",
      example = "9811000",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String IMONumber;

  @Schema(
      description = "Registration number of the ship",
      example = "REG-2024-001",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String registrationNumber;

  @Schema(
      description = "Ship's country of registration (ISO 3166-1 alpha-2 code).",
      example = "US",
      required = true)
  private String countryISO;

  @Schema(
      description = "Name of the ship",
      example = "Ever Given",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String name;

  @Schema(description = "Optional description of the ship", example = "Large container ship")
  private String description;

  @Schema(
      description = "URL to an image of the ship",
      example = "https://example.com/images/ship.jpg")
  private File imageUrl;

  @Schema(description = "Type of the ship")
  private String shipType;
}
