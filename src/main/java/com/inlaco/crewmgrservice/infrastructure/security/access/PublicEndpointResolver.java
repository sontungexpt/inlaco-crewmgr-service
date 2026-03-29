package com.inlaco.crewmgrservice.infrastructure.security.access;

import com.inlaco.crewmgrservice.infrastructure.web.annotation.PublicEndpoint;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.PublicEndpoint.AuthMode;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

@Component
@Slf4j
public class PublicEndpointResolver {

  private final record APIPath(PathPattern pattern, boolean dynamic, boolean authOptional) {}

  private final Environment environment;

  /* ==========================================================
   * Fields
   * ========================================================== */
  private final PathPatternParser PATH_PARSER = new PathPatternParser();
  private final Map<HttpMethod, Set<APIPath>> publicEndpoints = new ConcurrentHashMap<>();
  private final Map<HttpMethod, Set<PathPattern>> staticEndpoints = new ConcurrentHashMap<>();

  /* ==========================================================
   * Constructor
   * ========================================================== */
  public PublicEndpointResolver(
      RequestMappingHandlerMapping handlerMapping, Environment environment) {
    this.environment = environment;

    Map<RequestMappingInfo, HandlerMethod> mappings = handlerMapping.getHandlerMethods();
    mappings.forEach(this::registerStaticEndpoint);

    // Scan @PublicEndpoint
    mappings.forEach(
        (info, method) -> {
          PublicEndpoint annotation = resolveAnnotation(method);
          if (annotation == null) return;
          registerPublicEndpoint(info, annotation.auth() == AuthMode.OPTIONAL);
        });

    logInitializedEndpoints();
  }

  /* ==========================================================
   * Public API
   * ========================================================== */

  /** Public endpoint (no JWT required) */
  public boolean isPublic(@NonNull HttpServletRequest request) {
    HttpMethod method = HttpMethod.valueOf(request.getMethod());
    return publicEndpoints.getOrDefault(method, Set.of()).stream()
        .anyMatch(p -> matchPath(p, request));
  }

  public boolean isFullyPublic(@NonNull HttpServletRequest request) {
    HttpMethod method = HttpMethod.valueOf(request.getMethod());
    String uri = request.getRequestURI();

    return publicEndpoints.getOrDefault(method, Set.of()).stream()
        .filter(p -> !p.authOptional())
        .anyMatch(
            p -> {
              boolean match = matchPath(p, request);
              if (match) {
                log.debug(
                    "[MATCH] PUBLIC (no JWT): {} {} -> pattern={}",
                    method,
                    uri,
                    p.pattern().getPatternString());
              }
              return match;
            });
  }

  /* ==========================================================
   * Internal helpers
   * ========================================================== */

  private void registerPublicEndpoint(
      @Nullable HttpMethod method, String path, boolean authOptional) {
    if (method == null) {
      for (HttpMethod m : HttpMethod.values()) {
        registerPublicEndpoint(m, path, authOptional);
      }
      return;
    }

    PathPattern pattern = PATH_PARSER.parse(path);
    boolean dynamic = pattern.hasPatternSyntax();

    log.debug(
        "[REGISTER] PUBLIC endpoint: {} {} (dynamic={}, authOptional={})",
        method,
        path,
        dynamic,
        authOptional);

    publicEndpoints
        .computeIfAbsent(method, m -> ConcurrentHashMap.newKeySet())
        .add(new APIPath(pattern, dynamic, authOptional));
  }

  private void registerPublicEndpoint(RequestMappingInfo info, boolean authOptional) {
    info.getMethodsCondition()
        .getMethods()
        .forEach(
            method ->
                info.getPatternValues()
                    .forEach(
                        path -> registerPublicEndpoint(method.asHttpMethod(), path, authOptional)));
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

  private boolean matchPath(APIPath apiPath, HttpServletRequest request) {
    String uri = request.getRequestURI();
    PathPattern pattern = apiPath.pattern();

    boolean matched = pattern.matches(PathContainer.parsePath(uri));

    log.trace(
        "[MATCH-TRY] pattern={} uri={} matched={} dynamic={}",
        pattern.getPatternString(),
        uri,
        matched,
        apiPath.dynamic());

    if (!matched) return false;

    // Static path → OK
    if (!apiPath.dynamic()) {
      log.trace("[MATCH] STATIC pattern accepted: {}", pattern.getPatternString());
      return true;
    }

    boolean staticExists = staticEndpointExists(request);

    log.trace(
        "[MATCH] DYNAMIC pattern={}, staticExists={}", pattern.getPatternString(), staticExists);

    return !staticExists;
  }

  private boolean staticEndpointExists(HttpServletRequest request) {
    HttpMethod method = HttpMethod.valueOf(request.getMethod());
    String uri = request.getRequestURI();

    PathContainer path = PathContainer.parsePath(uri);

    boolean found =
        staticEndpoints.getOrDefault(method, Set.of()).stream()
            .anyMatch(
                p -> {
                  boolean match = p.matches(path);
                  if (match) {
                    log.trace(
                        "[STATIC-OVERRIDE] uri={} matched static pattern ={}",
                        uri,
                        p.getPatternString());
                  }
                  return match;
                });

    return found;
  }

  private void logInitializedEndpoints() {
    StringBuilder sb = new StringBuilder(1024);

    sb.append("\n================ SECURITY ENDPOINTS ================\n");

    /* ================= STATIC ================= */

    sb.append("\n[STATIC ENDPOINTS]\n");

    List<HttpMethod> staticMethods = new ArrayList<>(staticEndpoints.keySet());
    Collections.sort(staticMethods);

    for (HttpMethod method : staticMethods) {

      sb.append(method.name()).append(":\n");

      Set<PathPattern> patternsSet = staticEndpoints.get(method);
      if (patternsSet == null || patternsSet.isEmpty()) {
        continue;
      }

      List<String> patterns = new ArrayList<>(patternsSet.size());

      for (PathPattern pattern : patternsSet) {
        patterns.add(pattern.getPatternString());
      }

      Collections.sort(patterns);

      for (String pattern : patterns) {
        sb.append("  - ").append(pattern).append('\n');
      }
    }

    /* ================= PUBLIC ================= */

    sb.append("\n[PUBLIC ENDPOINTS]\n");

    List<HttpMethod> publicMethods = new ArrayList<>(publicEndpoints.keySet());
    Collections.sort(publicMethods);

    for (HttpMethod method : publicMethods) {

      sb.append(method.name()).append(":\n");

      Set<APIPath> apiPaths = publicEndpoints.get(method);
      if (apiPaths == null || apiPaths.isEmpty()) {
        continue;
      }

      List<APIPath> sortedPaths = new ArrayList<>(apiPaths);
      sortedPaths.sort(
          new Comparator<APIPath>() {
            @Override
            public int compare(APIPath a, APIPath b) {
              return a.pattern().getPatternString().compareTo(b.pattern().getPatternString());
            }
          });

      for (APIPath apiPath : sortedPaths) {
        sb.append("  - ")
            .append(apiPath.pattern().getPatternString())
            .append(apiPath.authOptional() ? " [PUBLIC + OPTIONAL JWT]" : " [PUBLIC]")
            .append('\n');
      }
    }

    sb.append("====================================================\n");

    log.info(sb.toString());
  }

  /* ==========================================================
   * Core resolve logic
   * ========================================================== */

  @Nullable
  public PublicEndpoint resolveAnnotation(HandlerMethod hm) {
    PublicEndpoint pe = hm.getMethodAnnotation(PublicEndpoint.class);
    if (pe == null) pe = hm.getBeanType().getAnnotation(PublicEndpoint.class);
    if (pe == null) return null;
    return isProfileMatched(pe) ? pe : null;
  }

  private boolean isProfileMatched(PublicEndpoint annotation) {
    String[] activeProfiles = environment.getActiveProfiles();

    if (annotation.profiles().length == 0) {
      return true;
    }

    for (String required : annotation.profiles()) {
      for (String active : activeProfiles) {
        if (required.equals(active)) {
          return true;
        }
      }
    }

    return false;
  }
}
