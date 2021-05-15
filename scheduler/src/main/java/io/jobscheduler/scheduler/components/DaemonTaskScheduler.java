package io.jobscheduler.scheduler.components;

import io.jobscheduler.models.Task;
import io.jobscheduler.scheduler.models.document.TaskDocument;
import io.jobscheduler.scheduler.repository.MongoTaskRepositoryImpl;
import io.jobscheduler.scheduler.repository.MongoUtil;
import io.jobscheduler.scheduler.service.TaskServiceImpl;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DaemonTaskScheduler {

  @Autowired
  private MongoTaskRepositoryImpl mongoTaskRepositoryImpl;

  @Autowired
  private TaskServiceImpl taskServiceImpl;

  @Value("${task.daemon.execution.query.start-interval}")
  private long startInterval;

  @Value("${task.daemon.execution.query.elapsed-interval}")
  private long elapsedInterval;

  //  fixedRate of elapsedInterval seconds from last invocation to start of next
  @Scheduled(fixedRateString="${task.daemon.execution.interval:10}")
  public void scheduleTask() {

    Instant now = Instant.now();
    long startSeconds = now.minusSeconds(startInterval).getEpochSecond();
    long elapsedSeconds = now.plusSeconds(elapsedInterval).getEpochSecond();
    final List<TaskDocument> documentsByScheduledTime = this.mongoTaskRepositoryImpl
        .getDocumentsByScheduledTime(startSeconds, elapsedSeconds);

    documentsByScheduledTime.stream().parallel()
        .forEach(document -> {
          Task task = MongoUtil.documentToTaskMapper(document);
          log.info("Daemon thread, executing task={}", task);
          long executionDelaySeconds = this.taskServiceImpl.computeExecutionDelayTime(task);
          this.taskServiceImpl.executeTask(task, executionDelaySeconds);
        });
  }
}
