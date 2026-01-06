package com.inlaco.crewmgrservice.common.model;

import java.time.Instant;
import org.bson.types.ObjectId;

public interface Versionable<ID, VT> {

  ID getId();

  ID getCurrentVersionId();

  ID getFirstVersionId();

  ID getPrevVersionId();

  ID getNextVersionId();

  VT getVersion();

  VT getPrevVersion();

  VT getNextVersion();

  String getChangeLog();

  Instant getCreatedAt();

  Instant getUpdatedAt();

  ObjectId getCreatedBy();

  ObjectId getUpdatedBy();
}
