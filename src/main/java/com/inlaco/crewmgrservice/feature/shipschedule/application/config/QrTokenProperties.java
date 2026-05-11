package com.inlaco.crewmgrservice.feature.shipschedule.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "token.qr-token.attendance")
@Configuration
public class QrTokenProperties {

  private Checkin checkin = new Checkin();
  private Checkout checkout = new Checkout();

  @Data
  public static class Checkin {
    private String secretKey;
    private long expiration = 300000; // 5 minutes default
  }

  @Data
  public static class Checkout {
    private String secretKey;
    private long expiration = 300000; // 5 minutes default
  }
}
