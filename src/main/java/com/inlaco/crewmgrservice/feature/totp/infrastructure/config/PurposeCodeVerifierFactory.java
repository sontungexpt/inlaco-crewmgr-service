package com.inlaco.crewmgrservice.feature.totp.infrastructure.config;

import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.secret.SecretGenerator;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PurposeCodeVerifierFactory {

  private final Map<TotpSecret.TotpPurpose, CodeVerifier> codeVerifiers;
  private final Map<TotpSecret.TotpPurpose, SecretGenerator> secretGenerators;

  private final CodeVerifier defaultCodeVerifier;
  private final SecretGenerator defaultSecretGenerator;

  public CodeVerifier getCodeVerifier(TotpSecret.TotpPurpose purpose) {
    return codeVerifiers.getOrDefault(purpose, defaultCodeVerifier);
  }

  public SecretGenerator getSecretGenerator(TotpSecret.TotpPurpose purpose) {
    return secretGenerators.getOrDefault(purpose, defaultSecretGenerator);
  }
}
