package com.inlaco.crewmgrservice.utils;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import org.springframework.lang.NonNull;

public class TextTemplateBuilder {

  private StringBuilder stringBuilder;
  private Function<String, String> keyFormatter = varName -> "${" + varName + "}";

  /**
   * Reads the content of a file and creates a new TextTemplateBuilder instance. The file should be
   * located within the project's directory structure.
   *
   * @param path the path to the file relative to the project's root
   * @return a new TextTemplateBuilder instance
   * @throws IOException if an I/O error occurs reading from the file
   */
  public static TextTemplateBuilder relativePath(@NonNull String path) throws IOException {
    assert path != null;
    return new TextTemplateBuilder(Files.readString(Paths.get(path)));
  }

  /**
   * Reads the content of a file and creates a new TextTemplateBuilder instance.
   *
   * @param uri the URI to the file
   * @return a new TextTemplateBuilder instance
   * @throws IOException if an I/O error occurs reading from the file
   */
  public static TextTemplateBuilder src(@NonNull URI uri) throws IOException {
    assert uri != null;
    return new TextTemplateBuilder(Files.readString(Paths.get(uri)));
  }

  /**
   * Creates a new TextTemplateBuilder instance with the given content.
   *
   * @param content the content of the template
   * @return a new TextTemplateBuilder instance
   */
  public static TextTemplateBuilder content(@NonNull String content) {
    assert content != null;
    return new TextTemplateBuilder(content);
  }

  /**
   * Sets the key formatter function. The key formatter is used to format the variable names in the
   * template. The default key formatter is "${varName}".
   *
   * @param keyFormatter the key formatter function
   * @return this TextTemplateBuilder instance
   */
  public TextTemplateBuilder keyFormatter(@NonNull Function<String, String> keyFormatter) {
    assert keyFormatter != null;
    this.keyFormatter = keyFormatter;
    return this;
  }

  private TextTemplateBuilder(@NonNull String content) {
    assert content != null;
    this.stringBuilder = new StringBuilder(content);
  }

  /**
   * Replaces a variable in the template with the given value.
   *
   * @param key the variable name
   * @param value the value to replace the variable with
   * @return this TextTemplateBuilder instance
   */
  public TextTemplateBuilder var(@NonNull String key, @NonNull String value) {
    assert key != null;
    assert value != null;
    replace(key, value);
    return this;
  }

  private void replace(@NonNull String key, @NonNull String value) {
    String formattedKey = keyFormatter.apply(key);
    int index = 0;

    while (index < stringBuilder.length()) {
      int foundIndex = stringBuilder.indexOf(formattedKey, index);

      if (foundIndex == -1) {
        break;
      }

      // Check for escape character
      if (foundIndex > 0 && stringBuilder.charAt(foundIndex - 1) == '\\') {
        // Remove the escape character
        stringBuilder.deleteCharAt(foundIndex - 1);
        index = foundIndex + formattedKey.length() - 1; // Skip past the escaped variable
      } else {
        // Replace the variable
        stringBuilder.replace(foundIndex, foundIndex + formattedKey.length(), value);
        index = foundIndex + value.length(); // Move past the replaced value
      }
    }
  }

  /**
   * Builds the content of the template.
   *
   * @return the content of the template
   */
  public String buildContent() {
    return stringBuilder.toString();
  }
}
