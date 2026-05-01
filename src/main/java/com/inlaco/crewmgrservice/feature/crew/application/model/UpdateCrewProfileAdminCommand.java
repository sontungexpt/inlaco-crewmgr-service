package com.inlaco.crewmgrservice.feature.crew.application.model;

import com.inlaco.crewmgrservice.shared.application.model.Patch;
import com.inlaco.crewmgrservice.shared.objectvalue.Gender;
import java.time.Instant;
import lombok.Data;

@Data
public class UpdateCrewProfileAdminCommand implements UpdateCrewProfileCommand {

  Patch<String> fullName;

  Patch<String> email;

  Patch<String> phoneNumber;

  Patch<String> address;

  Patch<Gender> gender;

  Patch<String> professionalPosition;

  Patch<Instant> birthDate;

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
