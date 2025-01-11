package com.inlaco.crewmgrservice.init;

import com.inlaco.crewmgrservice.feature.user.enums.UserStatus;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.model.authorization.Right;
import com.inlaco.crewmgrservice.feature.user.model.authorization.Role;
import com.inlaco.crewmgrservice.feature.user.repository.RoleRepository;
import com.inlaco.crewmgrservice.feature.user.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RoleInit implements CommandLineRunner {
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
    String adminUsername = "admin@gmail.com";
    User user = userRepository.findByUsername(adminUsername).orElse(null);
    if (user == null) {
      User admin =
          User.builder()
              .username(adminUsername)
              .password(passwordEncoder.encode("Admin123"))
              .build();

      var roles = roleRepository.findAll();
      Right right = new Right(new HashSet<>(roles));
      admin.setStatus(UserStatus.ACTIVE);
      admin.setRight(right);

      userRepository.save(admin);
    }
  }
}
