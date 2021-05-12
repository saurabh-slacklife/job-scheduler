package io.jobscheduler.producer.repository;

import io.jobscheduler.models.Task;
import io.jobscheduler.producer.models.document.TaskDocument;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.lang.NonNull;

public class MongoUtil {

  public static TaskDocument taskToDocumentMapper(@NonNull Task task) {
    final TaskDocument taskDocument = new TaskDocument();
    taskDocument.setTaskId(task.getTaskId());
    taskDocument.setTaskRequest(task.getTaskRequest());
    taskDocument.setJobType(task.getJobType());
    taskDocument.setJobScheduleTimeSeconds(task.getJobScheduleTimeUtc().getEpochSecond());
    taskDocument.setPriority(task.getPriority());
    return taskDocument;
  }

  public static Task documentToTaskMapper(@NonNull TaskDocument taskDocument) {
    final Task task = new Task();
    task.setTaskId(taskDocument.getTaskId());
    task.setJobType(taskDocument.getJobType());
    task.setPriority(taskDocument.getPriority());
    task.setTaskRequest(taskDocument.getTaskRequest());

    final LocalDateTime localDateTime = LocalDateTime
        .ofEpochSecond(taskDocument.getJobScheduleTimeSeconds(), 0,
            ZoneOffset.UTC);

    task.setJobScheduleTimeUtc(localDateTime.toInstant(ZoneOffset.UTC).plusSeconds(10));
    return task;
  }

}
