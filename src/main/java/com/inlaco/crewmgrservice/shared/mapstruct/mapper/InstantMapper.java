package com.inlaco.crewmgrservice.shared.mapstruct.mapper;

import java.time.Instant;

public interface InstantMapper {

  default String map(Instant id) {
    return id != null ? id.toString() : null;
  }

  default Instant map(String id) {
    return id != null ? Instant.parse(id) : null;
  }
}
