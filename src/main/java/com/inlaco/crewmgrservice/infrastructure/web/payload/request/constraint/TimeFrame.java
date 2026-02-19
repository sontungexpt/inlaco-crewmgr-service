package com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;

@Valid
@com.inlaco.crewmgrservice.infrastructure.web.validation.timeframe.TimeFrame
public interface TimeFrame {

  List<Range> getTimeFrames();

  enum RequirementPolicy {
    NONE,
    START_REQUIRED,
    END_REQUIRED,
    BOTH_REQUIRED,
    BOTH_REQUIRED_IF_EITHER_PRESENT
  }

  record Range(Instant start, Instant end, RequirementPolicy policy) {

    public Range {
      // Defensive default
      if (policy == null) {
        policy = RequirementPolicy.NONE;
      }
    }

    public static Range of(Instant start, Instant end, RequirementPolicy policy) {
      return new Range(start, end, policy);
    }

    public static Range optional(Instant start, Instant end) {
      return new Range(start, end, RequirementPolicy.NONE);
    }

    public static Range startRequired(Instant start, Instant end) {
      return new Range(start, end, RequirementPolicy.START_REQUIRED);
    }

    public static Range endRequired(Instant start, Instant end) {
      return new Range(start, end, RequirementPolicy.END_REQUIRED);
    }

    public static Range bothRequired(Instant start, Instant end) {
      return new Range(start, end, RequirementPolicy.BOTH_REQUIRED);
    }

    public static Range bothRequiredIfEitherPresent(Instant start, Instant end) {
      return new Range(start, end, RequirementPolicy.BOTH_REQUIRED_IF_EITHER_PRESENT);
    }
  }
}
