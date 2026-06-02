package com.inlaco.crewmgrservice.infrastructure.web.resolver;

import com.inlaco.crewmgrservice.infrastructure.web.annotation.Filter;
import java.lang.reflect.*;
import java.time.temporal.Temporal;
import java.util.*;
import lombok.AllArgsConstructor;
import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@AllArgsConstructor
public class FilterArgumentResolver implements HandlerMethodArgumentResolver {

  private final ConversionService filterConversionService;
  private final Map<Class<?>, ClassMeta> metaCache = new ConcurrentReferenceHashMap<>(256);

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(Filter.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    Filter filter = parameter.getParameterAnnotation(Filter.class);
    String prefix = filter.value();
    if (prefix != null && !prefix.isBlank()) prefix += '.';
    return bind(parameter.getParameterType(), prefix, webRequest);
  }

  // =========================================================
  // CORE
  // =========================================================

  private Object bind(Class<?> clazz, String prefix, NativeWebRequest req) {

    ClassMeta meta = getMeta(clazz);

    if (meta.record) {
      return bindRecord(meta, prefix, req);
    }

    return bindPojo(meta, prefix, req);
  }

  // =========================================================
  // POJO
  // =========================================================

  private Object bindPojo(ClassMeta meta, String prefix, NativeWebRequest req) {

    Object instance;
    try {
      instance = meta.constructor.newInstance();
    } catch (Exception e) {
      throw new IllegalStateException("Cannot instantiate " + meta.type, e);
    }

    boolean touched = false;

    for (PropertyMeta prop : meta.properties) {

      String paramName = prefix + prop.name;

      try {

        // 1️⃣ Collection
        if (prop.collection) {
          String[] rawValues = getParameterValues(req, paramName);
          if (rawValues == null || rawValues.length == 0) continue;

          Object converted = filterConversionService.convert(List.of(rawValues), prop.type);

          if (converted == null) continue;

          prop.set(instance, converted);
          touched = true;
          continue;
        }

        // 2️⃣ Simple
        if (prop.simple) {
          String rawValue = req.getParameter(paramName);
          if (rawValue == null || rawValue.isBlank()) continue;

          Object converted = filterConversionService.convert(rawValue, prop.type);
          if (converted == null) continue;

          prop.set(instance, converted);
          touched = true;
          continue;
        }

        // 3️⃣ Nested
        String nestedPrefix = paramName + ".";
        if (!hasAnyParam(req, nestedPrefix)) continue;

        Object nested = bind(prop.type, nestedPrefix, req);
        if (nested != null) {
          prop.set(instance, nested);
          touched = true;
        }

      } catch (Exception e) {
        throw new IllegalStateException("Failed to bind field: " + paramName, e);
      }
    }

    return touched ? instance : null;
  }

  // =========================================================
  // RECORD
  // =========================================================

  private Object bindRecord(ClassMeta meta, String prefix, NativeWebRequest req) {

    Object[] args = new Object[meta.properties.size()];
    boolean touched = false;

    for (int i = 0; i < meta.properties.size(); i++) {

      PropertyMeta prop = meta.properties.get(i);
      String paramName = prefix + prop.name;

      try {
        // 1️⃣ Collection
        if (prop.collection) {
          String[] rawValues = getParameterValues(req, paramName);

          if (rawValues == null || rawValues.length == 0) {
            args[i] = null;
            continue;
          }

          args[i] = filterConversionService.convert(List.of(rawValues), prop.type);
          touched = true;
          continue;
        }

        // 2️⃣ Simple
        if (prop.simple) {
          String rawValue = req.getParameter(paramName);
          if (rawValue == null || rawValue.isBlank()) {
            args[i] = null;
            continue;
          }

          args[i] = filterConversionService.convert(rawValue, prop.type);
          touched = true;
          continue;
        }

        // 3️⃣ Nested
        String nestedPrefix = paramName + ".";
        if (!hasAnyParam(req, nestedPrefix)) {
          args[i] = null;
          continue;
        }

        Object nested = bind(prop.type, nestedPrefix, req);
        args[i] = nested;
        touched = true;

      } catch (Exception e) {
        throw new IllegalStateException("Failed to bind record field: " + paramName, e);
      }
    }

    if (!touched) return null;

    try {
      return meta.constructor.newInstance(args);
    } catch (Exception e) {
      throw new IllegalStateException("Cannot create record: " + meta.type, e);
    }
  }

  // =========================================================
  // META CACHE
  // =========================================================

  private ClassMeta getMeta(Class<?> clazz) {
    return metaCache.computeIfAbsent(clazz, this::buildMeta);
  }

  private ClassMeta buildMeta(Class<?> clazz) {

    try {

      if (clazz.isRecord()) {

        RecordComponent[] components = clazz.getRecordComponents();
        List<PropertyMeta> props = new ArrayList<>(components.length);

        for (RecordComponent rc : components) {
          props.add(PropertyMeta.fromRecord(rc));
        }

        Constructor<?> ctor =
            clazz.getDeclaredConstructor(
                Arrays.stream(components).map(RecordComponent::getType).toArray(Class[]::new));

        ReflectionUtils.makeAccessible(ctor);

        return new ClassMeta(clazz, true, props, ctor);
      }

      // POJO
      List<PropertyMeta> props = new ArrayList<>();

      ReflectionUtils.doWithFields(
          clazz,
          field -> {
            ReflectionUtils.makeAccessible(field);
            props.add(PropertyMeta.fromField(field));
          });

      Constructor<?> ctor = clazz.getDeclaredConstructor();
      ReflectionUtils.makeAccessible(ctor);

      return new ClassMeta(clazz, false, props, ctor);

    } catch (Exception e) {
      throw new IllegalStateException("Failed building meta for " + clazz, e);
    }
  }

  // =========================================================
  // META STRUCTURES
  // =========================================================

  static record ClassMeta(
      Class<?> type, boolean record, List<PropertyMeta> properties, Constructor<?> constructor) {}

  static record PropertyMeta(
      String name, Class<?> type, boolean simple, boolean collection, Field field) {
    static PropertyMeta fromField(Field field) {
      Class<?> type = field.getType();
      return new PropertyMeta(
          field.getName(),
          type,
          isSimpleType(type),
          Collection.class.isAssignableFrom(type),
          field);
    }

    static PropertyMeta fromRecord(RecordComponent rc) {
      Class<?> type = rc.getType();
      return new PropertyMeta(
          rc.getName(), type, isSimpleType(type), Collection.class.isAssignableFrom(type), null);
    }

    void set(Object target, Object value) throws IllegalAccessException {
      if (field != null) {
        field.set(target, value);
      }
    }
  }

  // =========================================================
  // HELPERS
  // =========================================================
  private String[] getParameterValues(NativeWebRequest req, String name) {
    return Optional.ofNullable(req.getParameterValues(name))
        .orElseGet(() -> req.getParameterValues(name + "[]"));
  }

  private boolean hasAnyParam(NativeWebRequest req, String prefix) {
    return req.getParameterMap().keySet().stream().anyMatch(k -> k.startsWith(prefix));
  }

  private static boolean isSimpleType(Class<?> type) {
    return type.isPrimitive()
        || type == String.class
        || type == Character.class
        || type == Boolean.class
        || Number.class.isAssignableFrom(type)
        || Enum.class.isAssignableFrom(type)
        || Temporal.class.isAssignableFrom(type)
        || java.time.temporal.TemporalAmount.class.isAssignableFrom(type)
        || java.util.Date.class.isAssignableFrom(type)
        || java.util.Calendar.class.isAssignableFrom(type)
        || type == com.aventrix.jnanoid.jnanoid.NanoIdUtils.class
        || type == java.net.URI.class
        || type == java.net.URL.class;
  }
}
