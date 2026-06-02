package com.inlaco.crewmgrservice.feature.totp.infrastructure.config;

import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import java.time.Duration;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "totp")
public class TotpCustomProperties {

  private QrConfig qr = new QrConfig();
  private CleanupConfig cleanup = new CleanupConfig();
  private Map<TotpSecret.TotpPurpose, PurposeConfig> purposes;

  @Data
  public static class SecretConfig {
    private int length = 128;
  }

  @Data
  public static class CodeConfig {
    private int length = 6;
  }

  @Data
  public static class TimeConfig {
    private int period = 30;
    private int discrepancy = 2;
  }

  // ===== OTHER =====

  @Data
  public static class QrConfig {
    private int width = 200;
    private int height = 200;
    private int margin = 1;
  }

  @Data
  public static class CleanupConfig {
    private String cron = "0 0 2 * * *";
    private Duration lifetime = Duration.ofDays(30);
  }

  // ===== PURPOSE =====

  @Data
  public static class PurposeConfig {
    private SecretConfig secret;
    private CodeConfig code;
    private TimeConfig time;
    private String description;
  }
}
