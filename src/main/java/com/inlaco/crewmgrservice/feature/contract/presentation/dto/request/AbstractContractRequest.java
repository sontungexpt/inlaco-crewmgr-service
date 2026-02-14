package com.inlaco.crewmgrservice.feature.contract.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.Party;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@JsonTypeInfo(
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    visible = true,
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
  @Type(value = DynamicContractRequest.class, name = ContractType.Fields.DYNAMIC_CONTRACT),
  @Type(value = LaborContractRequest.class, name = ContractType.Fields.LABOR_CONTRACT),
  @Type(value = CrewSupplyContractRequest.class, name = ContractType.Fields.SUPPLY_CONTRACT)
})
@Getter
@Setter
public abstract class AbstractContractRequest implements TimeFrame, Serializable {

  private final ContractType type;

  public AbstractContractRequest(ContractType type) {
    this.type = type;
  }

  @NotBlank private String title;

  @NotNull private Party initiator;

  @Size(min = 1)
  private List<@Valid Party> partners;

  private File contractFile;

  private List<@Valid File> attachments;

  private List<@NotBlank String> terms;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant activationDate;

  @Future
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant expiredDate;

  @Min(0)
  private int contractFreezeDelay = 5;

  @Override
  public List<Pair> getTimeFrames() {
    return List.of(Pair.of(activationDate, expiredDate));
  }
}
