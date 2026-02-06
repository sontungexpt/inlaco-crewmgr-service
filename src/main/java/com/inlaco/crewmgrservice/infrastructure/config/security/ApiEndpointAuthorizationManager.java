package com.inlaco.crewmgrservice.infrastructure.config.security;

import static org.springframework.http.HttpMethod.GET;

import com.inlaco.crewmgrservice.infrastructure.web.annotation.PublicEndpoint;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

@Component
@Slf4j
public class ApiEndpointAuthorizationManager
    implements AuthorizationManager<RequestAuthorizationContext> {

  // @Override
  // public @org.jspecify.annotations.Nullable AuthorizationResult authorize(
  //     Supplier<Authentication> authentication,
  //     RequestAuthorizationContext context) {
  //   HttpServletRequest request = context.getRequest();

  //   // Public endpoints
  //   if (isUnsecureJwtRequest(request)) {
  //     return new AuthorizationDecision(true);
  //   }

  //   // Optional JWT
  //   if (isOptionalJwtSecurityPath(request)) {
  //     return new AuthorizationDecision(true);
  //   }

  //   // Auth required
  //   Authentication auth = authentication.get();
  //   boolean granted =
  //       auth != null && auth.isAuthenticated() && !(auth instanceof
  // AnonymousAuthenticationToken);

  //   return new AuthorizationDecision(granted);
  // }

  @Override
  public @org.jspecify.annotations.Nullable AuthorizationResult authorize(
      Supplier<? extends @org.jspecify.annotations.Nullable Authentication> authentication,
      RequestAuthorizationContext context) {
    HttpServletRequest request = context.getRequest();

    // Public endpoints
    if (isUnsecureJwtRequest(request)) {
      return new AuthorizationDecision(true);
    }

    // Optional JWT
    if (isOptionalJwtSecurityPath(request)) {
      return new AuthorizationDecision(true);
    }

    // Auth required
    Authentication auth = authentication.get();
    boolean granted =
        auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);

    return new AuthorizationDecision(granted);
  }

  /* ==========================================================
   * Internal model
   * ========================================================== */
  @Getter
  @EqualsAndHashCode(of = "pattern")
  @ToString(of = "pattern")
  @AllArgsConstructor
  private static final class APIPath {
    private final PathPattern pattern;
    private final boolean dynamic;
    private final boolean filterJwt;
  }

  /* ==========================================================
   * Fields
   * ========================================================== */
  private final Environment environment;
  private static final PathPatternParser PATH_PARSER = new PathPatternParser();
  private static final Map<HttpMethod, Set<APIPath>> publicEndpoints = new ConcurrentHashMap<>();
  private static final Map<HttpMethod, Set<PathPattern>> staticEndpoints =
      new ConcurrentHashMap<>();

  /* ==========================================================
   * Constructor
   * ========================================================== */
  public ApiEndpointAuthorizationManager(
      RequestMappingHandlerMapping handlerMapping, Environment environment) {

    // this.handlerMapping = handlerMapping;
    this.environment = environment;

    // default public endpoints
    registerPublicEndpoint(null, "/actuator/**", false); // tất cả method
    registerPublicEndpoint(GET, "/v3/api-docs**/**", false);
    registerPublicEndpoint(GET, "/swagger-ui**/**", false);
    registerPublicEndpoint(GET, "/.well-known**/**", false);
    registerPublicEndpoint(GET, "/favicon.ico", false);
    registerPublicEndpoint(GET, "/test/**", false);
    registerPublicEndpoint(GET, "/api/v1/test/**", false);

    // register static endpoints that manually added
    publicEndpoints.forEach(
        (method, apiPaths) ->
            apiPaths.forEach(apiPath -> registerStaticEndpoint(apiPath.getPattern(), method)));

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
        .anyMatch(p -> matchPath(p, request));
  }

  /** Public endpoint (no JWT required) */
  public boolean isUnsecureRequest(@NonNull HttpServletRequest request) {
    HttpMethod method = HttpMethod.valueOf(request.getMethod());
    return publicEndpoints.getOrDefault(method, Set.of()).stream()
        .anyMatch(p -> matchPath(p, request));
  }

  /** Public endpoint without JWT */
  public boolean isUnsecureJwtRequest(@NonNull HttpServletRequest request) {
    HttpMethod method = HttpMethod.valueOf(request.getMethod());

    return publicEndpoints.getOrDefault(method, Set.of()).stream()
        .filter(p -> !p.isFilterJwt())
        .anyMatch(p -> matchPath(p, request));
  }

  /* ==========================================================
   * Internal helpers
   * ========================================================== */

  private void registerPublicEndpoint(@Nullable HttpMethod method, String path, boolean filterJwt) {

    if (method == null) {
      for (HttpMethod m : HttpMethod.values()) {
        registerPublicEndpoint(m, path, filterJwt);
      }
      return;
    }
    PathPattern pattern = PATH_PARSER.parse(path);
    boolean dynamic = pattern.hasPatternSyntax();

    publicEndpoints
        .computeIfAbsent(method, m -> ConcurrentHashMap.newKeySet())
        .add(new APIPath(pattern, dynamic, filterJwt));
  }

  private void registerPublicEndpoint(RequestMappingInfo info, boolean filterJwt) {
    info.getMethodsCondition()
        .getMethods()
        .forEach(
            method ->
                info.getPatternValues()
                    .forEach(
                        path -> registerPublicEndpoint(method.asHttpMethod(), path, filterJwt)));
  }

  private void registerStaticEndpoint(PathPattern pattern, HttpMethod method) {
    if (pattern.hasPatternSyntax()) return;
    staticEndpoints.computeIfAbsent(method, m -> ConcurrentHashMap.newKeySet()).add(pattern);
    log.trace("[SECURITY] Static endpoint registered: {} {}", method, pattern);
  }

  private void registerStaticEndpoint(RequestMappingInfo info, HandlerMethod method) {
    info.getMethodsCondition()
        .getMethods()
        .forEach(
            m ->
                info.getPatternValues()
                    .forEach(
                        path -> registerStaticEndpoint(PATH_PARSER.parse(path), m.asHttpMethod())));
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

  // private boolean matchPath(String pattern, String uri, String method) {
  //   boolean matched = antPathMatcher.match(pattern, uri);
  //   if (!isPathVariablePattern(pattern)) return matched;
  //   return matched && !staticEndpointExists(uri, method);
  // }
  //
  private boolean matchPath(APIPath apiPath, HttpServletRequest request) {
    PathPattern pattern = apiPath.getPattern();
    boolean matched = pattern.matches(PathContainer.parsePath(request.getRequestURI()));
    if (!matched) return false;
    // No {var} → match immediately
    else if (!apiPath.isDynamic()) {
      return true;
    }
    // Has {var} → check static
    return !staticEndpointExists(request);
  }

  private boolean staticEndpointExists(HttpServletRequest request) {
    HttpMethod method = HttpMethod.valueOf(request.getMethod());
    PathContainer path = PathContainer.parsePath(request.getRequestURI());
    return staticEndpoints.getOrDefault(method, Set.of()).stream().anyMatch(p -> p.matches(path));
  }

  private void logInitializedEndpoints() {

    log.info(
        "Registered static endpoints:\n{}",
        staticEndpoints.entrySet().stream()
            .map(
                e ->
                    e.getKey().name()
                        + ":\n"
                        + e.getValue().stream()
                            .map(p -> "  - " + p.getPatternString())
                            .sorted()
                            .collect(Collectors.joining("\n")))
            .collect(Collectors.joining("\n")));

    StringBuilder publicLog = new StringBuilder();
    publicLog.append("[SECURITY] Public endpoints initialized:\n");

    for (Map.Entry<HttpMethod, Set<APIPath>> entry : publicEndpoints.entrySet()) {
      publicLog.append("  ").append(entry.getKey().name()).append(":\n");
      for (APIPath apiPath : entry.getValue()) {
        publicLog
            .append("    - ")
            .append(apiPath.getPattern().getPatternString())
            .append(apiPath.isFilterJwt() ? " [PUBLIC + OPTIONAL JWT]" : " [PUBLIC]")
            .append("\n");
      }
    }

    log.info(publicLog.toString());
  }
}
