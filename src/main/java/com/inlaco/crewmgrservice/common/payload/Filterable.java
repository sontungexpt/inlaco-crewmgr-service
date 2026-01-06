package com.inlaco.crewmgrservice.common.payload;

import java.util.Collection;

public interface Filterable {

  default boolean isFilterable() {
    for (var field : this.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      try {
        var value = field.get(this);
        var type = field.getType();
        if (value != null) {
          if (type == String.class && ((String) value).isEmpty()) {
            continue;
          } else if (type == Collection.class && ((Collection<?>) value).isEmpty()) {
            continue;
          }
          return true;
        }

      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return false;
  }
}
