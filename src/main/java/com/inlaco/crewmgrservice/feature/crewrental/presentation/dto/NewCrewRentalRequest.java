package com.inlaco.crewmgrservice.feature.crewrental.presentation.dto;

import com.inlaco.crewmgrservice.feature.crewrental.domain.enums.CrewRentalRequestStatus;
import com.inlaco.crewmgrservice.infrastructure.web.validation.phone.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class NewCrewRentalRequest {

  private String detailFile;

  // Company information
  @NotBlank private String companyName;

  @NotBlank private String companyAddress;

  @NotBlank @PhoneNumber private String companyPhone;

  @Email @NotBlank private String companyEmail;

  @NotBlank private String companyRepresentor;

  @NotBlank private String companyRepresentorPosition;

  // Planned schedule information
  @NotNull
  @Future
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant rentalStartDate;

  @NotNull
  @Future
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant rentalEndDate;

  @NotNull private ShipInfoRequest shipInfo;

  @NotNull private CrewRentalRequestStatus status = CrewRentalRequestStatus.PENDING;
}
