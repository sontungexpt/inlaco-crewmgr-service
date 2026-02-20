package com.inlaco.crewmgrservice.infrastructure.persistence.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap;

final class AnnotationUtil {

  private AnnotationUtil() {}

  private static record CacheKey(Class<?> entityClass, Class<? extends Annotation> annotation) {}

  // Cache key (entityClass + annotation)
  private static final Map<CacheKey, String> CACHE = new ConcurrentReferenceHashMap<>();

  public static String findFirstAnnotationFieldName(
      Class<?> entityClass, Class<? extends Annotation> annotation) {
    CacheKey key = new CacheKey(entityClass, annotation);
    return CACHE.computeIfAbsent(key, k -> resolve(entityClass, annotation));
  }

  private static String resolve(Class<?> entityClass, Class<? extends Annotation> annotation) {
    for (Class<?> clazz = entityClass;
        clazz != null && clazz != Object.class;
        clazz = clazz.getSuperclass()) {

      for (Field field : clazz.getDeclaredFields()) {
        if (field.isAnnotationPresent(annotation)) {
          return field.getName();
        }
      }
    }

    return null; // cache null
  }
}
