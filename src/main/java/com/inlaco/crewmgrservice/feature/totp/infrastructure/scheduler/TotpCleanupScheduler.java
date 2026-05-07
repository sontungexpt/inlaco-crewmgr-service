package com.inlaco.crewmgrservice.feature.totp.infrastructure.scheduler;

import com.inlaco.crewmgrservice.feature.totp.application.port.in.TotpUseCase;
import com.inlaco.crewmgrservice.feature.totp.infrastructure.config.TotpCustomProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TotpCleanupScheduler {

  private final TotpUseCase totpUseCase;
  private final TotpCustomProperties totpCustomProperties;

  @Scheduled(cron = "${totp.cleanup.cron:0 0 2 * * *}")
  public void cleanupExpiredTotpSecrets() {
    try {
      log.info("Starting cleanup of old TOTP secrets");
      totpUseCase.cleanupExpiredTokens(totpCustomProperties.getCleanup().getLifetime());
      log.info("Completed cleanup of old TOTP secrets");
    } catch (Exception e) {
      log.error("Error during TOTP cleanup", e);
    }
  }
}
