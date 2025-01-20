package com.inlaco.crewmgrservice.utils;

import java.util.Map;

public class HtmlEmailVariable {

  private StringBuilder htmlContentBuilder;
  private Case convertTo = Case.CUSTOM_CASE;

  private Map<Case, AbstractCaseConverter> caseConverters =
      Map.of(
          Case.PASCAL_CASE, new PascalCaseConverter(""),
          Case.CAMEL_CASE, new CamelCaseConverter(""),
          Case.SNAKE_CASE, new SnakeCaseConverter(""),
          Case.CUSTOM_CASE, new CustomCaseConverter(""));

  private String toCase(String key, Case convertTo) {
    if (convertTo == null) return key;
    var caseConverter = caseConverters.get(Case.from(key));
    caseConverter.setKey(key);
    return caseConverter.toCase(convertTo);
  }

  public HtmlEmailVariable convertTo(Case convertTo) {
    this.convertTo = convertTo;
    return this;
  }

  public static HtmlEmailVariable htmlContent(String htmlContent) {
    return new HtmlEmailVariable(htmlContent);
  }

  public HtmlEmailVariable(String htmlContent) {
    this.htmlContentBuilder = new StringBuilder(htmlContent);
  }

  public HtmlEmailVariable var(String key, String value) {
    key = toCase(key, convertTo);
    replace(key, value);
    return this;
  }

  private void replace(String key, String value) {
    key = formatKey(key);
    int index = htmlContentBuilder.indexOf(key);
    while (index != -1) {
      htmlContentBuilder.replace(index, index + key.length(), value);
      index = htmlContentBuilder.indexOf(key, value.length());
    }
  }

  private String formatKey(String key) {
    return "${" + key + "}";
  }

  public HtmlEmailVariable var(String key, String value, Case convertTo) {
    replace(toCase(key, convertTo), value);
    return this;
  }

  public String buildHtml() {
    return htmlContentBuilder.toString();
  }

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

  private abstract class AbstractCaseConverter {
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

  private class PascalCaseConverter extends AbstractCaseConverter {
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

  private class CamelCaseConverter extends AbstractCaseConverter {
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

  private class SnakeCaseConverter extends AbstractCaseConverter {
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

  private class CustomCaseConverter extends AbstractCaseConverter {
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
}
