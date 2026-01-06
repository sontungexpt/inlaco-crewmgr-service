package com.inlaco.crewmgrservice.config;

import com.inlaco.crewmgrservice.resolver.BearerTokenArgumentResolver;
import com.inlaco.crewmgrservice.resolver.DefaultSortPageableResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ResolverConfig implements WebMvcConfigurer {

  private final DefaultSortPageableResolver defaultSortPageableResolver;
  private final BearerTokenArgumentResolver bearerTokenArgumentResolver;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(bearerTokenArgumentResolver);
    resolvers.add(defaultSortPageableResolver);
  }
}
