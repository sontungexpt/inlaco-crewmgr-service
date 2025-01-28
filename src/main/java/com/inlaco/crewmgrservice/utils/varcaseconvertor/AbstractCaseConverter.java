package com.inlaco.crewmgrservice.utils.varcaseconvertor;

public abstract class AbstractCaseConverter {
  protected String key;

  public void setKey(String key) {
    this.key = key;
  }

  public AbstractCaseConverter(String key) {
    this.key = key;
  }

  public String toCase(Case convertTo) {
    switch (convertTo) {
      case PASCAL_CASE:
        return toPascalCase();
      case CAMEL_CASE:
        return toCamelCase();
      case SNAKE_CASE:
        return toSnakeCase();
      default:
        return key;
    }
  }

  protected abstract String toPascalCase();

  protected abstract String toCamelCase();

  protected abstract String toSnakeCase();
}
