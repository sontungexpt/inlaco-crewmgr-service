package com.inlaco.crewmgrservice.feature.contract.application.model;

import java.util.List;
import lombok.Getter;

@Getter
public class CrewSupplyContractAssets extends ContractAssets {
  private final String shipInfoImage;

  public CrewSupplyContractAssets(
      String contractFile, List<String> attachments, String shipInfoImage) {
    super(contractFile, attachments);
    this.shipInfoImage = shipInfoImage;
  }
}
