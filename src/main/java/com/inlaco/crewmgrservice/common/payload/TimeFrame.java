package com.inlaco.crewmgrservice.common.payload;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import org.springframework.data.util.Pair;

@Valid
@com.inlaco.crewmgrservice.validation.annotation.TimeFrame
public interface TimeFrame {

  List<Pair<Instant, Instant>> getTimeFrames();
}
