package com.inlaco.crewmgrservice.feature.shipschedule.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "attendance.qr-code")
public class AttendanceProperties {

  private long duration = 300000; // 5 minutes in milliseconds default
}
