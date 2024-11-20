package com.inlaco.crewmgrservice.common.model;

import java.time.Instant;

public interface Versionable<ID, VT> {

  ID getId();

  VT getVersion();

  VT getPrevVersion();

  String getChangeLog();

  Instant getCreatedAt();

  Instant getUpdatedAt();
}
