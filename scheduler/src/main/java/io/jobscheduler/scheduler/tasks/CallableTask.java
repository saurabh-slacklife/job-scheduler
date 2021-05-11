package io.jobscheduler.scheduler.tasks;

import io.jobscheduler.models.Task;
import io.jobscheduler.scheduler.action.ActionFactory;
import java.util.concurrent.Semaphore;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CallableTask implements Runnable {

  private Semaphore semaphore;
  private Task data;

  public CallableTask(Task data, Semaphore semaphore) {
    this.data = data;
    this.semaphore = semaphore;

  }

  @Override
  public void run() {
    try {
      if (this.data != null) {
//        TODO handle if Data is not persisted
        semaphore.acquire();
        final var actor = ActionFactory.getActor(this.data.getJobType());
        actor.act(this.data);
      }
    } catch (InterruptedException e) {
      log.error("Semaphore Interrupted", e);
    } finally {
      semaphore.release();
    }
  }
}
