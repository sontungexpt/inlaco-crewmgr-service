// package com.inlaco.crewmgrservice.infrastructure.web.event;

// import com.inlaco.crewmgrservice.domain.annotation.AutoSlugify;
// import com.inlaco.crewmgrservice.domain.model.Sluggable;
// import com.inlaco.crewmgrservice.utils.SlugUtils;
// import java.lang.reflect.Field;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Map;
// import java.util.Objects;
// import java.util.concurrent.ConcurrentHashMap;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.data.mongodb.core.MongoTemplate;
// import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
// import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
// import org.springframework.stereotype.Component;
// import org.springframework.util.ReflectionUtils;

// @Component
// @RequiredArgsConstructor
// @Slf4j
// public class SluggableModelListener extends AbstractMongoEventListener<Sluggable<?>> {

//   private final MongoTemplate mongoTemplate;
//   private final Map<Class<?>, List<Field>> slugFieldsCache = new ConcurrentHashMap<>();

//   @Override
//   public void onBeforeConvert(BeforeConvertEvent<Sluggable<?>> event) {
//     Sluggable<?> entity = event.getSource();
//     Class<?> clazz = entity.getClass();

//     List<Field> slugFields = slugFieldsCache.computeIfAbsent(clazz, this::findSlugFields);

//     if (slugFields.isEmpty()) {
//       return;
//     }

//     Sluggable<?> oldEntity =
//         entity.getId() == null
//             ? null
//             : (Sluggable<?>) mongoTemplate.findById(entity.getId(), clazz);

//     for (Field slugField : slugFields) {
//       processSlugField(entity, oldEntity, slugField);
//     }
//   }

//   private void processSlugField(Sluggable<?> entity, Sluggable<?> oldEntity, Field slugField) {

//     AutoSlugify config = slugField.getAnnotation(AutoSlugify.class);

//     if (!shouldUpdate(entity, oldEntity, slugField, config)) {
//       return;
//     }

//     List<String> values =
//         collectValues(entity, oldEntity, config, shouldCompare(oldEntity, config));

//     if (values.isEmpty()) {
//       return;
//     }

//     String slug =
//         SlugUtils.createSlug(
//             String.join(config.separator().getValue(), values),
//             config.unique(),
//             config.fromUniqueField(),
//             config.separator());

//     ReflectionUtils.makeAccessible(slugField);
//     ReflectionUtils.setField(slugField, entity, slug);

//     log.debug(
//         "Slug updated: field={}, entity={}, id={}",
//         slugField.getName(),
//         entity.getClass().getSimpleName(),
//         entity.getId());
//   }

//   // ---------------- helpers ----------------

//   private boolean shouldUpdate(
//       Sluggable<?> entity, Sluggable<?> oldEntity, Field slugField, AutoSlugify config) {

//     ReflectionUtils.makeAccessible(slugField);
//     Object currentSlug = ReflectionUtils.getField(slugField, entity);

//     if (currentSlug == null || entity.getId() == null) {
//       return true;
//     }

//     return switch (config.updateStrategy()) {
//       case ON_DOCUMENT_SAVE -> true;
//       case NEVER_UPDATE -> false;
//       case ON_VALUE_CHANGE -> oldEntity == null || valuesChanged(entity, oldEntity, config);
//     };
//   }

//   private boolean shouldCompare(Sluggable<?> oldEntity, AutoSlugify config) {
//     return config.updateStrategy() == AutoSlugify.UpdateStrategy.ON_VALUE_CHANGE
//         && oldEntity != null;
//   }

//   private boolean valuesChanged(Object current, Object old, AutoSlugify config) {

//     for (String fieldName : config.fields()) {
//       Field f = ReflectionUtils.findField(current.getClass(), fieldName);
//       if (f == null) {
//         throw new IllegalArgumentException("Field not found: " + fieldName);
//       }

//       ReflectionUtils.makeAccessible(f);

//       Object newVal = ReflectionUtils.getField(f, current);
//       Object oldVal = ReflectionUtils.getField(f, old);

//       if (!Objects.equals(newVal, oldVal)) {
//         return true;
//       }
//     }
//     return false;
//   }

//   private List<String> collectValues(
//       Object entity, Object oldEntity, AutoSlugify config, boolean compare) {

//     List<String> values = new ArrayList<>();

//     for (String fieldName : config.fields()) {
//       Field f = ReflectionUtils.findField(entity.getClass(), fieldName);
//       ReflectionUtils.makeAccessible(f);

//       Object val = ReflectionUtils.getField(f, entity);

//       if (val != null) {
//         values.add(val.toString().trim());
//       }
//     }
//     return values;
//   }

//   private List<Field> findSlugFields(Class<?> clazz) {
//     return Arrays.stream(clazz.getDeclaredFields())
//         .filter(f -> f.isAnnotationPresent(AutoSlugify.class))
//         .filter(f -> f.getType().equals(String.class))
//         .peek(ReflectionUtils::makeAccessible)
//         .toList();
//   }
// }

// // @Component
// // @RequiredArgsConstructor
// // @Slf4j
// // public class SluggableModelListener extends AbstractMongoEventListener<Sluggable> {

// //   private final MongoTemplate mongoTemplate;

// //   @Override
// //   public void onBeforeConvert(BeforeConvertEvent<Sluggable> event) {
// //     var sluggable = event.getSource();

// //     Class<?> slugableClazz = sluggable.getClass();

// //     Sluggable oldSlugable = null;

// //     for (Field field : slugableClazz.getDeclaredFields()) {
// //       field.setAccessible(true);
// //       AutoSlugify autoSlugify = field.getAnnotation(AutoSlugify.class);
// //       if (autoSlugify != null && field.getType().equals(String.class)) {
// //         UpdateStrategy strategy = autoSlugify.updateStrategy();

// //         boolean haveToUpdate =
// //             ReflectionUtils.getField(field, sluggable) == null
// //                 || sluggable.getId() == null
// //                 || strategy == UpdateStrategy.ON_DOCUMENT_SAVE;

// //         if (!haveToUpdate) {
// //           if (strategy == UpdateStrategy.NEVER_UPDATE) {
// //             continue;
// //           } else if (strategy == UpdateStrategy.ON_VALUE_CHANGE) {
// //             if (oldSlugable == null) {
// //               oldSlugable = (Sluggable) mongoTemplate.findById(sluggable.getId(),
// slugableClazz);
// //               if (oldSlugable == null) {
// //                 haveToUpdate = true;
// //               }
// //             }
// //           }
// //         }

// //         List<String> values = new ArrayList<>();

// //         for (String f : autoSlugify.fields()) {
// //           Field foundField = ReflectionUtils.findField(slugableClazz, f);

// //           if (foundField == null) {
// //             throw new IllegalArgumentException(
// //                 "Cannot find field "
// //                     + f
// //                     + " in class "
// //                     + slugableClazz.getName()
// //                     + "when slugifying for "
// //                     + field.getName());
// //           }

// //           foundField.setAccessible(true);

// //           Object value = ReflectionUtils.getField(foundField, sluggable);

// //           if (haveToUpdate == false) {
// //             Object oldValue = ReflectionUtils.getField(foundField, oldSlugable);
// //             if (value != null && oldValue != null && !oldValue.equals(value)) {
// //               haveToUpdate = true;
// //             }
// //           }
// //           if (value != null) {
// //             values.add(value.toString().trim());
// //           }
// //         }

// //         if (!haveToUpdate) {
// //           continue;
// //         }

// //         log.info(
// //             "Updating slug for field {} in class {} in document {}",
// //             field.getName(),
// //             slugableClazz,
// //             sluggable.getId());

// //         Separator sep = autoSlugify.separator();
// //         String slug =
// //             SlugUtils.createSlug(
// //                 String.join(sep.getValue(), values),
// //                 autoSlugify.unique(),
// //                 autoSlugify.fromUniqueField(),
// //                 sep);

// //         ReflectionUtils.setField(field, sluggable, slug);

// //         // Only uncomment this if use onBeforeSave event instead of onBeforeConvert
// //         // Document document = event.getDocument();
// //         // if (document != null) {
// //         //   document.put(field.getName(), slug);
// //         // }
// //       }
// //     }
// //   }
// // }
