package io.jobscheduler.scheduler.models.notification;

import io.jobscheduler.models.Priority;
import io.jobscheduler.models.TaskStatus;
import java.util.Map;
import lombok.Data;

@Data
public class TaskNotification {

  private String jobType;
  private String taskId;
  private Map<String, String> taskRequest;
  private long jobScheduleTimeSeconds;
  private Priority priority;
  private TaskStatus taskStatus;

}
