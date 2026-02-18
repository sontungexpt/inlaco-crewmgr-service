package com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.create;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.DynamicAttribute;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.party.PartyDTO;
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
  @Type(value = NewLaborContract.class, name = ContractType.Fields.LABOR_CONTRACT),
  @Type(value = NewCrewSupplyContract.class, name = ContractType.Fields.SUPPLY_CONTRACT)
})
@Getter
@Setter
public abstract class NewContract implements TimeFrame, Serializable {

  private final ContractType type;

  public NewContract(ContractType type) {
    this.type = type;
  }

  @NotBlank private String title;

  @NotNull private PartyDTO initiator;

  @Size(min = 1)
  private List<@Valid PartyDTO> partners;

  @NotBlank private String contractFile;
  private List<String> attachments;
  private List<DynamicAttribute> customAttributes;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant activationDate;

  @Future
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant expiredDate;

  @Min(0)
  private int contractFreezeDelayMinutes = 5;

  @Override
  public List<Range> getTimeFrames() {
    return List.of(Range.of(activationDate, expiredDate));
  }
}
