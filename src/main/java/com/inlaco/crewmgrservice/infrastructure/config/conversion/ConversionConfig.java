package com.inlaco.crewmgrservice.infrastructure.config.conversion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.convert.Jsr310Converters.StringToInstantConverter;

@Configuration
public class ConversionConfig {

  @Bean
  public ConversionService filterConversionService() {
    DefaultConversionService cs = new DefaultConversionService();
    cs.addConverter(StringToInstantConverter.INSTANCE);
    return cs;
  }
}
