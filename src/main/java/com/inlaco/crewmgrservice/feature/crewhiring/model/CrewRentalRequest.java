package com.inlaco.crewmgrservice.feature.crewhiring.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.common.model.ShipInfo;
import com.inlaco.crewmgrservice.feature.crewhiring.enums.CrewRentalRequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@Document("crew_rental_requests")
@Schema(description = "Model representing a request for crew rental services.")
@JsonIgnoreProperties(
    value = {"id", "contractId", "status"},
    allowGetters = true)
public class CrewRentalRequest {

  @Id
  @Schema(hidden = true)
  private String id;

  @Schema(description = "Total number of crew members needed.", example = "10", required = true)
  @NotNull
  @Min(1)
  private Integer totalCrewNeeded;

  @Schema(
      description = "File containing detailed positions and required crew counts.",
      example =
          "{ \"fileName\": \"crew_details.pdf\", \"url\":"
              + " \"https://example.com/files/crew_details.pdf\" }")
  @NotNull
  private File positionDetail;

  // Company information
  @Schema(
      description = "Name of the company requesting the service.",
      example = "Global Shipping Ltd.",
      required = true)
  @NotBlank
  private String companyName;

  @Schema(
      description = "Address of the company.",
      example = "123 Maritime Ave, Port City",
      required = true)
  @NotBlank
  private String companyAddress;

  @Schema(description = "Phone number of the company.", example = "+1234567890", required = true)
  @NotBlank
  private String companyPhone;

  @Schema(
      description = "Email address of the company.",
      example = "contact@shippingcompany.com",
      required = true)
  @NotBlank
  private String companyEmail;

  @Schema(
      description = "Name of the company's representative.",
      example = "John Doe",
      required = true)
  @NotBlank
  private String representativeName;

  @Schema(
      description = "Title of the company's representative.",
      example = "Operations Manager",
      required = true)
  @NotBlank
  private String representativeTitle;

  // Planned schedule information
  @Schema(
      description = "Estimated departure time.",
      example = "2025-01-14T10:00:00Z",
      required = true)
  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Future
  private Instant estimatedDepartureTime;

  @Schema(
      description = "Estimated arrival time.",
      example = "2025-01-20T18:00:00Z",
      required = true)
  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Future
  private Instant estimatedArrivalTime;

  @Schema(description = "Departure point.", example = "Port of Los Angeles", required = true)
  @NotBlank
  private String departurePoint;

  @Schema(description = "Arrival point.", example = "Port of Tokyo", required = true)
  @NotBlank
  private String arrivalPoint;

  @Schema(description = "UN/LOCODE for the departure point.", example = "USLAX", required = true)
  @NotBlank
  private String departureUNLOCODE;

  @Schema(description = "UN/LOCODE for the arrival point.", example = "JPTYO", required = true)
  @NotBlank
  private String arrivalUNLOCODE;

  @Schema(description = "Ship information.")
  private ShipInfo shipInfo;

  @Schema(hidden = true)
  private ObjectId contractId;

  public boolean hasContract() {
    return contractId != null;
  }

  @Schema(description = "The status of the request.")
  @NotNull
  @Default
  private CrewRentalRequestStatus status = CrewRentalRequestStatus.PENDING;

  @CreatedBy
  @Schema(hidden = true)
  @JsonIgnore
  private ObjectId createdBy;

  @JsonGetter("createdBy")
  public String getCreatedByAccount() {
    return createdBy.toHexString();
  }

  @CreatedDate
  @Schema(hidden = true)
  @JsonIgnore
  private Instant createdAt;

  @JsonIgnore
  @LastModifiedBy
  @Schema(description = "The user who last updated the request.", hidden = true)
  private ObjectId updatedBy;

  @Schema(description = "The time when the request was last updated.", hidden = true)
  @JsonIgnore
  @LastModifiedDate
  private Instant updatedAt;

  @Schema(description = "The user who reviewed the request.", hidden = true)
  @JsonIgnore
  private ObjectId reviewedBy;
}
