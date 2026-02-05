package com.inlaco.crewmgrservice.infrastructure.config.async;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncEventConfig {

  @Bean
  public TaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    executor.setCorePoolSize(10); // the number of live threads
    executor.setMaxPoolSize(50); // max thread
    executor.setQueueCapacity(1000); // queue
    executor.setThreadNamePrefix("async-");

    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

    executor.initialize();
    return executor;
  }

  //   @Bean
  //   TaskExecutor taskExecutor() {
  //     return new SimpleAsyncTaskExecutor();
  //   }
}
