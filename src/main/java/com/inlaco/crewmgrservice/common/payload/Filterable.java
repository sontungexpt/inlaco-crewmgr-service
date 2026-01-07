package com.inlaco.crewmgrservice.common.payload;

import java.lang.reflect.Field;
import java.util.Collection;

public interface Filterable {

  default boolean isFilterable() {
    for (Field field : this.getClass().getDeclaredFields()) {
      field.setAccessible(true);

      Object value;
      try {
        value = field.get(this);
      } catch (IllegalAccessException e) {
        continue; // bỏ qua field không truy cập được
      }

      if (value == null) {
        continue;
      }

      // String
      else if (value instanceof String str && str.isBlank()) {
        continue;
      }

      // Collection
      else if (value instanceof Collection<?> col && col.isEmpty()) {
        continue;
      }

      // Array
      else if (value.getClass().isArray() && java.lang.reflect.Array.getLength(value) == 0) {
        continue;
      }

      // At least 1 field valid for filtering
      return true;
    }
    return false;
  }
}
