package com.inlaco.crewmgrservice.infrastructure.bootstrap;

import com.inlaco.crewmgrservice.feature.user.enums.UserStatus;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.model.authorization.Right;
import com.inlaco.crewmgrservice.feature.user.model.authorization.Role;
import com.inlaco.crewmgrservice.feature.user.repository.RoleRepository;
import com.inlaco.crewmgrservice.feature.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  @Value("${admin.username}")
  private String ADMIN_USERNAME;

  @Value("${admin.password}")
  private String ADMIN_PASSWORD;

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void run(String... args) {
    List<Role> roles = initRoles();
    initAdmin(roles);
  }

  private List<Role> initRoles() {
    List<String> roleNames = List.of("USER", "ADMIN", "SAILOR");
    roleNames.forEach(
        name -> {
          if (!roleRepository.existsByName(name)) {
            roleRepository.save(Role.builder().name(name).build());
          }
        });
    return roleRepository.findAll();
  }

  private void initAdmin(List<Role> roles) {
    if (userRepository.existsByUsername(ADMIN_USERNAME)) {
      return;
    }
    User admin =
        User.builder()
            .name("Admin")
            .username(ADMIN_USERNAME)
            .password(passwordEncoder.encode(ADMIN_PASSWORD))
            .status(UserStatus.ACTIVE)
            .right(new Right(roles))
            .build();

    userRepository.save(admin);
  }
}
