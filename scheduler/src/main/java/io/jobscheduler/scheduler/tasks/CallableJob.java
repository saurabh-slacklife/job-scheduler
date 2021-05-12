package io.jobscheduler.scheduler.tasks;

import io.jobscheduler.models.Task;
import io.jobscheduler.models.TaskStatus;
import io.jobscheduler.scheduler.action.ActionFactory;
import io.jobscheduler.scheduler.models.document.TaskDocument;
import io.jobscheduler.scheduler.repository.MongoTaskRepositoryImpl;
import io.jobscheduler.scheduler.repository.MongoUtil;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CallableJob implements Runnable {

  private Semaphore semaphore;
  private Task data;

  private MongoTaskRepositoryImpl mongoTaskRepository;

  public CallableJob(Task data, Semaphore semaphore, MongoTaskRepositoryImpl mongoTaskRepository) {
    this.data = data;
    this.semaphore = semaphore;
    this.mongoTaskRepository = mongoTaskRepository;

  }

  @Override
  public void run() {
    boolean isActionCompleted = false;
    try {
      semaphore.acquire();
      if (this.data != null) {
        log.info("Runnable={} executing with priority={} at={}", this.data.getTaskId(),
            this.data.getPriority(), this.data.getJobScheduleTimeUtc());

        this.mongoTaskRepository.update(this.data.getTaskId(), TaskStatus.RUNNING);


        final var actor = ActionFactory.getActor(this.data.getJobType());
        isActionCompleted = actor.act(this.data);

        this.mongoTaskRepository.update(this.data.getTaskId(), TaskStatus.SUCCESS);

      }
    } catch (InterruptedException e) {
      log.error("Semaphore Interrupted");
    } finally {
      semaphore.release();
    }
  }

  public Task getData() {
    return data;
  }
}
