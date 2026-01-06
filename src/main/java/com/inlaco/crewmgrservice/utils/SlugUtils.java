package com.inlaco.crewmgrservice.utils;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.github.slugify.Slugify;
import com.inlaco.crewmgrservice.annotation.AutoSlugify.Separator;
import org.springframework.util.StringUtils;

public class SlugUtils {

  private static int determineNanoIdSize(int length) {
    if (length > 100) return 5;
    else if (length > 80) return 7;
    else if (length > 70) return 9;
    else if (length > 60) return 11;
    else if (length > 40) return 13;
    else if (length > 30) return 15;
    else if (length > 20) return 17;
    else if (length > 15) return 19;
    return NanoIdUtils.DEFAULT_SIZE;
  }

  public static String createSlug(
      String value, boolean unique, boolean fromUniqueField, Separator separator) {
    if (!StringUtils.hasText(value)) {
      throw new IllegalArgumentException("Cannot generate a slug from an empty string.");
    }

    final Slugify slg =
        Slugify.builder()
            .transliterator(true)
            .underscoreSeparator(separator == Separator.UNDERSCORE)
            .build();

    String slug = slg.slugify(value);
    if (unique && !fromUniqueField) {
      slug += separator.getValue();
      int value_len = value.length();
      int nanoid_size = determineNanoIdSize(value_len);
      slug +=
          NanoIdUtils.randomNanoId(
              NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, nanoid_size);
    }

    return slug;
  }
}
