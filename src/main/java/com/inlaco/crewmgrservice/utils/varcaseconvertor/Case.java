package com.inlaco.crewmgrservice.utils.varcaseconvertor;

public enum Case {
  PASCAL_CASE,
  CAMEL_CASE,
  SNAKE_CASE,
  CUSTOM_CASE;

  public static Case from(String str) {
    if (str.matches("^[A-Z][a-zA-Z0-9]*$")) {
      return Case.PASCAL_CASE;
    } else if (str.matches("^[a-z][a-zA-Z0-9]*$")) {
      return Case.CAMEL_CASE;
    } else if (str.matches("^[a-z0-9_]+$")) {
      return Case.SNAKE_CASE;
    } else {
      return Case.CUSTOM_CASE;
    }
  }
}
