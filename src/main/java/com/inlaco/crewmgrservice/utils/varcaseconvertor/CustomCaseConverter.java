package com.inlaco.crewmgrservice.utils.varcaseconvertor;

public class CustomCaseConverter extends AbstractCaseConverter {
  public CustomCaseConverter(String key) {
    super(key);
  }

  @Override
  protected String toPascalCase() {
    return key;
  }

  @Override
  protected String toCamelCase() {
    return key;
  }

  @Override
  protected String toSnakeCase() {
    return key;
  }
}
