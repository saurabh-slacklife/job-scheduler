package io.jobscheduler.producer.service;

import io.jobscheduler.models.Task;
import io.jobscheduler.models.TaskStatus;
import io.jobscheduler.producer.connector.KafkaConnector;
import io.jobscheduler.producer.errors.ErrorMessages;
import io.jobscheduler.producer.errors.InvalidRequestException;
import io.jobscheduler.producer.errors.InvalidRequestScheduleException;
import io.jobscheduler.producer.errors.JobSchedulingException;
import io.jobscheduler.producer.models.document.TaskDocument;
import io.jobscheduler.producer.models.resources.JobRequest;
import io.jobscheduler.producer.repository.MongoTaskRepositoryImpl;
import io.jobscheduler.producer.repository.MongoUtil;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class TaskScheduleServiceImpl implements
    ITaskScheduleService {

  @Autowired
  KafkaConnector kafkaConnector;

  @Autowired
  MongoTaskRepositoryImpl mongoTaskRepository;

  @Override
  public String processTask(@NonNull JobRequest jobRequest, String jobType) {
    this.validateTask(jobRequest);
    final Task task = mapper(jobRequest, jobType);
    final TaskDocument taskDocument = MongoUtil.taskToDocumentMapper(task);
    taskDocument.setTaskStatus(TaskStatus.QUEUED);
    String docId = this.persistJob(taskDocument);
    try {
      task.setTaskId(docId);
      this.kafkaConnector.publishTask(jobType, task);
    } catch (JobSchedulingException ex) {
      taskDocument.setTaskStatus(TaskStatus.SCHEDULED);
      this.persistJob(taskDocument);
    }
    return Base64.getEncoder()
        .encodeToString((jobType + ":" + docId).getBytes(StandardCharsets.UTF_8));
  }

  private String persistJob(TaskDocument taskDocument) {
    return this.mongoTaskRepository.save(taskDocument).getId();
  }

  private Task mapper(JobRequest jobRequest, String jobType) {
    final Task task = new Task();
    task.setJobType(jobType);
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

    if (null != jobRequest.getJobScheduleTimeUtc() && Clock.systemUTC().instant()
        .isAfter(jobRequest.getJobScheduleTimeUtc())) {
      throw new InvalidRequestScheduleException(
          ErrorMessages.SCHEDULED_UTC_ELAPSED.getErrorMessage(),
          ErrorMessages.SCHEDULED_UTC_ELAPSED.getErrorCode());
    }

  }
}
