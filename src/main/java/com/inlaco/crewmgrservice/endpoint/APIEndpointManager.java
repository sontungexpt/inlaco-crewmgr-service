// NOTE : This class is not used in the project.
// It is just an preparation.
// package com.inlaco.crewmgrservice.endpoint;

// import com.inlaco.crewmgrservice.CrewmgrserviceApplication;
// import jakarta.annotation.PostConstruct;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Map;
// import java.util.Set;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Component;
// import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

// @Slf4j
// @Component
// public class APIEndpointManager {

//   private RequestMappingHandlerMapping requestHandlerMapping;
//   private final Map<APIEndpointName, Set<APIEndpoint>> endpoints = new HashMap<>();

//   public Set<APIEndpoint> getEndpoint(APIEndpointName endpointName) {
//     return endpoints.get(endpointName);
//   }

//   public List<Set<APIEndpoint>> getEndpoints() {
//     return List.copyOf(endpoints.values());
//   }

//   public APIEndpointManager(RequestMappingHandlerMapping requestHandlerMapping) {
//     this.requestHandlerMapping = requestHandlerMapping;
//   }

//   @PostConstruct
//   public void init() {
//     final var handlerMethods = requestHandlerMapping.getHandlerMethods();
//     String pkgName = CrewmgrserviceApplication.class.getPackageName();

//     handlerMethods.forEach(
//         (requestInfo, handlerMethod) -> {
//           if (!handlerMethod.getBeanType().getPackageName().startsWith(pkgName)) {
//             return;
//           } else if (handlerMethod.getMethodAnnotation(APIEndpointMapSkip.class) != null) {
//             return;
//           }

//           APIEndpointMap annotation = handlerMethod.getMethodAnnotation(APIEndpointMap.class);
//           if (annotation == null) {
//             throw new MissEndpointMapperException(handlerMethod);
//           }

//           APIEndpointName endpointName = annotation.name();
//           requestInfo
//               .getMethodsCondition()
//               .getMethods()
//               .forEach(
//                   httpMethod -> {
//                     requestInfo
//                         .getPatternValues()
//                         .forEach(
//                             path -> {
//                               if (!endpoints.containsKey(endpointName)) {
//                                 endpoints.put(endpointName, new HashSet<APIEndpoint>());
//                               }
//                               log.info("Registering endpoint: {}", endpointName);
//                               endpoints
//                                   .get(endpointName)
//                                   .add(
//                                       APIEndpoint.builder()
//                                           .displayName(annotation.displayName())
//                                           .description(annotation.description())
//                                           .method(httpMethod.asHttpMethod())
//                                           .route(path)
//                                           .build());
//                             });
//                   });
//         });
//   }
// }
