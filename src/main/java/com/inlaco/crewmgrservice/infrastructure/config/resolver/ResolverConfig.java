package com.inlaco.crewmgrservice.infrastructure.config.resolver;

import com.inlaco.crewmgrservice.infrastructure.web.resolver.BearerTokenArgumentResolver;
import com.inlaco.crewmgrservice.infrastructure.web.resolver.CurrentUserArgumentResolver;
import com.inlaco.crewmgrservice.infrastructure.web.resolver.DefaultSortPageableResolver;
import com.inlaco.crewmgrservice.infrastructure.web.resolver.FilterableArgumentResolver;
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
  private final FilterableArgumentResolver filterableArgumentResolver;
  private final CurrentUserArgumentResolver currentUserArgumentResolver;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(bearerTokenArgumentResolver);
    resolvers.add(defaultSortPageableResolver);
    resolvers.add(filterableArgumentResolver);
    resolvers.add(currentUserArgumentResolver);
  }
}
