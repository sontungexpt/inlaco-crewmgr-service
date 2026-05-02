package com.inlaco.crewmgrservice.feature.crew.application.model;

import com.inlaco.crewmgrservice.shared.application.model.Patch;
import lombok.Data;

@Data
public class UpdateCrewProfileCrewCommand {

  Patch<String> email;
  Patch<String> phoneNumber;
  Patch<String> address;

  Patch<String> image;

  Patch<String> citizenIdentityCardId;
  Patch<String> citizenIdentityCardImageFront;
  Patch<String> citizenIdentityCardImageBack;

  Patch<String> socialInsuranceCode;
  Patch<String> socialInsuranceImageFront;
  Patch<String> socialInsuranceImageBack;

  Patch<String> accidentInsuranceCode;
  Patch<String> accidentInsuranceImageFront;
  Patch<String> accidentInsuranceImageBack;
}
