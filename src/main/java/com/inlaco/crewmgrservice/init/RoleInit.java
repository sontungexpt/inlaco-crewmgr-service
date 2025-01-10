package com.inlaco.crewmgrservice.init;

import com.inlaco.crewmgrservice.feature.user.model.authorization.Role;
import com.inlaco.crewmgrservice.feature.user.repository.RoleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RoleInit implements CommandLineRunner {
  private final RoleRepository roleRepository;

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN", "ROLE_SAILOR");

    roles.forEach(
        role -> {
          if (!roleRepository.existsByName(role)) {
            roleRepository.save(Role.builder().name(role).build());
          }
        });
  }
}
