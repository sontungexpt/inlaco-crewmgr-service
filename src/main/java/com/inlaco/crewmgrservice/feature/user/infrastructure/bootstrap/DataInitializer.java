package com.inlaco.crewmgrservice.feature.user.infrastructure.bootstrap;

import com.inlaco.crewmgrservice.feature.user.application.port.out.RoleRepository;
import com.inlaco.crewmgrservice.feature.user.application.port.out.UserRepository;
import com.inlaco.crewmgrservice.feature.user.domain.model.Role;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.feature.user.domain.model.UserAuthority;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

  private static final String FALLBACK_PASSWORD = "DemoPassword123!";

  @Value("${admin.username}")
  private String ADMIN_USERNAME;

  @Value("${admin.password}")
  private String ADMIN_PASSWORD;

  /** Format: account:password, account2:password, account3, account4 */
  @Value("${demo.accounts:}")
  private String demoAdminAccounts;

  @Value("${demo.default-password:" + FALLBACK_PASSWORD + "}")
  private String demoAdminDefaultPassword;

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void run(String... args) {
    List<Role> roles = initRoles();
    initAdmin(roles);
    initDemoAdminAccounts(roles);
  }

  private List<Role> initRoles() {
    List<String> roleNames = List.of("USER", "ADMIN", "SAILOR", "COMPANY_ADMIN");

    roleNames.forEach(
        name -> {
          if (!roleRepository.existsByName(name)) {
            Role role = new Role();
            role.setName(name);
            roleRepository.save(role);
          }
        });

    return roleRepository.findAll();
  }

  private void initAdmin(List<Role> roles) {
    if (userRepository.existsByUsername(ADMIN_USERNAME)) {
      return;
    }

    User admin =
        new User(
            ADMIN_USERNAME,
            passwordEncoder.encode(ADMIN_PASSWORD),
            new UserAuthority(roles.stream().map(Role::getId).toList()),
            "Admin");

    admin.activate("Admin account created");
    userRepository.save(admin);

    log.info("Admin account created: {}", ADMIN_USERNAME);
  }

  private void initDemoAdminAccounts(List<Role> roles) {
    if (demoAdminAccounts == null || demoAdminAccounts.isBlank()) {
      return;
    }

    List<String> roleIds = roles.stream().map(Role::getId).toList();
    String fallback =
        (demoAdminDefaultPassword != null && !demoAdminDefaultPassword.isBlank())
            ? demoAdminDefaultPassword
            : FALLBACK_PASSWORD;

    for (String entry : demoAdminAccounts.split(",")) {
      String trimmed = entry.trim();
      if (trimmed.isBlank()) {
        continue;
      }

      // Split on the first ':' only, so passwords may themselves contain ':'
      String[] parts = trimmed.split(":", 2);

      String username = parts[0].trim();
      if (username.isBlank() || userRepository.existsByUsername(username)) {
        continue;
      }

      boolean hasCustomPassword = parts.length > 1 && !parts[1].trim().isBlank();
      String password = hasCustomPassword ? parts[1].trim() : fallback;

      User demo =
          new User(
              username, passwordEncoder.encode(password), new UserAuthority(roleIds), "Demo");

      demo.activate("Demo account created");
      userRepository.save(demo);

      log.info(
          "Demo account created: {} (using {})",
          username,
          hasCustomPassword ? "custom password" : "default password");
    }
  }
}
