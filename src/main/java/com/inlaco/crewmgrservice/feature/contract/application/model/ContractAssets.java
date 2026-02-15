package com.inlaco.crewmgrservice.feature.contract.application.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContractAssets {
  private final String contractFile;
  private final List<String> attachments;
}
