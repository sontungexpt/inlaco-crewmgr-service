package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.config;

import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "inlaco.template.email.recruitment")
public class RecruitmentEmailProperties {

  private Map<ApplicationStatus, Template> templates;

  public record Template(String subject, String path) {}
}
