package com.inlaco.crewmgrservice.utils.varcaseconvertor;

public class PascalCaseConverter extends AbstractCaseConverter {
  public PascalCaseConverter(String key) {
    super(key);
  }

  @Override
  protected String toPascalCase() {
    return key;
  }

  @Override
  protected String toCamelCase() {
    return key.substring(0, 1).toLowerCase() + key.substring(1);
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
