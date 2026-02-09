package com.inlaco.crewmgrservice.infrastructure.persistence.mongo.slug;
// package com.inlaco.crewmgrservice.infrastructure.mongo.slug;

// import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
// import com.github.slugify.Slugify;
// import com.inlaco.crewmgrservice.domain.annotation.AutoSlug;
// import com.inlaco.crewmgrservice.domain.service.SlugGenerator;
// import java.lang.reflect.Field;
// import java.util.*;
// import java.util.concurrent.ConcurrentHashMap;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Component;
// import org.springframework.util.ReflectionUtils;

// @Component
// @Slf4j
// public class MongoSlugGeneratorImpl implements SlugGenerator {

//   private final Map<Class<?>, List<SlugFieldMeta>> cache = new ConcurrentHashMap<>();
//   private final Slugify slugifyHyphen =
//       Slugify.builder().transliterator(true).underscoreSeparator(false).build();
//   private final Slugify slugifyUnderscore =
//       Slugify.builder().transliterator(true).underscoreSeparator(true).build();

//   @Override
//   public void generate(Object entity, Object oldEntity) {
//     List<SlugFieldMeta> metas = cache.computeIfAbsent(entity.getClass(), this::scanMetadata);
//     if (metas.isEmpty()) return;
//     for (SlugFieldMeta meta : metas) {
//       if (shouldUpdate(entity, oldEntity, meta)) {
//         applySlug(entity, meta);
//       }
//     }
//   }

//   /* ---------- core ---------- */

//   private void applySlug(Object entity, SlugFieldMeta meta) {
//     List<String> values = new ArrayList<>();

//     for (Field f : meta.sourceFields) {
//       Object v = ReflectionUtils.getField(f, entity);
//       if (v != null) values.add(v.toString().trim());
//     }

//     if (values.isEmpty()) return;

//     String raw = String.join(meta.config.separator().getValue(), values);

//     String slug =
//         createSlug(
//             raw, meta.config.unique(), meta.config.fromUniqueField(), meta.config.separator());

//     ReflectionUtils.setField(meta.slugField, entity, slug);

//     log.debug("Slug updated: {}.{}", entity.getClass().getSimpleName(),
// meta.slugField.getName());
//   }

//   /* ---------- decision ---------- */

//   private boolean shouldUpdate(Object entity, Object oldEntity, SlugFieldMeta meta) {
//     Object current = ReflectionUtils.getField(meta.slugField, entity);

//     if (current == null || oldEntity == null) return true;

//     return switch (meta.config.updateStrategy()) {
//       case ON_DOCUMENT_SAVE -> true;
//       case NEVER_UPDATE -> false;
//       case ON_VALUE_CHANGE -> valuesChanged(entity, oldEntity, meta);
//     };
//   }

//   private boolean valuesChanged(Object cur, Object old, SlugFieldMeta meta) {
//     for (Field f : meta.sourceFields) {
//       if (!Objects.equals(ReflectionUtils.getField(f, cur), ReflectionUtils.getField(f, old))) {
//         return true;
//       }
//     }
//     return false;
//   }

//   /* ---------- slug ---------- */

//   private String createSlug(
//       String value, boolean unique, boolean fromUniqueField, AutoSlug.Separator separator) {

//     Slugify engine = separator == AutoSlug.Separator.UNDERSCORE ? slugifyUnderscore :
// slugifyHyphen;

//     String slug = engine.slugify(value);

//     if (unique && !fromUniqueField) {
//       slug += separator.getValue();
//       slug +=
//           NanoIdUtils.randomNanoId(
//               NanoIdUtils.DEFAULT_NUMBER_GENERATOR,
//               NanoIdUtils.DEFAULT_ALPHABET,
//               determineNanoIdSize(value.length()));
//     }
//     return slug;
//   }

//   private int determineNanoIdSize(int len) {
//     if (len > 100) return 5;
//     else if (len > 80) return 7;
//     else if (len > 70) return 9;
//     else if (len > 60) return 11;
//     else if (len > 40) return 13;
//     else if (len > 30) return 15;
//     else if (len > 20) return 17;
//     else if (len > 15) return 19;
//     return NanoIdUtils.DEFAULT_SIZE;
//   }

//   /* ---------- scan ---------- */

//   private List<SlugFieldMeta> scanMetadata(Class<?> clazz) {
//     List<SlugFieldMeta> list = new ArrayList<>();

//     for (Field slugField : clazz.getDeclaredFields()) {
//       AutoSlug cfg = slugField.getAnnotation(AutoSlug.class);
//       if (cfg == null || slugField.getType() != String.class) continue;

//       ReflectionUtils.makeAccessible(slugField);

//       List<Field> sources = new ArrayList<>();
//       for (String name : cfg.fields()) {
//         Field f = ReflectionUtils.findField(clazz, name);
//         if (f == null) {
//           throw new IllegalArgumentException(
//               "Field '" + name + "' not found in " + clazz.getName());
//         }
//         ReflectionUtils.makeAccessible(f);
//         sources.add(f);
//       }

//       list.add(new SlugFieldMeta(slugField, cfg, sources));
//     }
//     return list;
//   }
// }
