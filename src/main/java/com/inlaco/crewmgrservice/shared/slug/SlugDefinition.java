package com.inlaco.crewmgrservice.shared.slug;

import java.lang.reflect.Field;

public interface SlugDefinition {
  boolean isSlugField(Field field);

  String[] sourceFields(Field slugField);

  boolean hyphen(Field slugField);

  boolean lowerCase(Field slugField);
}
