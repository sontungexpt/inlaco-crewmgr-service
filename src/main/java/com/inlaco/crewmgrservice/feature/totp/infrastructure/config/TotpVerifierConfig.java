package com.inlaco.crewmgrservice.feature.totp.infrastructure.config;

import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(TotpConfigProperties.class)
public class TotpVerifierConfig {

    private final TotpConfigProperties totpConfig;
    private final TimeProvider timeProvider = new SystemTimeProvider();

    // Don't create a default CodeVerifier bean to avoid conflicts with Spring Boot starter
    // The starter already provides one, we'll create purpose-specific ones only

    @Bean(name = "apiKeyCreationVerifier")
    public CodeVerifier apiKeyCreationVerifier() {
        TotpConfigProperties.PurposeConfig config = totpConfig.getConfigForPurpose(TotpSecret.TotpPurpose.API_KEY_CREATION);
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(HashingAlgorithm.SHA1, timeProvider);
        verifier.setTimePeriod(config.getTimePeriod());
        verifier.setAllowedTimePeriodDiscrepancy(config.getTimeTolerance());
        return verifier;
    }

    @Bean(name = "secretKeyViewingVerifier")
    public CodeVerifier secretKeyViewingVerifier() {
        TotpConfigProperties.PurposeConfig config = totpConfig.getConfigForPurpose(TotpSecret.TotpPurpose.SECRET_KEY_VIEWING);
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(HashingAlgorithm.SHA1, timeProvider);
        verifier.setTimePeriod(config.getTimePeriod());
        verifier.setAllowedTimePeriodDiscrepancy(config.getTimeTolerance());
        return verifier;
    }

    @Bean(name = "passwordResetVerifier")
    public CodeVerifier passwordResetVerifier() {
        TotpConfigProperties.PurposeConfig config = totpConfig.getConfigForPurpose(TotpSecret.TotpPurpose.PASSWORD_RESET);
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(HashingAlgorithm.SHA1, timeProvider);
        verifier.setTimePeriod(config.getTimePeriod());
        verifier.setAllowedTimePeriodDiscrepancy(config.getTimeTolerance());
        return verifier;
    }

    @Bean(name = "accountVerificationVerifier")
    public CodeVerifier accountVerificationVerifier() {
        TotpConfigProperties.PurposeConfig config = totpConfig.getConfigForPurpose(TotpSecret.TotpPurpose.ACCOUNT_VERIFICATION);
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(HashingAlgorithm.SHA1, timeProvider);
        verifier.setTimePeriod(config.getTimePeriod());
        verifier.setAllowedTimePeriodDiscrepancy(config.getTimeTolerance());
        return verifier;
    }

    @Bean
    public PurposeCodeVerifierFactory purposeCodeVerifierFactory(Map<String, CodeVerifier> verifiers) {
        return new PurposeCodeVerifierFactory(verifiers);
    }
}
