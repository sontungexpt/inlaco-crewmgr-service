package com.inlaco.crewmgrservice.resolver;

import com.inlaco.crewmgrservice.annotation.FilterGroup;
import com.inlaco.crewmgrservice.common.payload.Filterable;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * {@code FilterableArgumentResolver} is a {@link HandlerMethodArgumentResolver} responsible for
 * binding HTTP query parameters into a filter object that implements {@link Filterable}.
 *
 * <p>This resolver enables flexible and type-safe filtering for REST APIs without manually
 * extracting query parameters in controllers.
 *
 * <h2>1. Applicability</h2>
 *
 * <ul>
 *   <li>The controller parameter must implement {@link Filterable}
 *   <li>The parameter may optionally be annotated with {@link FilterGroup} to define a custom query
 *       prefix
 * </ul>
 *
 * <h2>2. General Conventions</h2>
 *
 * <ul>
 *   <li>Default query prefix: {@code filter}
 *   <li>Query parameter format: {@code &lt;prefix&gt;.&lt;field&gt;}
 *   <li>Nested objects use dot-notation: {@code parent.child.field}
 *   <li>Multi-value query parameters are mapped to {@link Collection} fields
 * </ul>
 *
 * <h2>3. Example: Simple (Flat) Filter Object</h2>
 *
 * <h3>Filter class</h3>
 *
 * <pre>{@code
 * public class UserFilter implements Filterable {
 *   private String name;
 *   private Integer age;
 *   private Boolean active;
 * }
 * }</pre>
 *
 * <h3>Controller usage</h3>
 *
 * <pre>{@code
 * @GetMapping("/users")
 * public Page<UserDto> search(@FilterGroup("filter") UserFilter filter) {
 *   return userService.search(filter);
 * }
 * }</pre>
 *
 * <h3>HTTP request</h3>
 *
 * <pre>
 * GET /users?filter.name=John&filter.age=30&filter.active=true
 * </pre>
 *
 * <h3>Resulting object</h3>
 *
 * <pre>{@code
 * UserFilter {
 *   name = "John"
 *   age = 30
 *   active = true
 * }
 * }</pre>
 *
 * <h2>4. Example: Collection-based Filter</h2>
 *
 * <h3>Filter class</h3>
 *
 * <pre>{@code
 * public class UserFilter implements Filterable {
 *   private List<Long> roleIds;
 * }
 * }</pre>
 *
 * <h3>HTTP request</h3>
 *
 * <pre>
 * GET /users?filter.roleIds=1&filter.roleIds=2&filter.roleIds=3
 * </pre>
 *
 * <h3>Resulting object</h3>
 *
 * <pre>{@code
 * roleIds = [1, 2, 3]
 * }</pre>
 *
 * <h2>5. Example: Nested Filter Objects</h2>
 *
 * <p>Nested filter objects are supported using dot-notation. A nested object is instantiated
 * <strong>only if</strong> at least one of its fields receives a value.
 *
 * <h3>Filter classes</h3>
 *
 * <pre>{@code
 * public class UserFilter implements Filterable {
 *   private String name;
 *   private ProfileFilter profile;
 * }
 *
 * public class ProfileFilter implements Filterable {
 *   private String email;
 *   private Instant createdFrom;
 * }
 * }</pre>
 *
 * <h3>HTTP request</h3>
 *
 * <pre>
 * GET /users
 *   ?filter.name=John
 *   &filter.profile.email=john@mail.com
 *   &filter.profile.createdFrom=2024-01-01T00:00:00Z
 * </pre>
 *
 * <h3>Resulting object</h3>
 *
 * <pre>{@code
 * UserFilter {
 *   name = "John"
 *   profile = ProfileFilter {
 *     email = "john@mail.com"
 *     createdFrom = 2024-01-01T00:00:00Z
 *   }
 * }
 * }</pre>
 *
 * <h2>6. Special Behavior</h2>
 *
 * <ul>
 *   <li>If a filter object (or nested filter object) has no bound fields, it will be resolved as
 *       {@code null}
 *   <li>Type conversion is handled by Spring {@link ConversionService}
 *   <li>The following "simple" types are supported:
 *       <ul>
 *         <li>Primitive types and wrappers
 *         <li>{@link String}, {@link Character}, {@link Boolean}
 *         <li>{@link Number} and its subclasses
 *         <li>{@link Enum}
 *         <li>{@link java.time.temporal.Temporal} (Instant, LocalDate, ...)
 *         <li>{@link java.time.temporal.TemporalAmount} (Duration, Period)
 *         <li>{@link java.util.Date}, {@link java.util.Calendar}
 *         <li>{@link java.util.UUID}
 *         <li>{@link java.net.URI}, {@link java.net.URL}
 *       </ul>
 * </ul>
 *
 * <h2>7. Design Goals</h2>
 *
 * <ul>
 *   <li>Eliminate boilerplate parameter parsing in controllers
 *   <li>Provide a consistent filtering convention across APIs
 *   <li>Serve as a foundation for dynamic queries (Specification, Criteria API, QueryDSL)
 * </ul>
 */
@Component
public class FilterableArgumentResolver implements HandlerMethodArgumentResolver {

  private final ConversionService conversionService;

  public FilterableArgumentResolver(
      @Qualifier("filterConversionService") ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return Filterable.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory)
      throws Exception {

    FilterGroup group = parameter.getParameterAnnotation(FilterGroup.class);
    String prefix = (group != null ? group.value() : FilterGroup.DEFAULT_PREFIX) + ".";

    return bind(parameter.getParameterType(), prefix, webRequest);
  }

  // ========================= CORE =========================

  private Object bind(Class<?> clazz, String prefix, NativeWebRequest webRequest) throws Exception {

    Object instance = clazz.getDeclaredConstructor().newInstance();
    boolean hasValue = false;

    for (var field : clazz.getDeclaredFields()) {
      field.setAccessible(true);
      String paramName = prefix + field.getName();
      Class<?> fieldType = field.getType();

      // 1️⃣ Collection
      if (Collection.class.isAssignableFrom(fieldType)) {
        String[] rawValues = webRequest.getParameterValues(paramName);
        if (rawValues == null || rawValues.length == 0) continue;
        Object converted = conversionService.convert(List.of(rawValues), field.getType());
        if (converted == null) continue;
        field.set(instance, converted);
        hasValue = true;
        continue;
      }

      // 2️⃣ Simple value (String, Number, Instant, Enum, ...)
      if (isSimple(fieldType)) {
        String rawValue = webRequest.getParameter(paramName);
        if (rawValue == null || rawValue.isBlank()) continue;
        Object converted = conversionService.convert(rawValue, fieldType);
        if (converted == null) continue;

        field.set(instance, converted);
        hasValue = true;
        continue;
      }
      // 3️⃣ Nested object (ONLY real objects)
      Object nested = bind(fieldType, paramName + ".", webRequest);
      if (nested != null) {
        field.set(instance, nested);
        hasValue = true;
      }
    }

    // instance.set("isFilterable", true)

    return instance;
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
