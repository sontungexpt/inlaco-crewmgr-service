package com.inlaco.crewmgrservice.infrastructure.config.validtion;

import com.inlaco.crewmgrservice.infrastructure.web.validation.extractor.FieldUpdateValueExtractor;
import jakarta.validation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@org.springframework.context.annotation.Configuration
public class ValidationConfig extends LocalValidatorFactoryBean {

  // Source - https://stackoverflow.com/a/64975661
  // Posted by Mr.Robot
  // Retrieved 2026-02-18, License - CC BY-SA 4.0
  @Override
  protected void postProcessConfiguration(Configuration<?> configuration) {
    super.postProcessConfiguration(configuration);
    configuration.addValueExtractor(new FieldUpdateValueExtractor());
  }
}
