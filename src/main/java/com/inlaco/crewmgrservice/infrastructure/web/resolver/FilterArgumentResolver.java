package com.inlaco.crewmgrservice.infrastructure.web.resolver;

import com.inlaco.crewmgrservice.infrastructure.web.annotation.Filter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class FilterArgumentResolver implements HandlerMethodArgumentResolver {

  private final ConversionService conversionService;

  static record FieldMeta(Field field, Class<?> type, boolean simple, boolean collection) {}

  private final Map<Class<?>, List<FieldMeta>> fieldCache = new ConcurrentReferenceHashMap<>(256);
  private final Map<Class<?>, Constructor<?>> ctorCache = new ConcurrentReferenceHashMap<>(256);

  public FilterArgumentResolver(
      @Qualifier("filterConversionService") ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(Filter.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory)
      throws Exception {
    Filter filter = parameter.getParameterAnnotation(Filter.class);

    return bind(parameter.getParameterType(), filter.value(), webRequest);
  }

  // ========================= CORE =========================
  private Object bind(Class<?> clazz, String prefix, NativeWebRequest req) {
    Object instance = newInstance(clazz);
    boolean touched = false;

    for (FieldMeta meta : getFields(clazz)) {
      String paramName = prefix + meta.field.getName();

      try {
        // 1️⃣ Collection
        if (meta.collection) {
          String[] rawValues = req.getParameterValues(paramName);
          if (rawValues == null || rawValues.length == 0) continue;

          Object converted = conversionService.convert(List.of(rawValues), meta.type);
          if (converted == null) continue;

          meta.field.set(instance, converted);
          touched = true;
          continue;
        }

        // 2️⃣ Simple value
        if (meta.simple) {
          String rawValue = req.getParameter(paramName);
          if (rawValue == null || rawValue.isBlank()) continue;

          Object converted = conversionService.convert(rawValue, meta.type);
          if (converted == null) continue;

          meta.field.set(instance, converted);
          touched = true;
          continue;
        }

        // 3️⃣ Nested object (lazy)
        String nestedPrefix = paramName + ".";
        if (!hasAnyParam(req, nestedPrefix)) continue;

        Object nested = bind(meta.type, nestedPrefix, req);
        if (nested != null) {
          meta.field.set(instance, nested);
          touched = true;
        }

      } catch (Exception e) {
        throw new IllegalStateException("Failed to bind filter field: " + paramName, e);
      }
    }

    return touched ? instance : null;
  }

  private List<FieldMeta> getFields(Class<?> clazz) {
    return fieldCache.computeIfAbsent(
        clazz,
        c -> {
          List<FieldMeta> metas = new ArrayList<>();
          ReflectionUtils.doWithFields(
              c,
              f -> {
                Class<?> fieldType = f.getType();
                ReflectionUtils.makeAccessible(f);
                metas.add(
                    new FieldMeta(
                        f,
                        fieldType,
                        isSimple(fieldType),
                        Collection.class.isAssignableFrom(fieldType)));
              });
          return metas;
        });
  }

  private Object newInstance(Class<?> clazz) {
    try {
      Constructor<?> ctor =
          ctorCache.computeIfAbsent(
              clazz,
              c -> {
                try {
                  Constructor<?> ct = c.getDeclaredConstructor();
                  ct.setAccessible(true);
                  return ct;
                } catch (Exception e) {
                  throw new IllegalStateException("No default constructor for " + c, e);
                }
              });
      return ctor.newInstance();
    } catch (Exception e) {
      throw new IllegalStateException("Cannot create instance of " + clazz, e);
    }
  }

  // ========================= HELPERS =========================

  private boolean hasAnyParam(NativeWebRequest req, String prefix) {
    return req.getParameterMap().keySet().stream().anyMatch(k -> k.startsWith(prefix));
  }

  private boolean isSimple(Class<?> type) {
    return type.isPrimitive()
        || type == String.class
        || type == Character.class
        || type == Boolean.class
        || Number.class.isAssignableFrom(type)
        || Enum.class.isAssignableFrom(type)
        || Temporal.class.isAssignableFrom(type) // Instant, LocalDate...
        || java.time.temporal.TemporalAmount.class.isAssignableFrom(type) // Duration, Period
        || java.util.Date.class.isAssignableFrom(type)
        || java.util.Calendar.class.isAssignableFrom(type)
        || type == java.util.UUID.class
        || type == java.net.URI.class
        || type == java.net.URL.class;
  }
}
