package com.inlaco.crewmgrservice.feature.totp.infrastructure.config;

import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "inlaco.totp")
public class TotpConfigProperties {
    
    private String issuer = "Inlaco";
    private QrConfig qr = new QrConfig();
    private CleanupConfig cleanup = new CleanupConfig();
    private Map<String, PurposeConfig> purposes;
    
    @Data
    public static class QrConfig {
        private int width = 200;
        private int height = 200;
        private int margin = 1;
    }
    
    @Data
    public static class CleanupConfig {
        private String cron = "0 0 2 * * *";
    }
    
    @Data
    public static class PurposeConfig {
        private int secretLength = 128;
        private int codeLength = 6;
        private int timePeriod = 30;
        private int timeTolerance = 2;
        private String description;
    }
    
    /**
     * Get configuration for specific purpose
     */
    public PurposeConfig getConfigForPurpose(TotpSecret.TotpPurpose purpose) {
        String key = getPurposeKey(purpose);
        return purposes != null ? purposes.get(key) : getDefaultConfig();
    }
    
    /**
     * Convert enum to configuration key
     */
    private String getPurposeKey(TotpSecret.TotpPurpose purpose) {
        return switch (purpose) {
            case API_KEY_CREATION -> "api-key-creation";
            case SECRET_KEY_VIEWING -> "secret-key-viewing";
            case PASSWORD_RESET -> "password-reset";
            case ACCOUNT_VERIFICATION -> "account-verification";
        };
    }
    
    /**
     * Get default configuration if purpose-specific not found
     */
    private PurposeConfig getDefaultConfig() {
        PurposeConfig defaultConfig = new PurposeConfig();
        defaultConfig.setSecretLength(128);
        defaultConfig.setCodeLength(6);
        defaultConfig.setTimePeriod(30);
        defaultConfig.setTimeTolerance(2);
        defaultConfig.setDescription("Default Configuration");
        return defaultConfig;
    }
}
