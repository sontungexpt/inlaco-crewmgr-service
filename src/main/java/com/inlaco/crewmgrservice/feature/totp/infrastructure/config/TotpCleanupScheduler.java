package com.inlaco.crewmgrservice.feature.totp.infrastructure.config;

import com.inlaco.crewmgrservice.feature.totp.application.port.in.TotpUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TotpCleanupScheduler {

    private final TotpUseCase totpUseCase;

    @Scheduled(cron = "${totp.cleanup.cron:0 0 2 * * *}")
    public void cleanupExpiredTotpSecrets() {
        try {
            log.info("Starting cleanup of old TOTP secrets");
            totpUseCase.cleanupExpiredTokens();
            log.info("Completed cleanup of old TOTP secrets");
        } catch (Exception e) {
            log.error("Error during TOTP cleanup", e);
        }
    }
}
