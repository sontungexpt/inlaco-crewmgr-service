package com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.update;

import com.inlaco.crewmgrservice.shared.application.model.Patch;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipInfoPatchRequest {

  private Patch<@NotBlank String> imoNumber = Patch.unchanged();

  private Patch<@NotBlank String> countryISO = Patch.unchanged();

  private Patch<@NotBlank String> name = Patch.unchanged();

  private Patch<String> description = Patch.unchanged();

  private Patch<@NotBlank String> image = Patch.unchanged();

  private Patch<@NotBlank String> type = Patch.unchanged();
}
