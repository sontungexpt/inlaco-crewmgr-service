package com.inlaco.crewmgrservice.utils.varcaseconvertor;

public class SnakeCaseConverter extends AbstractCaseConverter {
  public SnakeCaseConverter(String key) {
    super(key);
  }

  @Override
  protected String toPascalCase() {
    String[] words = key.split("_");
    StringBuilder pascalCase = new StringBuilder();
    for (String word : words) {
      pascalCase.append(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase());
    }
    return pascalCase.toString();
  }

  @Override
  protected String toCamelCase() {
    StringBuilder camelCase = new StringBuilder();
    for (String word : key.split("_")) {
      camelCase.append(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase());
    }
    return camelCase.substring(0, 1).toLowerCase() + camelCase.substring(1);
  }

  @Override
  protected String toSnakeCase() {
    return key;
  }
}
