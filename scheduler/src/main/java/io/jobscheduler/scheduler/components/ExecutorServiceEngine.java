package io.jobscheduler.scheduler.components;

import io.jobscheduler.scheduler.comparators.JobComparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExecutorServiceEngine {

  @Value("${service.executor.core.pool.min}")
  private int corePoolSize;
  @Value("${service.executor.core.pool.max}")
  private int maximumPoolSize;
  @Value("${service.executor.core.pool.keep-alive}")
  private long keepAlive;

  @Bean
  public ExecutorService getExecutorService() {
    ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
        keepAlive, TimeUnit.SECONDS,
        new PriorityBlockingQueue(11, new JobComparator()),
        new GrowAndSubmitPolicy());
    return executorService;
  }

}
