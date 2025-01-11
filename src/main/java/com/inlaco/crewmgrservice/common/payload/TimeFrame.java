package com.inlaco.crewmgrservice.common.payload;

import jakarta.validation.Valid;
import java.time.Instant;

@Valid
@com.inlaco.crewmgrservice.validation.annotation.TimeFrame
public interface TimeFrame {

  Instant getStartDate();

  Instant getEndDate();
}
