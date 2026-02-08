package com.inlaco.crewmgrservice.shared.slug.domain;

import com.github.slugify.Slugify;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

@Slf4j
public class SlugProcessor {

  private final SlugMetaScanner scanner = new SlugMetaScanner();

  public void generate(Object target, SlugDefinition definition) {
    if (target == null) return;
    log.debug("SlugProcessor: generating slug for {}", target.getClass().getSimpleName());

    List<SlugMeta> metas = scanner.scan(target.getClass(), definition);

    for (SlugMeta meta : metas) {
      List<String> parts = new ArrayList<>();

      for (Field f : meta.sourceFields()) {
        ReflectionUtils.makeAccessible(f);
        Object v = ReflectionUtils.getField(f, target);
        if (v != null) parts.add(v.toString().trim());
      }

      if (parts.isEmpty()) continue;

      String sep = meta.hyphen() ? "-" : "_";
      String raw = String.join(sep, parts);
      if (meta.lowerCase()) raw = raw.toLowerCase();

      Slugify slugify =
          Slugify.builder()
              .transliterator(true)
              .lowerCase(meta.lowerCase())
              .underscoreSeparator(!meta.hyphen())
              .build();

      log.debug("SlugProcessor: generating slug for {} ({})", meta.slugField().getName(), raw);
      ReflectionUtils.makeAccessible(meta.slugField());
      ReflectionUtils.setField(meta.slugField(), target, slugify.slugify(raw));
    }
  }
}
