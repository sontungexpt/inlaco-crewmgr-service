package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.common.payload.TimeFrame;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
// import org.checkerframework.common.value.qual.MinLen;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@JsonIgnoreProperties(
    value = {"id", "version", "partyAccountIds", "createdAt", "updatedAt", "prevVersion", "signed"},
    allowGetters = true)
@Document("contracts")
@SuperBuilder
@JsonTypeInfo(
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    visible = true,
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = DynamicContract.class, name = ContractType.Fields.DYNAMIC_CONTRACT),
  @JsonSubTypes.Type(value = LaborContract.class, name = ContractType.Fields.LABOR_CONTRACT),
  @JsonSubTypes.Type(value = SupplyContract.class, name = ContractType.Fields.SUPPLY_CONTRACT)
})
public abstract class AbstractContract extends ContractVersion
    implements Contract, TimeFrame, Serializable {

  public AbstractContract(ContractType type) {
    super();
    this.type = type;
  }

  @NotBlank
  @Schema(
      description = "The title of the contract",
      example = "The title of the contract",
      requiredMode = RequiredMode.REQUIRED)
  private String title;

  @Schema(
      description = "The initiator of the contract (our company)",
      requiredMode = RequiredMode.REQUIRED)
  @NotNull
  private Party initiator;

  @Schema(
      description = "The list of signed partners (example sailor)",
      requiredMode = RequiredMode.REQUIRED)
  @Size(min = 1)
  private List<@Valid Party> partners;

  @Schema(description = "The list of paper contracts", requiredMode = RequiredMode.REQUIRED)
  private File contractFile;

  @Schema(
      description = "The description of the contract",
      example = "[{\"name\":\"Hop dong\", \"url\": \"https://....\"}]")
  private List<@Valid File> attachments = new ArrayList<>();

  @Schema(description = "The list of term of the contract", requiredMode = RequiredMode.REQUIRED)
  private List<@NotBlank String> terms;

  @JsonPatchIgnore
  @Schema(description = "The contract is signed or not", hidden = true)
  private boolean signed = false;

  @JsonPatchIgnore
  @JsonIgnore
  @Schema(description = "The user who signed the contract", hidden = true)
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId signedBy;

  public void sign(ObjectId signedBy) {
    this.signed = true;
    this.signedBy = signedBy;
    this.signedAt = Instant.now();
  }

  @FutureOrPresent
  @Schema(description = "The time that the contract is signed")
  @JsonIgnore
  @JsonPatchIgnore
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant signedAt;

  @Schema(description = "The time that the contract is valid, and active")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant activationDate;

  @JsonIgnore
  @JsonPatchIgnore
  @Schema(
      description =
          "The contract is activated or not(Only use for timertask to checked if the contract"
              + " should be check)",
      hidden = true)
  private boolean activated = false;

  @Schema(description = "The time that the contract expired")
  @Future
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant expiredDate;

  @Schema(
      description = "The template id of the contract",
      type = "String",
      hidden = true,
      requiredMode = RequiredMode.REQUIRED)
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId templateId;

  @Schema(
      description =
          "The activation grace period in minutes after the contract is created. In this time, the"
              + " contract is not really active and the user can update it easily. After this"
              + " period, the contract is activated and the user can't update it anymore. If they"
              + " want to update the contract it will save the older version (default: 5 minutes)",
      example = "5")
  @Min(0)
  @Default
  @JsonPatchIgnore
  private int contractFreezeDelay = 5;

  @Schema(hidden = true)
  public boolean isFreezed() {
    return isSigned() && Instant.now().isAfter(getFreezeDate());
  }

  @Schema(hidden = true)
  public Instant getFreezeDate() {
    return activationDate == null ? null : activationDate.plusSeconds(contractFreezeDelay * 60);
  }

  @Schema(
      description = "The type of the contract",
      enumAsRef = true,
      requiredMode = RequiredMode.REQUIRED)
  @NotNull
  @JsonPatchIgnore
  private ContractType type;

  @Override
  @JsonIgnore
  public List<Pair> getTimeFrames() {
    return List.of(Pair.of(activationDate, expiredDate));
  }
}
