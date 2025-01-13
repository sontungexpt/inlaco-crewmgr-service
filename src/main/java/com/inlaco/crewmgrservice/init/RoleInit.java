package com.inlaco.crewmgrservice.init;

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
public class RoleInit implements CommandLineRunner {

  @Value("${admin.username}")
  private String ADMIN_USERNAME;

  @Value("${admin.password}")
  private String ADMIN_PASSWORD;

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    List<String> roles = List.of("USER", "ADMIN", "SAILOR");

    roles.forEach(
        role -> {
          if (!roleRepository.existsByName(role)) {
            roleRepository.save(Role.builder().name(role).build());
          }
        });

    initAdminAccount();
  }

  public void initAdminAccount() {
    User user = userRepository.findByUsername(ADMIN_USERNAME).orElse(null);
    if (user == null) {
      User admin =
          User.builder()
              .username(ADMIN_USERNAME)
              .password(passwordEncoder.encode(ADMIN_PASSWORD))
              .status(UserStatus.ACTIVE)
              .right(new Right(roleRepository.findAll()))
              .build();

      userRepository.save(admin);
    }
  }
}
