package com.inlaco.crewmgrservice.utils.varcaseconvertor;

public class CamelCaseConverter extends AbstractCaseConverter {
  public CamelCaseConverter(String key) {
    super(key);
  }

  @Override
  protected String toPascalCase() {
    return key.substring(0, 1).toUpperCase() + key.substring(1);
  }

  @Override
  protected String toCamelCase() {
    return key;
  }

  @Override
  protected String toSnakeCase() {
    StringBuilder snakeCase = new StringBuilder();
    for (char c : key.toCharArray()) {
      if (Character.isUpperCase(c)) {
        if (snakeCase.length() > 0) {
          snakeCase.append("_");
        }
        snakeCase.append(Character.toLowerCase(c));
      } else {
        snakeCase.append(c);
      }
    }
    return snakeCase.toString();
  }
}
