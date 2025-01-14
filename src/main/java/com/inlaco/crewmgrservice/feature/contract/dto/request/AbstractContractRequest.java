package com.inlaco.crewmgrservice.feature.contract.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.common.payload.TimeFrame;
import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.model.ContractType;
import com.inlaco.crewmgrservice.feature.contract.model.PaperContract;
import com.inlaco.crewmgrservice.feature.contract.model.Party;
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
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.checkerframework.common.value.qual.MinLen;
import org.springframework.format.annotation.DateTimeFormat;

@SuperBuilder
@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class AbstractContractRequest implements Contract, TimeFrame {

  public abstract Contract toModel();

  @NotBlank
  @Schema(
      description = "The title of the contract",
      example = "The title of the contract",
      requiredMode = RequiredMode.REQUIRED)
  protected String title;

  @MinLen(2)
  protected List<Party> parties;

  @Schema(
      description = "The list of term of the contract",
      example = "The term 1 of the contract",
      requiredMode = RequiredMode.REQUIRED)
  @NotEmpty
  protected List<@NotBlank String> terms;

  @DateTimeFormat
  @FutureOrPresent
  @Schema(description = "The time that the contract is valid, and active")
  protected Instant activationDate;

  @Schema(description = "The time that the contract expired")
  @Future
  @DateTimeFormat
  protected Instant expiredDate;

  @Schema(
      description = "The template id of the contract",
      type = "String",
      requiredMode = RequiredMode.REQUIRED)
  protected ObjectId templateId;

  @Schema(
      description =
          "The activation grace period in minutes after the contract is created. In this time, the"
              + " contract is not really active and the user can update it easily. After this"
              + " period, the contract is activated and the user can't update it anymore. If they"
              + " want to update the contract it will save the older version (default: 10 minutes)",
      example = "10")
  @Min(0)
  @Default
  protected int contractFreezeDelay = 10;

  @NotNull
  @Schema(
      description = "The type of the contract",
      enumAsRef = true,
      requiredMode = RequiredMode.REQUIRED)
  protected ContractType type;

  @Override
  @JsonIgnore
  public List<Pair> getTimeFrames() {
    return List.of(Pair.of(activationDate, expiredDate));
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public List<Party> getParties() {
    return parties;
  }

  @Override
  public List<PaperContract> getPaperContracts() {
    return null;
  }

  @Override
  public List<File> getAttachments() {
    return null;
  }

  @Override
  public List<String> getTerms() {
    return terms;
  }

  @Override
  public Instant getActivationDate() {
    return activationDate;
  }

  @Override
  public Instant getExpiredDate() {
    return expiredDate;
  }

  @Override
  public boolean isSigned() {
    return false;
  }

  @Override
  public ContractType getType() {
    return type;
  }
}
