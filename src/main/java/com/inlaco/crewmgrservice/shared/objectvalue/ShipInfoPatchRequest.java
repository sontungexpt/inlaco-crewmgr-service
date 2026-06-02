package com.inlaco.crewmgrservice.shared.objectvalue;

import com.inlaco.crewmgrservice.shared.application.model.Patch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShipInfoPatchRequest {

  private Patch<String> imoNumber;

  private Patch<String> countryISO;

  private Patch<String> name;

  private Patch<String> description;

  private Patch<String> image;

  private Patch<String> type;
}
