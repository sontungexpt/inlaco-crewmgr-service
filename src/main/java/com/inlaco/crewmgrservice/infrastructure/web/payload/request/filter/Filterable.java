package com.inlaco.crewmgrservice.infrastructure.web.payload.request.filter;

import java.lang.reflect.Field;
import java.util.Collection;

public interface Filterable {

  default boolean isFilterable() {
    // 2️⃣ Fallback: auto-detect by field
    for (Field field : this.getClass().getDeclaredFields()) {
      field.setAccessible(true);

      Object value;
      try {
        value = field.get(this);
      } catch (IllegalAccessException e) {
        continue;
      }

      if (isEmptyValue(value)) {
        continue;
      }

      return true;
    }

    return false;
  }

  // ========================= HELPERS =========================

  private boolean isEmptyValue(Object value) {
    if (value == null) return true;

    if (value instanceof String str) {
      return str.isBlank();
    }

    if (value instanceof Collection<?> col) {
      return col.isEmpty();
    }

    if (value.getClass().isArray()) {
      return java.lang.reflect.Array.getLength(value) == 0;
    }

    return false;
  }
}
