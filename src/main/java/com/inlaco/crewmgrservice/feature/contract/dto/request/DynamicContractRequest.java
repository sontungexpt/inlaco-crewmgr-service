package com.inlaco.crewmgrservice.feature.contract.dto.request;

import com.inlaco.crewmgrservice.common.model.DynamicAttribute;
import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.model.DynamicContract;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class DynamicContractRequest extends AbstractContractRequest {

  @Schema(
      description = "The list of custom attributes of the contract",
      example = "[{\"name\":\"custom1\", \"value\": \"value1\"}]")
  @Default
  private List<DynamicAttribute> customAttributes = new ArrayList<>();

  @Override
  public Contract toModel() {
    return DynamicContract.builder()
        .title(this.title)
        .parties(this.parties)
        .terms(this.terms)
        .activationDate(this.activationDate)
        .expiredDate(this.expiredDate)
        .templateId(this.templateId)
        .contractFreezeDelay(this.contractFreezeDelay)
        .type(this.type)
        .customAttributes(this.customAttributes)
        .build();
  }
}
