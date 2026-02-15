package com.inlaco.crewmgrservice.infrastructure.persistence.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationUtils {

  public static boolean hasAnnotationField(Class<?> clazz, Class<? extends Annotation> annotation) {
    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(annotation)) {
        return true;
      }
    }
    return false;
  }

  public static String getFirstAnnotationFieldName(
      Class<?> clazz, Class<? extends Annotation> annotation) {
    Field field = getFirstAnnotationField(clazz, annotation);
    return field == null ? null : field.getName();
  }

  public static Field getFirstAnnotationField(
      Class<?> clazz, Class<? extends Annotation> annotation) {
    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(annotation)) {
        return field;
      }
    }
    return null;
  }

  public static List<Field> getAnnotationFields(
      Class<?> clazz, Class<? extends Annotation> annotation) {
    List<Field> fields = new ArrayList<>();
    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(annotation)) {
        fields.add(field);
      }
    }
    return fields;
  }
}
