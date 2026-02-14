package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.config;

import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import java.util.Collections;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "inlaco.template.email")
public class RecruitmentEmailProperties {

  private Map<ApplicationStatus, Template> recruitment;

  public Map<ApplicationStatus, Template> getTemplates() {
    return recruitment != null ? Collections.unmodifiableMap(recruitment) : Map.of();
  }

  public Template getTemplate(ApplicationStatus status) {
    return getTemplates().get(status);
  }

  public record Template(String subject, String path) {}
}
