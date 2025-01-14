package com.inlaco.crewmgrservice.common.model;

import java.time.Instant;
import org.bson.types.ObjectId;

public interface Versionable<ID, VT> {

  ID getId();

  VT getVersion();

  VT getPrevVersion();

  String getChangeLog();

  Instant getCreatedAt();

  Instant getUpdatedAt();

  ObjectId getCreatedBy();

  ObjectId getUpdatedBy();
}
