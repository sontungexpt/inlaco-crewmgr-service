package com.inlaco.crewmgrservice.shared.slug;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.ReflectionUtils;

public class SlugMetaScanner {

  private final Map<Class<?>, List<SlugMeta>> cache = new ConcurrentHashMap<>();

  public List<SlugMeta> scan(Class<?> type, SlugDefinition definition) {
    return cache.computeIfAbsent(type, t -> doScan(t, definition));
  }

  private List<SlugMeta> doScan(Class<?> type, SlugDefinition def) {
    List<SlugMeta> metas = new ArrayList<>();

    for (Field slugField : type.getDeclaredFields()) {
      if (slugField.getType() != String.class) continue;
      if (!def.isSlugField(slugField)) continue;

      ReflectionUtils.makeAccessible(slugField);

      List<Field> sources = new ArrayList<>();
      for (String name : def.sourceFields(slugField)) {
        Field f = ReflectionUtils.findField(type, name);
        if (f == null) {
          throw new IllegalArgumentException("Field '" + name + "' not found in " + type.getName());
        }
        ReflectionUtils.makeAccessible(f);
        sources.add(f);
      }

      metas.add(new SlugMeta(slugField, sources, def.hyphen(slugField), def.lowerCase(slugField)));
    }
    return metas;
  }
}
