package com.inlaco.crewmgrservice.init;

import com.inlaco.crewmgrservice.feature.auth.repository.EmailVerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TestInit implements CommandLineRunner {
  private final EmailVerificationTokenRepository emailVerificationTokenRepository;

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    // emailVerificationTokenRepository.deleteAll();
    // ;
  }
}
