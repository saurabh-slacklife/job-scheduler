package io.jobscheduler.scheduler.service;

import io.jobscheduler.models.Task;
import io.jobscheduler.models.TaskStatus;
import io.jobscheduler.scheduler.repository.MongoTaskRepositoryImpl;
import io.jobscheduler.scheduler.tasks.CallableJob;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService<Task> {

  private ScheduledThreadPoolExecutor jobScheduledThreadPoolExecutor;
  private Semaphore semaphore;
  private MongoTaskRepositoryImpl mongoTaskRepositoryImpl;

  @Value("${task.schedule.execution.window}")
  private long executionWindowSeconds;

  @Value("${task.schedule.execution.delta}")
  private long executionDelta;

  public TaskServiceImpl(@Autowired ScheduledThreadPoolExecutor jobScheduledThreadPoolExecutor,
      @Autowired MongoTaskRepositoryImpl mongoTaskRepositoryImpl,
      @Value("${service.thread.semaphore.count}") int semaphoreCount,
      @Value("${service.thread.semaphore.fairness:true}") boolean fairness) {
    this.mongoTaskRepositoryImpl = mongoTaskRepositoryImpl;
    this.jobScheduledThreadPoolExecutor = jobScheduledThreadPoolExecutor;
    this.semaphore = new Semaphore(semaphoreCount, fairness);
  }

  @Override
  public void processTask(Task task) {
    long executionDelaySeconds = computeExecutionDelayTime(task);

    if (executionDelaySeconds >= -executionDelta
        && executionDelaySeconds < executionWindowSeconds) {
      log.info("Submitting task={} to queue", task);
      this.executeTask(task, executionDelaySeconds);
    } else if (executionDelaySeconds >= -executionDelta) {
      this.updateTask(task, TaskStatus.SCHEDULED);
      log.info("JobId={} with job={} will be scheduled at={}", task.getJobId(), task,
          LocalDateTime
              .ofEpochSecond(task.getJobScheduleTimeUtc().getEpochSecond(), 0,
                  ZoneOffset.UTC));
    } else {
      final StringBuilder reason = new StringBuilder();
      reason.append("JobId=").append(task.getJobId())
          .append(" with job=").append(task)
          .append(" was submitted in past at=")
          .append(LocalDateTime
              .ofEpochSecond(task.getJobScheduleTimeUtc().getEpochSecond(), 0, ZoneOffset.UTC))
          .append(" with delta greater=").append(executionDelta).append(", hence not scheduling");
      this.updateTask(task, TaskStatus.FAILED, reason.toString());
      log.error(reason.toString());
    }
  }

  /**
   * Execute the task with this executionDelaySeconds {@link #computeExecutionDelayTime}
   *
   * @param task                  {@link Task}
   * @param executionDelaySeconds
   */
  public void executeTask(Task task, long executionDelaySeconds) {
    this.jobScheduledThreadPoolExecutor
        .schedule(new CallableJob(task, semaphore, mongoTaskRepositoryImpl), executionDelaySeconds,
            TimeUnit.SECONDS);
  }

  /**
   * Compute executionDelaySeconds for given Task i.e Get the difference of Current system UTC time
   * and time at which task needs to be executed
   *
   * @param task {@link Task}
   * @return long
   */
  public long computeExecutionDelayTime(Task task) {
    long executionDelaySeconds = 0L;
    if (task.getJobScheduleTimeUtc() != null) {
      final Instant jobScheduledTimeUtc = task.getJobScheduleTimeUtc();

      executionDelaySeconds = Duration
          .between(Clock.systemUTC().instant(), jobScheduledTimeUtc)
          .getSeconds();
    }
    return executionDelaySeconds;
  }

  private void updateTask(@NonNull Task task, TaskStatus taskStatus) {
    if (task != null) {
      this.mongoTaskRepositoryImpl.update(task.getJobId(), taskStatus,
          task.getJobScheduleTimeUtc().getEpochSecond());
    }
  }

  private void updateTask(@NonNull Task task, TaskStatus taskStatus, String reason) {
    if (task != null) {
      this.mongoTaskRepositoryImpl.update(task.getJobId(), taskStatus, reason);
    }
  }
}
