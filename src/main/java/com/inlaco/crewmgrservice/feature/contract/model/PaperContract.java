package com.inlaco.crewmgrservice.feature.contract.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PaperContract {
  private int page;
  private String imageUrl;
}
