package com.inlaco.crewmgrservice.init;

import com.inlaco.crewmgrservice.feature.user.model.authorization.ApiEndpoint;
import com.inlaco.crewmgrservice.feature.user.repository.ApiEndpointRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public record ApiEndpointInit(
    RequestMappingHandlerMapping requestMappingHandlerMapping,
    ApiEndpointRepository apiEndpointRepository)
    implements CommandLineRunner {

  @Override
  public void run(String... args) throws Exception {
    requestMappingHandlerMapping
        .getHandlerMethods()
        .forEach(
            (requestMappingInfo, handlerMethod) -> {
              String urlPattern = requestMappingInfo.getPatternsCondition().toString();
              String httpMethod = requestMappingInfo.getMethodsCondition().toString();

              // Tạo entity ApiEndpoint và lưu vào database
              ApiEndpoint endpoint = new ApiEndpoint();
              // endpoint.setMethod(httpMethod);
              // endpoint.setUrl(urlPattern);
              apiEndpointRepository.save(endpoint);
            });

    System.out.println("All API endpoints have been saved to the database.");
  }
}
