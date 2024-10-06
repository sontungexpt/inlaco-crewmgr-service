package com.inlaco.crewmgrservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncEventConfig {

  @Bean
  TaskExecutor taskExecutor() {
    return new SimpleAsyncTaskExecutor();
  }
}
