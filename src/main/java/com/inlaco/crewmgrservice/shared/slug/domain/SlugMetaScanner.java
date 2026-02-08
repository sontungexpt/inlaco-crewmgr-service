package com.inlaco.crewmgrservice.shared.slug.domain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

public class SlugMetaScanner {

  private final Map<Class<?>, List<SlugMeta>> cache = new ConcurrentReferenceHashMap<>(256);

  public List<SlugMeta> scan(Class<?> type, SlugDefinition definition) {
    return cache.computeIfAbsent(type, t -> doScan(t, definition));
  }

  private List<SlugMeta> doScan(Class<?> type, SlugDefinition def) {
    List<SlugMeta> metas = new ArrayList<>();

    ReflectionUtils.doWithFields(
        type,
        (field) -> {
          List<Field> sources = new ArrayList<>();
          for (String name : def.sourceFields(field)) {
            Field f = ReflectionUtils.findField(type, name);
            if (f == null) {
              throw new IllegalArgumentException(
                  "Field '" + name + "' not found in " + type.getName());
            }
            sources.add(f);
          }

          metas.add(new SlugMeta(field, sources, def.hyphen(field), def.lowerCase(field)));
        },
        (field) -> field.getType() == String.class && def.isSlugField(field));

    return metas;
  }
}
