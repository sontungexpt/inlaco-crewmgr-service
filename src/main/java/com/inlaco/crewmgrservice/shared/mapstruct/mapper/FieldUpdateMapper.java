package com.inlaco.crewmgrservice.shared.mapstruct.mapper;

import com.inlaco.crewmgrservice.shared.application.model.FieldUpdate;
import org.mapstruct.Mapper;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(componentModel = "spring")
public interface FieldUpdateMapper {

  default <T> FieldUpdate<T> map(JsonNullable<T> value) {
    if (value == null || !value.isPresent()) {
      return FieldUpdate.unchanged();
    }
    return FieldUpdate.of(value.get());
  }

  // default <T> FieldUpdate<T> map(T value) {
  //   return FieldUpdate.of(value);
  // }

  // @Condition
  // default boolean isUpdated(FieldUpdate<?> change) {
  //   return change != null && change.isUpdated();
  // }
}
