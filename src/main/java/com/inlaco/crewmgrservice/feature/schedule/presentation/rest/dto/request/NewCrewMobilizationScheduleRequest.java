package com.inlaco.crewmgrservice.feature.schedule.presentation.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inlaco.crewmgrservice.feature.schedule.domain.enums.CrewMobilizationScheduleStatus;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame.Range;
import com.inlaco.crewmgrservice.infrastructure.web.validation.phone.PhoneNumber;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;

public record NewCrewMobilizationScheduleRequest(
    @NotBlank String partnerName,
    @NotBlank @PhoneNumber String partnerPhone,
    @Email @NotBlank String partnerEmail,
    @NotBlank String partnerAddress,
    @NotNull ShipInfo shipInfo,
    @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @FutureOrPresent Instant startDate,
    @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Future Instant endDate,
    CrewMobilizationScheduleStatus status,
    @Size(min = 1) @JsonAlias({"crewMembers", "crews"}) Set<@Valid AssignedCrewRequest> crews)
    implements TimeFrame {

  @Override
  @JsonIgnore
  public List<Range> getTimeFrames() {
    return List.of(Range.of(startDate, endDate));
  }
}
