package com.inlaco.crewmgrservice.shared.template;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jspecify.annotations.NonNull;
import org.springframework.core.io.ClassPathResource;

public class TemplateBuilder {

  public enum TemplateSyntax {
    DOLLAR_CURLY("(\\\\*)\\$\\{([a-zA-Z0-9_]+)(?::([^}]+))?}"), // ${varName}
    DOUBLE_CURLY("(\\\\*)\\{\\{([a-zA-Z0-9_]+)(?::([^}]+))?}}"), // {{varName}}
    DOLLAR_SIMPLE("(\\\\*)\\$([a-zA-Z0-9_]+)"), // $varName
    DOUBLE_ANGLE("(\\\\*)<<([a-zA-Z0-9_]+)(?::([^>]+))?>>"); // <<varName>>

    private final Pattern pattern;

    TemplateSyntax(String pattern) {
      this.pattern = Pattern.compile(pattern);
    }

    public Pattern getPattern() {
      return pattern;
    }
  }

  private String template;
  private final Map<String, String> variables = new HashMap<>();
  private TemplateSyntax syntax = TemplateSyntax.DOLLAR_CURLY;

  private TemplateBuilder(@NonNull String content) {
    this.template = content;
  }

  /*
   * This method is used to create a new TextTemplateBuilder instance from a file path. The file
   * path is used to read the content of the file and create the template.
   *
   */
  public static TemplateBuilder relativePath(@NonNull String path) throws IOException {
    return new TemplateBuilder(Files.readString(Paths.get(path)));
  }

  /*
   *  This method is used to create a new TextTemplateBuilder instance from a resource path. The
   *  resource path is used to read the content of the resource and create the template.
   *
   */
  public static TemplateBuilder resourcePath(@NonNull String resourcePath) throws IOException {
    ClassPathResource resource = new ClassPathResource(resourcePath);
    return new TemplateBuilder(
        new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
  }

  /*
   * This method is used to create a new TextTemplateBuilder instance from a file path. The file
   * path is used to read the content of the file and create the template.
   *
   */
  public static TemplateBuilder src(@NonNull URI uri) throws IOException {
    return new TemplateBuilder(Files.readString(Paths.get(uri)));
  }

  /*
   * This method is used to set the template content. The content is the text that will be used to
   * create the template.
   *
   */
  public static TemplateBuilder content(@NonNull String content) {
    return new TemplateBuilder(content);
  }

  /*
   * This method is used to set the template syntax. The syntax is used to identify the variables in
   * the template. The default syntax is DOLLAR_CURLY.
   *
   */
  public TemplateBuilder syntax(@NonNull TemplateSyntax syntax) {
    this.syntax = syntax;
    return this;
  }

  /*
   * This method is used to add a variable to the template. The key is the variable name in the
   * template and the value is the value that will replace the variable in the template.
   *
   */
  public TemplateBuilder var(@NonNull String key, @NonNull String value) {
    variables.put(key, value);
    return this;
  }

  /*
   * This method is used to replace the variables in the template with the values provided in the
   * variables map. The keyFormatter is used to format the key before replacing it in the template.
   * This is useful when the key in the template is different from the key in the variables map.
   *
   */
  private void replace(
      @NonNull String key,
      @NonNull String value,
      @NonNull Function<String, String> keyFormatter,
      @NonNull StringBuilder template) {
    String formattedKey = keyFormatter.apply(key);
    int index = 0;

    while (index < template.length()) {
      int foundIndex = template.indexOf(formattedKey, index);

      if (foundIndex == -1) {
        break;
      }

      int backslashes = 0;
      if (foundIndex > 0) {
        // Check for escape characters
        for (int i = foundIndex - 1; i >= 0 && template.charAt(i) == '\\'; i--) {
          backslashes++;
        }
      }

      if (backslashes % 2 == 1) {
        // Remove the escape character
        template.deleteCharAt(foundIndex - 1);
        index = foundIndex + formattedKey.length() - 1; // Skip past the escaped variable
      } else {
        // Replace the variable
        String replacedValue = "\\".repeat(backslashes / 2) + value;

        int start = foundIndex - backslashes;
        template.replace(start, foundIndex + formattedKey.length(), replacedValue);

        index = start + replacedValue.length(); // Move past the replaced value

        // template.replace(foundIndex, foundIndex + formattedKey.length(), value);
        // index = foundIndex + value.length(); // Move past the replaced value
      }
    }
  }

  /*
   * This method is used to replace the variables in the template with the values provided in the
   * variables map. The keyFormatter is used to format the key before replacing it in the template.
   * This is useful when the key in the template is different from the key in the variables map.
   *
   */
  public String buildContent(Function<String, String> keyFormatter) {
    StringBuilder templateBuilder = new StringBuilder(template);
    for (Map.Entry<String, String> entry : variables.entrySet()) {
      replace(entry.getKey(), entry.getValue(), keyFormatter, templateBuilder);
    }
    return templateBuilder.toString();
  }

  /*
   * This method is used to replace the variables in the template with the values provided in the
   * variables map. The keyFormatter is used to format the key before replacing it in the template.
   * This is useful when the key in the template is different from the key in the variables map.
   *
   */
  public String buildContent() {
    StringBuilder result = new StringBuilder();
    Matcher matcher = syntax.getPattern().matcher(template);

    while (matcher.find()) {
      String backslashes = matcher.group(1);
      String key = matcher.group(2);
      String defaultValue = matcher.group(3);
      String value = variables.getOrDefault(key, defaultValue != null ? defaultValue : "");

      int backslashesLength = backslashes.length();
      if (backslashesLength % 2 == 1) {
        // ESCAPED → remove 1 slash and keep literal
        matcher.appendReplacement(result, backslashes.substring(1) + matcher.group(0));
      } else {
        // NOT ESCAPED → replace normally
        matcher.appendReplacement(
            result, Matcher.quoteReplacement("\\".repeat(backslashesLength / 2)) + value);
      }
    }

    matcher.appendTail(result);
    return result.toString();
  }
}
