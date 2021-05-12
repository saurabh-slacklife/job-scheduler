package io.jobscheduler.scheduler.components;

import io.jobscheduler.models.Task;
import io.jobscheduler.scheduler.models.document.TaskDocument;
import io.jobscheduler.scheduler.repository.MongoTaskRepositoryImpl;
import io.jobscheduler.scheduler.repository.MongoUtil;
import io.jobscheduler.scheduler.service.TaskServiceImpl;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DaemonTaskScheduler {

  @Autowired
  private MongoTaskRepositoryImpl mongoTaskRepositoryImpl;

  @Autowired
  private TaskServiceImpl taskServiceImpl;

  //  fixedRate of 30 seconds from last invocation to start of next
  @Scheduled(fixedRate = 10000)
  public void scheduleTask() {

    log.info("Executed at={}", Clock.systemUTC().instant());
    Instant now = Clock.systemUTC().instant();
    long startSeconds = now.minusSeconds(10).getEpochSecond();
    long elapsedSeconds = now.plusSeconds(30).getEpochSecond();
    final List<TaskDocument> documentsByScheduledTime = this.mongoTaskRepositoryImpl
        .getDocumentsByScheduledTime(startSeconds, elapsedSeconds);

    log.info("Daemon thread, executing document list={}", documentsByScheduledTime.size());

    documentsByScheduledTime.stream().parallel()
        .filter(document -> document != null)
        .forEach(document -> {
          Task task = MongoUtil.documentToTaskMapper(document);
          log.info("Daemon thread, executing task={}", task);
          long executionDelaySeconds = this.taskServiceImpl.computeExecutionDelayTime(task);
          this.taskServiceImpl.executeTask(task, executionDelaySeconds);
        });
  }
}
