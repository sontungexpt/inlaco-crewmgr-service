package com.inlaco.crewmgrservice.common.payload;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

@Valid
@com.inlaco.crewmgrservice.validation.annotation.TimeFrame
public interface TimeFrame {

  @NoArgsConstructor
  public static class Pair {
    private Instant start;
    private Instant end;

    private boolean requiredFirst = true; // default is required

    // default is not required its mean that it is any time
    // in future.
    private boolean requiredSecond = true;

    public Pair(Instant start, Instant end) {
      this.start = start;
      this.end = end;
    }

    public Pair(Instant start, Instant end, boolean requiredFirst, boolean requiredSecond) {
      this.start = start;
      this.end = end;
      this.requiredFirst = requiredFirst;
      this.requiredSecond = requiredSecond;
    }

    public static Pair of(Instant start, Instant end) {
      return new Pair(start, end);
    }

    public static Pair of(
        Instant start, Instant end, boolean requiredFirst, boolean requiredSecond) {
      return new Pair(start, end, requiredFirst, requiredSecond);
    }

    public Instant getStart() {
      return start;
    }

    public Instant getEnd() {
      return end;
    }

    public boolean isRequiredFirst() {
      return requiredFirst;
    }

    public boolean isRequiredSecond() {
      return requiredSecond;
    }
  }

  @JsonSerialize(using = NullSerializer.class)
  List<Pair> getTimeFrames();
}
