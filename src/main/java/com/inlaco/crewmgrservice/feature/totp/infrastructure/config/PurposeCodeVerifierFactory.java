package com.inlaco.crewmgrservice.feature.totp.infrastructure.config;

import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import dev.samstevens.totp.code.CodeVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PurposeCodeVerifierFactory {

    private final Map<String, CodeVerifier> verifiers;

    public CodeVerifier getVerifierForPurpose(TotpSecret.TotpPurpose purpose) {
        String beanName = switch (purpose) {
            case API_KEY_CREATION -> "apiKeyCreationVerifier";
            case SECRET_KEY_VIEWING -> "secretKeyViewingVerifier";
            case PASSWORD_RESET -> "passwordResetVerifier";
            case ACCOUNT_VERIFICATION -> "accountVerificationVerifier";
        };

        CodeVerifier verifier = verifiers.get(beanName);
        if (verifier == null) {
            // Fallback to default verifier from Spring Boot starter
            return verifiers.get("codeVerifier");
        }
        return verifier;
    }
}
