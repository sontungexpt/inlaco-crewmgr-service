package com.inlaco.crewmgrservice.shared.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class ObjectIdMapper {

  @Named("objectIdToString")
  public String objectIdToString(ObjectId id) {
    return id != null ? id.toHexString() : null;
  }

  @Named("stringToObjectId")
  public ObjectId stringToObjectId(String id) {
    return (id != null && ObjectId.isValid(id)) ? new ObjectId(id) : null;
  }
}
