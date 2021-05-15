package io.jobscheduler.scheduler.components;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrowAndSubmitPolicy implements RejectedExecutionHandler {

  private Lock lock = new ReentrantLock(true);

  @Override
  public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
    log.warn("Thread Pool executor reached Saturation with ActiveCount={} MaxPoolSize={}",
        executor.getActiveCount(), executor.getMaximumPoolSize());
    lock.lock();
    try {
      if (!executor.isShutdown()) {
        executor.setMaximumPoolSize(1 + executor.getMaximumPoolSize());
        log.warn("Thread Pool executor max size increased with ActiveCount={} MaxPoolSize={}",
            executor.getActiveCount(), executor.getMaximumPoolSize());
        executor.submit(r);
      }
      {
        log.warn("Thread Pool executor is Shutdown, can't submit runnable task");
      }
    } finally {
      lock.unlock();
    }
  }
}
