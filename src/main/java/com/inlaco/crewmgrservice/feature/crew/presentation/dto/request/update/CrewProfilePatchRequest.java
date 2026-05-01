package com.inlaco.crewmgrservice.feature.crew.presentation.dto.request.update;

import com.inlaco.crewmgrservice.infrastructure.web.validation.phone.PhoneNumber;
import com.inlaco.crewmgrservice.shared.application.model.Patch;
import com.inlaco.crewmgrservice.shared.objectvalue.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.Instant;
import lombok.Data;

@Data
public class CrewProfilePatchRequest {
  Patch<@NotBlank String> fullName = Patch.unchanged();
  Patch<@Email String> email = Patch.unchanged();
  Patch<@PhoneNumber String> phoneNumber = Patch.unchanged();

  Patch<@NotBlank String> address = Patch.unchanged();
  Patch<@NotNull Gender> gender = Patch.unchanged();

  Patch<@NotBlank String> professionalPosition = Patch.unchanged();

  Patch<@Past Instant> birthDate = Patch.unchanged();

  // CCCD
  Patch<@NotBlank String> citizenIdentityCardId = Patch.unchanged();
  Patch<@NotBlank String> citizenIdentityCardImageFront = Patch.unchanged();
  Patch<@NotBlank String> citizenIdentityCardImageBack = Patch.unchanged();

  Patch<@NotBlank String> socialInsuranceCode = Patch.unchanged();
  Patch<@NotBlank String> socialInsuranceImageFront = Patch.unchanged();
  Patch<@NotBlank String> socialInsuranceImageBack = Patch.unchanged();

  Patch<@NotBlank String> accidentInsuranceCode = Patch.unchanged();
  Patch<@NotBlank String> accidentInsuranceImageFront = Patch.unchanged();
  Patch<@NotBlank String> accidentInsuranceImageBack = Patch.unchanged();
}
