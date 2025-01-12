package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.common.payload.TimeFrame;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.checkerframework.common.value.qual.MinLen;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.Pair;
import org.springframework.format.annotation.DateTimeFormat;

@SuperBuilder
@Getter
@Setter
@JsonIgnoreProperties(
    value = {"id", "version", "createdAt", "updatedAt", "prevVersion"},
    allowGetters = true)
@Document("contracts")
public abstract class AbstractContract extends ContractVersion implements Contract, TimeFrame {

  @NotBlank
  @Schema(
      description = "The title of the contract",
      example = "The title of the contract",
      requiredMode = RequiredMode.REQUIRED)
  private String title;

  @MinLen(2)
  private List<Party> parties;

  @Schema(
      description = "The list of paper contracts",
      requiredMode = RequiredMode.REQUIRED,
      example = "[{\"page\": 1, \"imageUrl\": \"https://...\"}]")
  private List<PaperContract> paperContracts;

  @Schema(
      description = "The description of the contract",
      example = "{\"name\":\"Hop dong\", \"url\": \"https://....\"}")
  private List<File> attachments;

  @Schema(
      description = "The list of term of the contract",
      example = "The term 1 of the contract",
      requiredMode = RequiredMode.REQUIRED)
  @NotEmpty
  private List<@NotBlank String> terms;

  @DateTimeFormat
  @FutureOrPresent
  @Schema(description = "The time that the contract is valid, and active")
  private Instant activationDate;

  @Schema(description = "The time that the contract expired")
  @Future
  @DateTimeFormat
  private Instant expiredDate;

  @Schema(
      description = "The template id of the contract",
      type = "String",
      requiredMode = RequiredMode.REQUIRED)
  private ObjectId templateId;

  @Schema(
      description =
          "The activation grace period in minutes after the contract is created. In this time, the"
              + " contract is not really active and the user can update it easily. After this"
              + " period, the contract is activated and the user can't update it anymore. If they"
              + " want to update the contract it will save the older version (default: 10 minutes)",
      example = "10")
  @Min(0)
  @Default
  private int contractFreezeDelay = 10;

  @Override
  public boolean isFreeze() {
    return Instant.now().minusSeconds(contractFreezeDelay * 60).isAfter(this.getCreatedAt());
  }

  @NotNull
  @Schema(
      description = "The type of the contract",
      enumAsRef = true,
      requiredMode = RequiredMode.REQUIRED)
  private ContractType type;

  @Override
  @JsonIgnore
  public List<Pair> getTimeFrames() {
    return List.of(Pair.of(activationDate, expiredDate));
  }
}
