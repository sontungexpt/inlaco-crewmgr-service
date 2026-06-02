package com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.update;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.party.PartyDTO;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame;
import com.inlaco.crewmgrservice.shared.application.model.Patch;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
    /* visible = true */ )
@JsonSubTypes({
  @Type(value = LaborContractPatchRequest.class, name = ContractType.Fields.LABOR_CONTRACT),
  @Type(value = CrewSupplyContractPatchRequest.class, name = ContractType.Fields.SUPPLY_CONTRACT)
})
public class ContractPatchRequest implements TimeFrame, Serializable {
  Patch<@NotBlank String> title = Patch.unchanged();
  Patch<Instant> activationDate = Patch.unchanged();
  Patch<Instant> expiredDate = Patch.unchanged();
  Patch<@Min(0) Integer> contractFreezeDelayMinutes = Patch.unchanged();
  Patch<@NotNull PartyDTO> initiator = Patch.unchanged();
  Patch<@Size(min = 1) List<PartyDTO>> partners = Patch.unchanged();
  String contractFile;
  List<String> attachments;

  @Override
  public List<Range> getTimeFrames() {

    Instant start = null;
    Instant end = null;

    if (activationDate instanceof Patch.Updated<Instant> updated) {
      start = updated.value();
    }

    if (expiredDate instanceof Patch.Updated<Instant> updated) {
      end = updated.value();
    }

    if (start == null && end == null) {
      return List.of();
    }

    return List.of(Range.bothRequiredIfEitherPresent(start, end));
  }
}
