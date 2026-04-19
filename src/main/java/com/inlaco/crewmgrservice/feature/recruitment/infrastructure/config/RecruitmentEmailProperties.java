package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.config;

import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "inlaco.template.email")
@RequiredArgsConstructor
@Slf4j
public class RecruitmentEmailProperties {

  private final EnumMap<ApplicationStatus, Template> recruitment;

  public Map<ApplicationStatus, Template> getTemplates() {
    return recruitment != null ? Collections.unmodifiableMap(recruitment) : Map.of();
  }

  public Template getTemplate(ApplicationStatus status) {
    return getTemplates().get(status);
  }

  public record Template(String subject, String path) {}

  @PostConstruct
  public void debug() {
    log.debug("Recruitment email templates initialized: {}", recruitment);
  }
}
