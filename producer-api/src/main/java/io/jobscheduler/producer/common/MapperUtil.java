package io.jobscheduler.producer.common;

import io.jobscheduler.models.Task;
import io.jobscheduler.producer.models.Action;
import io.jobscheduler.producer.models.resources.JobRequest;
import java.time.ZoneOffset;

public class MapperUtil {

  public static Task jobRequestToTask(JobRequest jobRequest, Action jobType) {
    final Task task = new Task();
    task.setRequestId(jobRequest.getRequestId());
    task.setTaskRequest(jobRequest.getTaskRequest());
    task.setJobScheduleTimeUtc(jobRequest.getJobScheduleTimeUtc());
    task.setPriority(jobRequest.getPriority());
    task.setJobType(jobType.name());

    return task;
  }

}
