// package com.inlaco.crewmgrservice.infrastructure.security.config;

// import static org.springframework.http.HttpMethod.GET;

// import com.inlaco.crewmgrservice.infrastructure.web.annotation.PublicEndpoint;
// import jakarta.servlet.http.HttpServletRequest;
// import java.lang.annotation.Annotation;
// import java.util.*;
// import java.util.concurrent.ConcurrentHashMap;
// import java.util.function.Supplier;
// import lombok.extern.slf4j.Slf4j;
// import org.jspecify.annotations.NonNull;
// import org.jspecify.annotations.Nullable;
// import org.springframework.context.annotation.Profile;
// import org.springframework.core.env.Environment;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.server.PathContainer;
// import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
// import org.springframework.security.authorization.AuthorizationDecision;
// import org.springframework.security.authorization.AuthorizationManager;
// import org.springframework.security.authorization.AuthorizationResult;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
// import
// org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;
// import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
// import org.springframework.web.method.HandlerMethod;
// import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
// import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
// import org.springframework.web.util.pattern.PathPattern;
// import org.springframework.web.util.pattern.PathPatternParser;

// @Slf4j
// public class ApiEndpointAuthorizationManagerNew
//     implements AuthorizationManager<RequestAuthorizationContext> {

//   /* ========================================================== */
//   /* Internal model                                             */
//   /* ========================================================== */

//   private final record APIPath(PathPattern pattern, boolean dynamic, boolean filterJwt) {}

//   /* ========================================================== */
//   /* Fields                                                     */
//   /* ========================================================== */

//   private final Environment environment;
//   private final PathPatternParser PATH_PARSER = new PathPatternParser();

//   private final Map<HttpMethod, Set<APIPath>> publicEndpoints = new ConcurrentHashMap<>();
//   private final Map<HttpMethod, Set<PathPattern>> staticEndpoints = new ConcurrentHashMap<>();

//   private final RequestMatcherDelegatingAuthorizationManager delegate;

//   /* ========================================================== */
//   /* Constructor                                                */
//   /* ========================================================== */

//   public ApiEndpointAuthorizationManagerNew(
//       RequestMappingHandlerMapping handlerMapping, Environment environment) {
//     this.environment = environment;

//     /* ---------- Default public endpoints ---------- */
//     registerPublicEndpoint(null, "/actuator/**", false);
//     registerPublicEndpoint(GET, "/v3/api-docs**/**", false);
//     registerPublicEndpoint(GET, "/swagger-ui**/**", false);
//     registerPublicEndpoint(GET, "/.well-known**/**", false);
//     registerPublicEndpoint(GET, "/favicon.ico", false);

//     /* ---------- Static endpoint detection ---------- */

//     Map<RequestMappingInfo, HandlerMethod> mappings = handlerMapping.getHandlerMethods();

//     mappings.forEach(this::registerStaticEndpoint);

//     /* ---------- Scan @PublicEndpoint ---------- */

//     mappings.forEach(
//         (info, method) -> {
//           PublicEndpoint annotation = getAnnotation(method, PublicEndpoint.class);
//           if (annotation == null || !profileMatched(annotation, method)) return;
//           registerPublicEndpoint(info, annotation.filterJwt());
//         });

//     /* ---------- Build Delegating Manager ---------- */

//     var builder = RequestMatcherDelegatingAuthorizationManager.builder();

//     for (var entry : publicEndpoints.entrySet()) {
//       HttpMethod method = entry.getKey();

//       for (APIPath apiPath : entry.getValue()) {
//         PathPatternRequestMatcher matcher =
//             PathPatternRequestMatcher.withDefaults()
//                 .matcher(method, apiPath.pattern().getPatternString());
//         builder.add(matcher, (auth, ctx) -> new AuthorizationDecision(true));
//       }
//     }

//     // fallback → authenticated
//     builder.add(
//         PathPatternRequestMatcher.withDefaults().matcher("/**"),
//         AuthenticatedAuthorizationManager.authenticated());

//     delegate = builder.build();

//     logInitializedEndpoints();
//   }

//   /* ========================================================== */
//   /* Authorization                                              */
//   /* ========================================================== */

//   @Override
//   public @Nullable AuthorizationResult authorize(
//       Supplier<? extends @Nullable Authentication> authentication,
//       RequestAuthorizationContext context) {
//     HttpServletRequest request = context.getRequest();
//     return delegate.authorize(authentication, request);
//   }

//   /* ========================================================== */
//   /* Public helpers (for JWT filter)                            */
//   /* ========================================================== */

//   public boolean isOptionalJwtSecurityPath(@NonNull HttpServletRequest request) {
//     HttpMethod method = HttpMethod.valueOf(request.getMethod());
//     return publicEndpoints.getOrDefault(method, Set.of()).stream()
//         .filter(APIPath::filterJwt)
//         .anyMatch(p -> matchPath(p, request));
//   }

//   public boolean isUnsecureJwtRequest(@NonNull HttpServletRequest request) {
//     HttpMethod method = HttpMethod.valueOf(request.getMethod());
//     return publicEndpoints.getOrDefault(method, Set.of()).stream()
//         .filter(p -> !p.filterJwt())
//         .anyMatch(p -> matchPath(p, request));
//   }

//   /* ========================================================== */
//   /* Registration                                               */
//   /* ========================================================== */

//   private void registerPublicEndpoint(@Nullable HttpMethod method, String path, boolean
// filterJwt) {

//     if (method == null) {
//       for (HttpMethod m : HttpMethod.values()) {
//         registerPublicEndpoint(m, path, filterJwt);
//       }
//       return;
//     }

//     PathPattern pattern = PATH_PARSER.parse(path);
//     boolean dynamic = pattern.hasPatternSyntax();

//     publicEndpoints
//         .computeIfAbsent(method, m -> ConcurrentHashMap.newKeySet())
//         .add(new APIPath(pattern, dynamic, filterJwt));
//   }

//   private void registerPublicEndpoint(RequestMappingInfo info, boolean filterJwt) {
//     info.getMethodsCondition()
//         .getMethods()
//         .forEach(
//             method ->
//                 info.getPatternValues()
//                     .forEach(
//                         path -> registerPublicEndpoint(method.asHttpMethod(), path, filterJwt)));
//   }

//   private void registerStaticEndpoint(RequestMappingInfo info, HandlerMethod handlerMethod) {
//     info.getMethodsCondition()
//         .getMethods()
//         .forEach(
//             m ->
//                 info.getPatternValues()
//                     .forEach(
//                         path -> {
//                           PathPattern pattern = PATH_PARSER.parse(path);
//                           if (!pattern.hasPatternSyntax()) {
//                             staticEndpoints
//                                 .computeIfAbsent(
//                                     m.asHttpMethod(), k -> ConcurrentHashMap.newKeySet())
//                                 .add(pattern);
//                           }
//                         }));
//   }

//   /* ========================================================== */
//   /* Matching logic                                             */
//   /* ========================================================== */

//   private boolean matchPath(APIPath apiPath, HttpServletRequest request) {
//     PathPattern pattern = apiPath.pattern();
//     boolean matched = pattern.matches(PathContainer.parsePath(request.getRequestURI()));
//     if (!matched) return false;

//     if (!apiPath.dynamic()) return true;

//     return !staticEndpointExists(request);
//   }

//   private boolean staticEndpointExists(HttpServletRequest request) {
//     HttpMethod method = HttpMethod.valueOf(request.getMethod());
//     PathContainer path = PathContainer.parsePath(request.getRequestURI());

//     return staticEndpoints.getOrDefault(method, Set.of()).stream().anyMatch(p ->
// p.matches(path));
//   }

//   /* ========================================================== */
//   /* Annotation utils                                           */
//   /* ========================================================== */

//   private boolean profileMatched(PublicEndpoint annotation, HandlerMethod method) {
//     Set<String> activeProfiles = Set.of(environment.getActiveProfiles());

//     if (annotation.profiles().length > 0
//         && Collections.disjoint(activeProfiles, Arrays.asList(annotation.profiles()))) {
//       return false;
//     }

//     Profile profile = method.getBeanType().getAnnotation(Profile.class);
//     if (profile != null && Collections.disjoint(activeProfiles, Arrays.asList(profile.value())))
// {
//       return false;
//     }

//     return true;
//   }

//   @Nullable
//   private <A extends Annotation> A getAnnotation(HandlerMethod method, Class<A> clazz) {
//     A ann = method.getMethodAnnotation(clazz);
//     return ann != null ? ann : method.getBeanType().getAnnotation(clazz);
//   }

//   /* ========================================================== */
//   /* Logging                                                    */
//   /* ========================================================== */

//   private void logInitializedEndpoints() {
//     if (!log.isInfoEnabled()) return;

//     StringBuilder sb = new StringBuilder(1024);
//     sb.append("\n==== SECURITY ENDPOINTS ====\n");

//     for (var entry : publicEndpoints.entrySet()) {
//       sb.append(entry.getKey().name()).append(":\n");

//       List<APIPath> list = new ArrayList<>(entry.getValue());
//       list.sort(Comparator.comparing(p -> p.pattern().getPatternString()));

//       for (APIPath p : list) {
//         sb.append("  - ")
//             .append(p.pattern().getPatternString())
//             .append(p.filterJwt() ? " [PUBLIC + OPTIONAL JWT]" : " [PUBLIC]")
//             .append('\n');
//       }
//     }

//     sb.append("============================\n");
//     log.info(sb.toString());
//   }
// }
