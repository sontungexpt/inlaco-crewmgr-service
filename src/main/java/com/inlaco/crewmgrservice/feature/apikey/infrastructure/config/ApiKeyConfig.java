package com.inlaco.crewmgrservice.feature.apikey.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.apikey")
public class ApiKeyConfig {
  
  private boolean enabled = true;
  private long defaultExpirationDays = 365;
  private String headerKeyId = "X-API-Key-ID";
  private String headerKeySecret = "X-API-Key-Secret";
  private boolean requireHttps = false;
  
  public boolean isEnabled() {
    return enabled;
  }
  
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
  
  public long getDefaultExpirationDays() {
    return defaultExpirationDays;
  }
  
  public void setDefaultExpirationDays(long defaultExpirationDays) {
    this.defaultExpirationDays = defaultExpirationDays;
  }
  
  public String getHeaderKeyId() {
    return headerKeyId;
  }
  
  public void setHeaderKeyId(String headerKeyId) {
    this.headerKeyId = headerKeyId;
  }
  
  public String getHeaderKeySecret() {
    return headerKeySecret;
  }
  
  public void setHeaderKeySecret(String headerKeySecret) {
    this.headerKeySecret = headerKeySecret;
  }
  
  public boolean isRequireHttps() {
    return requireHttps;
  }
  
  public void setRequireHttps(boolean requireHttps) {
    this.requireHttps = requireHttps;
  }
}
