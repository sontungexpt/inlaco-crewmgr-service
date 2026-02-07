package com.inlaco.crewmgrservice.infrastructure.serialization.serializer.slug;

import com.inlaco.crewmgrservice.domain.annotation.Slug;
import com.inlaco.crewmgrservice.shared.slug.SlugDefinition;
import java.lang.reflect.Field;
import org.springframework.stereotype.Component;

@Component
public class AnnotationSlugDefinition implements SlugDefinition {

  @Override
  public String[] sourceFields(Field slugField) {
    return slugField.getAnnotation(Slug.class).fields();
  }

  @Override
  public boolean isSlugField(Field field) {
    return field.isAnnotationPresent(Slug.class);
  }

  @Override
  public boolean hyphen(Field slugField) {
    return slugField.getAnnotation(Slug.class).hyphen();
  }

  @Override
  public boolean lowerCase(Field slugField) {
    return slugField.getAnnotation(Slug.class).lowerCase();
  }
}
