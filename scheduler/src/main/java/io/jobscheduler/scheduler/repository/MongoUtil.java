package io.jobscheduler.scheduler.repository;

import io.jobscheduler.models.Task;
import io.jobscheduler.scheduler.models.document.TaskDocument;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.lang.NonNull;

public class MongoUtil {

  public static TaskDocument taskToDocumentMapper(@NonNull Task task) {
    final TaskDocument taskDocument = new TaskDocument();
    taskDocument.setRequestId(task.getRequestId());
    taskDocument.setTaskRequest(task.getTaskRequest());
    taskDocument.setJobScheduleTimeSeconds(task.getJobScheduleTimeUtc().getEpochSecond());
    taskDocument.setPriority(task.getPriority());
    taskDocument.setJobType(task.getJobType());

    return taskDocument;
  }

  public static Task documentToTaskMapper(@NonNull TaskDocument taskDocument) {
    final Task task = new Task();
    task.setJobId(taskDocument.getId());
    task.setRequestId(taskDocument.getRequestId());
    task.setTaskRequest(taskDocument.getTaskRequest());
    task.setJobType(taskDocument.getJobType());
    task.setPriority(taskDocument.getPriority());

    final LocalDateTime localDateTime = LocalDateTime
        .ofEpochSecond(taskDocument.getJobScheduleTimeSeconds(), 0,
            ZoneOffset.UTC);

    task.setJobScheduleTimeUtc(localDateTime.toInstant(ZoneOffset.UTC).plusSeconds(10));
    return task;
  }

}
