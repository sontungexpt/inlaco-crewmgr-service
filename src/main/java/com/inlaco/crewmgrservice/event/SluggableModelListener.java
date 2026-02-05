package com.inlaco.crewmgrservice.event;

import com.inlaco.crewmgrservice.common.model.Sluggable;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.AutoSlugify;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.AutoSlugify.Separator;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.AutoSlugify.UpdateStrategy;
import com.inlaco.crewmgrservice.utils.SlugUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@SuppressWarnings("rawtypes")
@Component
@RequiredArgsConstructor
@Slf4j
public class SluggableModelListener extends AbstractMongoEventListener<Sluggable> {

  private final MongoTemplate mongoTemplate;

  // public void onBeforeConvert(BeforeConvertEvent<Sluggable> event) {}

  @Override
  public void onBeforeConvert(BeforeConvertEvent<Sluggable> event) {
    var sluggable = event.getSource();

    Class<?> slugableClazz = sluggable.getClass();

    Sluggable oldSlugable = null;

    for (Field field : slugableClazz.getDeclaredFields()) {
      field.setAccessible(true);
      AutoSlugify autoSlugify = field.getAnnotation(AutoSlugify.class);
      if (autoSlugify != null && field.getType().equals(String.class)) {
        UpdateStrategy strategy = autoSlugify.updateStrategy();

        boolean haveToUpdate =
            ReflectionUtils.getField(field, sluggable) == null
                || sluggable.getId() == null
                || strategy == UpdateStrategy.ON_DOCUMENT_SAVE;

        if (!haveToUpdate) {
          if (strategy == UpdateStrategy.NEVER_UPDATE) {
            continue;
          } else if (strategy == UpdateStrategy.ON_VALUE_CHANGE) {
            if (oldSlugable == null) {
              oldSlugable = (Sluggable) mongoTemplate.findById(sluggable.getId(), slugableClazz);
              if (oldSlugable == null) {
                haveToUpdate = true;
              }
            }
          }
        }

        List<String> values = new ArrayList<>();

        for (String f : autoSlugify.fields()) {
          Field foundField = ReflectionUtils.findField(slugableClazz, f);

          if (foundField == null) {
            throw new IllegalArgumentException(
                "Cannot find field "
                    + f
                    + " in class "
                    + slugableClazz.getName()
                    + "when slugifying for "
                    + field.getName());
          }

          foundField.setAccessible(true);

          Object value = ReflectionUtils.getField(foundField, sluggable);

          if (haveToUpdate == false) {
            Object oldValue = ReflectionUtils.getField(foundField, oldSlugable);
            if (value != null && oldValue != null && !oldValue.equals(value)) {
              haveToUpdate = true;
            }
          }
          if (value != null) {
            values.add(value.toString().trim());
          }
        }

        if (!haveToUpdate) {
          continue;
        }

        log.info(
            "Updating slug for field {} in class {} in document {}",
            field.getName(),
            slugableClazz,
            sluggable.getId());

        Separator sep = autoSlugify.separator();
        String slug =
            SlugUtils.createSlug(
                String.join(sep.getValue(), values),
                autoSlugify.unique(),
                autoSlugify.fromUniqueField(),
                sep);

        ReflectionUtils.setField(field, sluggable, slug);

        // Only uncomment this if use onBeforeSave event instead of onBeforeConvert
        // Document document = event.getDocument();
        // if (document != null) {
        //   document.put(field.getName(), slug);
        // }
      }
    }
  }
}
