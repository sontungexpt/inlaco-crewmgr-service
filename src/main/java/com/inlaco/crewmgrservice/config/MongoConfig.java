package com.inlaco.crewmgrservice.config;

import com.inlaco.crewmgrservice.endpoint.APIEndpointNameCodeReadingConverter;
import com.inlaco.crewmgrservice.endpoint.APIEndpointNameStrReadingConverter;
import com.inlaco.crewmgrservice.endpoint.APIEndpointNameWritingConverter;
import com.inlaco.crewmgrservice.feature.user.model.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableMongoAuditing
@RequiredArgsConstructor
@Slf4j
public class MongoConfig {
  private final APIEndpointNameWritingConverter apiEndpointNameWritingConverter;
  private final APIEndpointNameStrReadingConverter apiEndpointNameReadingConverter;
  private final APIEndpointNameCodeReadingConverter apiEndpointNameCodeReadingConverter;

  @Bean
  public MongoCustomConversions customConversions() {
    return new MongoCustomConversions(
        List.of(
            apiEndpointNameWritingConverter,
            apiEndpointNameReadingConverter,
            apiEndpointNameCodeReadingConverter));
  }

  // https://stackoverflow.com/questions/29472931/how-does-createdby-work-in-spring-data-jpa
  @Bean
  public AuditorAware<String> auditorProvider() {
    return () -> {
      log.debug("derived auditorProvider");
      User user = getUser();
      if (user == null) return Optional.empty();
      return Optional.of(user.getId());
    };
  }

  @Bean
  @Primary
  public AuditorAware<ObjectId> auditorObjectIdProvider() {
    return () -> {
      log.debug("derived auditorObjectIdProvider");
      User user = getUser();
      if (user == null) return Optional.empty();
      return Optional.of(new ObjectId(user.getId()));
    };
  }

  private User getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) return null;
    Object principal = authentication.getPrincipal();
    if (!(principal instanceof User)) return null;
    return (User) principal;
  }
}
