package com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Valid
@com.inlaco.crewmgrservice.infrastructure.web.validation.timeframe.TimeFrame
public interface TimeFrame {

  @Getter
  @AllArgsConstructor
  @Builder
  public static class Range {
    private final Instant start;
    private final Instant end;
    private final boolean requiredStart;
    private final boolean requiredEnd;
    private final boolean requiredBothIfEitherPresent;

    public Range(Instant start, Instant end) {
      this(start, end, true, false, false);
    }

    public static Range of(Instant start, Instant end) {
      return new Range(start, end);
    }

    public static Range of(
        Instant start, Instant end, boolean requiredFirst, boolean requiredSecond) {
      return new Range(start, end, requiredFirst, requiredSecond, false);
    }

    public static Range of(Instant start, Instant end, boolean requiredBothIfEitherPresent) {
      return new Range(start, end, false, false, true);
    }
  }

  @JsonSerialize(using = NullSerializer.class)
  List<Range> getTimeFrames();
}
