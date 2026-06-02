package com.inlaco.crewmgrservice.feature.totp.infrastructure.config;

import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.spring.autoconfigure.TotpAutoConfiguration;
import dev.samstevens.totp.spring.autoconfigure.TotpProperties;
import dev.samstevens.totp.time.TimeProvider;
import java.util.EnumMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({TotpCustomProperties.class})
// Import explicitly to fix bean totp not found
@ImportAutoConfiguration(TotpAutoConfiguration.class)
public class TotpConfig {

  private final TotpCustomProperties customProps;
  private final TotpProperties totpProps;

  @Bean
  public Map<TotpSecret.TotpPurpose, SecretGenerator> secretGenerators() {

    Map<TotpSecret.TotpPurpose, SecretGenerator> map = new EnumMap(TotpSecret.TotpPurpose.class);

    var purposes = customProps.getPurposes();
    if (purposes == null || purposes.isEmpty()) {
      return map;
    }

    for (var entry : purposes.entrySet()) {

      var purpose = entry.getKey();
      var cfg = entry.getValue();

      int numCharacters =
          cfg.getSecret() != null && cfg.getSecret().getLength() != 0
              ? cfg.getSecret().getLength()
              : totpProps.getSecret().getLength();

      SecretGenerator generator = new DefaultSecretGenerator(numCharacters);

      map.put(purpose, generator);
    }

    return map;
  }

  @Bean
  public Map<TotpSecret.TotpPurpose, CodeVerifier> codeVerifiers(
      HashingAlgorithm hashingAlgorithm, TimeProvider timeProvider) {

    Map<TotpSecret.TotpPurpose, CodeVerifier> map = new EnumMap(TotpSecret.TotpPurpose.class);

    var purposes = customProps.getPurposes();
    if (purposes == null || purposes.isEmpty()) {
      return map;
    }

    for (var entry : purposes.entrySet()) {

      var purpose = entry.getKey();
      var cfg = entry.getValue();

      int digits =
          cfg.getCode() != null && cfg.getCode().getLength() != 0
              ? cfg.getCode().getLength()
              : totpProps.getCode().getLength();

      CodeGenerator codeGenerator = new DefaultCodeGenerator(hashingAlgorithm, digits);

      DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

      int period =
          cfg.getTime() != null && cfg.getTime().getPeriod() != 0
              ? cfg.getTime().getPeriod()
              : totpProps.getTime().getPeriod();

      int discrepancy =
          cfg.getTime() != null && cfg.getTime().getDiscrepancy() != 0
              ? cfg.getTime().getDiscrepancy()
              : totpProps.getTime().getDiscrepancy();

      verifier.setTimePeriod(period);
      verifier.setAllowedTimePeriodDiscrepancy(discrepancy);

      map.put(purpose, verifier);
    }

    return map;
  }
}
