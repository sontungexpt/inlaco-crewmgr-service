package com.inlaco.crewmgrservice.feature.user.infrastructure.bootstrap;

import com.inlaco.crewmgrservice.feature.user.application.port.out.RoleRepository;
import com.inlaco.crewmgrservice.feature.user.application.port.out.UserRepository;
import com.inlaco.crewmgrservice.feature.user.domain.model.Role;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.feature.user.domain.model.UserAuthority;
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
  }
}
