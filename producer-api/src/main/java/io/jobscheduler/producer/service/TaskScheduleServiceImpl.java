package io.jobscheduler.producer.service;

import io.jobscheduler.models.Task;
import io.jobscheduler.producer.connector.KafkaConnector;
import io.jobscheduler.producer.errors.ErrorMessages;
import io.jobscheduler.producer.errors.InvalidRequestException;
import io.jobscheduler.producer.errors.InvalidRequestScheduleException;
import io.jobscheduler.producer.models.resources.JobRequest;
import java.time.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class TaskScheduleServiceImpl implements
    ITaskScheduleService {

  @Autowired
  Clock clock;

  @Autowired
  KafkaConnector kafkaConnector;

  @Override
  public String processTask(@NonNull JobRequest jobRequest, String jobType) {
    this.validateTask(jobRequest);
    this.kafkaConnector.publishTask(jobType, mapper(jobRequest, jobType));
    return jobType + jobRequest.getTaskId();
  }

  private Task mapper(JobRequest jobRequest, String jobType) {
    final Task task = new Task();
    task.setJobType(jobType);
    task.setTaskId(jobRequest.getTaskId());
    task.setTaskRequest(jobRequest.getTaskRequest());
    task.setPriority(jobRequest.getPriority());
    task.setJobScheduleTimeUtc(jobRequest.getJobScheduleTimeUtc());
    return task;
  }

  private void validateTask(@NonNull JobRequest jobRequest) {
    if (null == jobRequest) {
      throw new InvalidRequestException(ErrorMessages.INVALID_REQUEST.getErrorMessage(),
          ErrorMessages.INVALID_REQUEST.getErrorCode());
    }

    if (null != jobRequest.getJobScheduleTimeUtc() && clock.instant()
        .isAfter(jobRequest.getJobScheduleTimeUtc().instant())) {
      throw new InvalidRequestScheduleException(
          ErrorMessages.SCHEDULED_UTC_ELAPSED.getErrorMessage(),
          ErrorMessages.SCHEDULED_UTC_ELAPSED.getErrorCode());
    }

  }
}
