package com.inlaco.crewmgrservice.shared.mapper;

import org.bson.types.ObjectId;

public interface ObjectIdMapper {

  default String map(ObjectId id) {
    return id != null ? id.toHexString() : null;
  }

  default ObjectId map(String id) {
    return id != null ? new ObjectId(id) : null;
  }
}
