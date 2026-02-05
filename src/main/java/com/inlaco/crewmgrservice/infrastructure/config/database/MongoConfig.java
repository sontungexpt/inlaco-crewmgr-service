package com.inlaco.crewmgrservice.infrastructure.config.database;

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
  public AuditorAware<String> auditorStringProvider() {
    return () -> {
      log.debug("derived auditorProvider");
      return getCurrentUser().map(User::getId);
    };
  }

  @Bean
  @Primary
  public AuditorAware<ObjectId> auditorObjectIdProvider() {
    return () -> {
      log.debug("derived auditorObjectIdProvider");
      return getCurrentUser().map(u -> new ObjectId(u.getId()));
    };
  }

  private Optional<User> getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated()) return Optional.empty();
    return auth.getPrincipal() instanceof User u ? Optional.of(u) : Optional.empty();
  }
}
