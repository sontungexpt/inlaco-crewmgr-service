package com.inlaco.crewmgrservice.feature.crewrental.presentation.dto;

import com.inlaco.crewmgrservice.feature.crewrental.domain.enums.CrewRentalRequestStatus;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame;
import com.inlaco.crewmgrservice.infrastructure.web.validation.phone.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class NewCrewRentalRequest implements TimeFrame {

  @NotBlank private String detailFile;

  // Company information
  @NotBlank private String companyName;

  @NotBlank private String companyAddress;

  @NotBlank @PhoneNumber private String companyPhone;

  @Email @NotBlank private String companyEmail;

  @NotBlank private String companyRepresentor;

  @NotBlank private String companyRepresentorPosition;

  // Planned schedule information
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant rentalStartDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant rentalEndDate;

  @NotNull private ShipInfoRequest shipInfo;

  @NotNull private CrewRentalRequestStatus status = CrewRentalRequestStatus.PENDING;

  @Override
  public List<Range> getTimeFrames() {
    return Collections.singletonList(Range.bothRequired(rentalStartDate, rentalEndDate));
  }
}
