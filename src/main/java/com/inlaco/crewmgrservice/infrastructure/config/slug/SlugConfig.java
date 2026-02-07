package com.inlaco.crewmgrservice.infrastructure.config.slug;

import com.inlaco.crewmgrservice.shared.slug.SlugProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlugConfig {

  @Bean
  public SlugProcessor slugProcessor() {
    return new SlugProcessor();
  }
}
