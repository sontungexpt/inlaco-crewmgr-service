package com.inlaco.crewmgrservice.utils;

import static org.springframework.http.HttpMethod.GET;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inlaco.crewmgrservice.annotation.PublicEndpoint;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Inspects API endpoint security configuration and determines whether a request is public,
 * unsecured, or requires JWT authentication.
 */
@Component
@Slf4j
public class ApiEndpointSecurityInspector {

  /* ==========================================================
   * Internal model
   * ========================================================== */
  @Getter
  @EqualsAndHashCode(of = "path")
  static final class APIPath {
    private static final long serialVersionUID = 1L;

    private final String path;
    private final boolean filterJwt;

    APIPath(String path, boolean filterJwt) {
      this.path = path;
      this.filterJwt = filterJwt;
    }

    @Override
    public String toString() {
      return path;
    }
  }

  /* ==========================================================
   * Fields
   * ========================================================== */
  private final RequestMappingHandlerMapping handlerMapping;
  private final Environment environment;
  private final AntPathMatcher antPathMatcher = new AntPathMatcher();

  /** Public endpoints grouped by HTTP method */
  private final Map<HttpMethod, Set<APIPath>> publicEndpoints = new ConcurrentHashMap<>();

  private final Set<String> staticEndpoints = ConcurrentHashMap.newKeySet();

  public void addPublicEndpoint(String... paths) {
    for (var m : HttpMethod.values()) {
      for (var path : paths) {
        addPublicEndpoint(m, path);
      }
    }
  }

  private void addPublicEndpoint(HttpMethod method, String path) {
    publicEndpoints
        .computeIfAbsent(method, m -> ConcurrentHashMap.newKeySet())
        .add(new APIPath(path, false));
  }

  private void addPublicEndpoint(HttpMethod method, String path, boolean optional) {
    publicEndpoints
        .computeIfAbsent(method, m -> ConcurrentHashMap.newKeySet())
        .add(new APIPath(path, optional));
  }

  /* ==========================================================
   * Constructor
   * ========================================================== */
  public ApiEndpointSecurityInspector(
      RequestMappingHandlerMapping handlerMapping, Environment environment) {

    this.handlerMapping = handlerMapping;
    this.environment = environment;

    for (HttpMethod method : HttpMethod.values()) {
      publicEndpoints.put(method, ConcurrentHashMap.newKeySet());
    }

    // default public endpoints
    addPublicEndpoint("/actuator/**");
    addPublicEndpoint(GET, "/v3/api-docs**/**");
    addPublicEndpoint(GET, "/swagger-ui**/**");
    addPublicEndpoint(GET, "/.well-known**/**");
    addPublicEndpoint(GET, "/favicon.ico");
    addPublicEndpoint(GET, "/test/**");
    addPublicEndpoint(GET, "/api/v1/test/**");
  }

  /* ==========================================================
   * Initialization
   * ========================================================== */
  @PostConstruct
  public void init() {
    Map<RequestMappingInfo, HandlerMethod> mappings = handlerMapping.getHandlerMethods();

    mappings.forEach(this::registerStaticEndpoint);

    mappings.forEach(
        (info, method) -> {
          PublicEndpoint annotation = getAnnotation(method, PublicEndpoint.class);
          if (annotation == null || !profileMatched(annotation, method)) return;
          registerPublicEndpoint(info, annotation.filterJwt());
        });

    logInitializedEndpoints();
  }

  /* ==========================================================
   * Public API
   * ========================================================== */

  /** Public endpoint, JWT optional (filter only) */
  public boolean isOptionalJwtSecurityPath(@NonNull HttpServletRequest request) {
    HttpMethod method = HttpMethod.valueOf(request.getMethod());
    return publicEndpoints.getOrDefault(method, Set.of()).stream()
        .filter(APIPath::isFilterJwt)
        .anyMatch(p -> matchPath(p.getPath(), request));
  }

  /** Public endpoint (no JWT required) */
  public boolean isUnsecureRequest(@NonNull HttpServletRequest request) {
    HttpMethod method = HttpMethod.valueOf(request.getMethod());
    return publicEndpoints.getOrDefault(method, Set.of()).stream()
        .anyMatch(p -> matchPath(p.getPath(), request));
  }

  /** Public endpoint without JWT */
  public boolean isUnsecureJwtRequest(@NonNull HttpServletRequest request) {
    HttpMethod method = HttpMethod.valueOf(request.getMethod());
    return publicEndpoints.getOrDefault(method, Set.of()).stream()
        .filter(p -> !p.isFilterJwt())
        .anyMatch(p -> matchPath(p.getPath(), request));
  }

  public String[] getPublicSecurityPaths(HttpMethod method) {
    return publicEndpoints.getOrDefault(method, Set.of()).stream()
        .map(APIPath::getPath)
        .toArray(String[]::new);
  }

  /* ==========================================================
   * Internal helpers
   * ========================================================== */

  private void registerPublicEndpoint(RequestMappingInfo info, boolean filterJwt) {
    info.getMethodsCondition()
        .getMethods()
        .forEach(
            method ->
                info.getPatternValues()
                    .forEach(path -> addPublicEndpoint(method.asHttpMethod(), path, filterJwt)));
  }

  private void registerStaticEndpoint(RequestMappingInfo info, HandlerMethod method) {
    info.getMethodsCondition()
        .getMethods()
        .forEach(m -> info.getPatternValues().forEach(path -> addStaticEndpoint(path, m.name())));
  }

  private boolean profileMatched(PublicEndpoint annotation, HandlerMethod method) {
    Set<String> activeProfiles = Set.of(environment.getActiveProfiles());

    if (annotation.profiles().length > 0
        && Collections.disjoint(activeProfiles, Arrays.asList(annotation.profiles()))) {
      return false;
    }

    Profile profile = method.getBeanType().getAnnotation(Profile.class);
    if (profile != null && Collections.disjoint(activeProfiles, Arrays.asList(profile.value()))) {
      return false;
    }

    return true;
  }

  @Nullable
  private <A extends Annotation> A getAnnotation(HandlerMethod method, Class<A> clazz) {
    A ann = method.getMethodAnnotation(clazz);
    if (ann != null) return ann;
    return method.getBeanType().getAnnotation(clazz);
  }

  private boolean matchPath(String path, HttpServletRequest request) {
    return matchPath(path, request.getRequestURI(), request.getMethod());
  }

  private boolean matchPath(String pattern, String uri, String method) {
    boolean matched = antPathMatcher.match(pattern, uri);
    if (!isPathVariablePattern(pattern)) return matched;
    return matched && !staticEndpointExists(uri, method);
  }

  private boolean isPathVariablePattern(String path) {
    boolean uriVar = false;
    for (int i = 0; i < path.length(); i++) {
      char c = path.charAt(i);
      if (c == '{') {
        uriVar = true;
      } else if (c == '}' && uriVar) {
        return true;
      }
    }
    return false;
  }

  private void addStaticEndpoint(String path, String methodName) {
    if (antPathMatcher.isPattern(path)) return;
    staticEndpoints.add(prefixByMethodName(path, methodName));
  }

  private boolean staticEndpointExists(String path, String methodName) {
    return staticEndpoints.contains(prefixByMethodName(path, methodName));
  }

  private String prefixByMethodName(String path, String methodName) {
    return methodName + "_" + path;
  }

  private void logInitializedEndpoints() {
    try {
      log.info(
          "Public endpoints initialized:\n{}",
          new ObjectMapper()
              .writerWithDefaultPrettyPrinter()
              .writeValueAsString(
                  publicEndpoints.entrySet().stream()
                      .collect(
                          Collectors.toMap(
                              e -> e.getKey().name(),
                              e ->
                                  e.getValue().stream()
                                      .map(APIPath::toString)
                                      .collect(Collectors.toList())))));
    } catch (Exception e) {
      log.info("Public endpoints initialized: {}", publicEndpoints);
    }
  }
}
