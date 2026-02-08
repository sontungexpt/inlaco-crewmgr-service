package com.inlaco.crewmgrservice.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TestInit implements CommandLineRunner {

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    // System.out.println("Database: " + mongoDatabase);
  }
}
