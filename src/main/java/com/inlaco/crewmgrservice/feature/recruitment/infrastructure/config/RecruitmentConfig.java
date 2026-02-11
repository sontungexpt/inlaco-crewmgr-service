package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RecruitmentEmailProperties.class)
public class RecruitmentConfig {}
