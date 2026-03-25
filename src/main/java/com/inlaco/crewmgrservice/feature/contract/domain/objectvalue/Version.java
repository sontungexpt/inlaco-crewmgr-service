package com.inlaco.crewmgrservice.feature.contract.domain.objectvalue;

import java.time.Instant;

public record Version(int num, Instant createdAt) implements Comparable<Version> {

  @Override
  public int compareTo(Version other) {
    int cmp = Integer.compare(this.num, other.num);
    if (cmp != 0) return cmp;
    return this.createdAt.compareTo(other.createdAt);
  }
}
