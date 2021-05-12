package io.jobscheduler.scheduler.components;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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

  @Value("${service.executor.core.pool.termination}")
  private long termination;

  private ThreadPoolExecutor threadPoolExecutor;
  private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

  @Bean
  public ThreadPoolExecutor priorityThreadPoolExecutor() {
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
        keepAlive, TimeUnit.SECONDS,
        new PriorityBlockingQueue(),
        new GrowAndSubmitPolicy());
    this.threadPoolExecutor = threadPoolExecutor;
    return threadPoolExecutor;
  }

  @Bean
  @Qualifier("jobScheduledThreadPoolExecutor")
  public ScheduledThreadPoolExecutor jobScheduledThreadPoolExecutor() {
    this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(
        corePoolSize, new GrowAndSubmitPolicy());
    return scheduledThreadPoolExecutor;
  }

  @PreDestroy
  public void stopThreadPoolExecutor() {
    final List<Runnable> unfinishedTasks = new ArrayList<>();
    if (null != this.threadPoolExecutor && !this.threadPoolExecutor.isTerminating()) {
      this.threadPoolExecutor.shutdown();
      try {
        if (!this.threadPoolExecutor.awaitTermination(termination, TimeUnit.SECONDS)) {
          unfinishedTasks.addAll(this.threadPoolExecutor.shutdownNow());
          if (!this.threadPoolExecutor.awaitTermination(termination, TimeUnit.SECONDS)) {
            log.error("Thread Pool didn't terminate");
          }
        }
      } catch (InterruptedException ie) {
        unfinishedTasks.addAll(this.threadPoolExecutor.shutdownNow());
        Thread.currentThread().interrupt();
      } finally {
        if (!this.threadPoolExecutor.isTerminated()) {
          unfinishedTasks.addAll(this.threadPoolExecutor.shutdownNow());
        }
        log.warn("Thread Pool terminated with total unfinished tasks={}", unfinishedTasks.size());
      }
    }
  }

  @PreDestroy
  public void stopScheduledThreadPoolExecutor() {
    final List<Runnable> unfinishedTasks = new ArrayList<>();
    if (null != this.scheduledThreadPoolExecutor && !this.scheduledThreadPoolExecutor
        .isTerminating()) {
      this.scheduledThreadPoolExecutor.shutdown();
      try {
        if (!this.scheduledThreadPoolExecutor.awaitTermination(termination, TimeUnit.SECONDS)) {
          unfinishedTasks.addAll(this.threadPoolExecutor.shutdownNow());
          if (!this.scheduledThreadPoolExecutor.awaitTermination(termination, TimeUnit.SECONDS)) {
            log.error("Scheduled ThreadPool didn't terminate");
          }
        }
      } catch (InterruptedException ie) {
        unfinishedTasks.addAll(this.scheduledThreadPoolExecutor.shutdownNow());
        Thread.currentThread().interrupt();
      } finally {
        if (!this.scheduledThreadPoolExecutor.isTerminated()) {
          unfinishedTasks.addAll(this.scheduledThreadPoolExecutor.shutdownNow());
        }
        log.warn("Scheduled ThreadPool terminated with total unfinished tasks={}",
            unfinishedTasks.size());
      }
    }
  }

}
