package com.inlaco.crewmgrservice.feature.contract.domain.objectvalue;

import java.time.Instant;

public record Version(int num, Instant createdAt) implements Comparable<Version> {
  @Override
  public int compareTo(Version other) {
    return Integer.compare(this.num, other.num);
  }
}
