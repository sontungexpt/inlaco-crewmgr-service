package com.inlaco.crewmgrservice.shared.mapstruct.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ObjectIdMapper {

  default String map(ObjectId id) {
    return id != null ? id.toHexString() : null;
  }

  default ObjectId map(String id) {
    return id != null ? new ObjectId(id) : null;
  }
}
