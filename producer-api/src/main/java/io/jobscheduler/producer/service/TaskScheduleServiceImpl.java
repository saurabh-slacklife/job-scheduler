package io.jobscheduler.producer.service;

import io.jobscheduler.models.Task;
import io.jobscheduler.models.TaskStatus;
import io.jobscheduler.producer.common.MapperUtil;
import io.jobscheduler.producer.connector.KafkaConnector;
import io.jobscheduler.producer.errors.ErrorMessages;
import io.jobscheduler.producer.errors.InvalidRequestException;
import io.jobscheduler.producer.errors.InvalidRequestScheduleException;
import io.jobscheduler.producer.errors.JobSchedulingException;
import io.jobscheduler.producer.models.Action;
import io.jobscheduler.producer.models.document.TaskDocument;
import io.jobscheduler.producer.models.resources.JobRequest;
import io.jobscheduler.producer.repository.MongoTaskRepositoryImpl;
import io.jobscheduler.producer.repository.MongoUtil;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.ZoneOffset;
import java.util.Base64;
import org.apache.kafka.common.KafkaException;
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
  public String processTask(@NonNull JobRequest jobRequest, Action jobType) {
    this.validateTask(jobRequest);
    final Task task = MapperUtil.jobRequestToTask(jobRequest, jobType);
    final TaskDocument taskDocument = MongoUtil.taskToDocumentMapper(task);
    taskDocument.setTaskStatus(TaskStatus.QUEUED);
    String docId = this.persistJob(taskDocument);
    try {
      task.setJobId(docId);
      this.kafkaConnector.publishTask(jobType, task);
    } catch (JobSchedulingException ex) {
      this.updateJob(docId, TaskStatus.SCHEDULED,
          task.getJobScheduleTimeUtc().plusSeconds(20).getEpochSecond());
    }
    return Base64.getEncoder()
        .encodeToString((jobType + ":" + docId).getBytes(StandardCharsets.UTF_8));
  }

  private String persistJob(TaskDocument taskDocument) {
    TaskDocument persistedDoc = this.mongoTaskRepository.save(taskDocument);
    return persistedDoc != null ? persistedDoc.getId() : "";
  }

  private void updateJob(String objectId, TaskStatus updatedStatus, long newScheduledEpoch) {
    this.mongoTaskRepository.update(objectId, updatedStatus, newScheduledEpoch);
  }

  private void validateTask(@NonNull JobRequest jobRequest) {
    if (null == jobRequest) {
      throw new InvalidRequestException(ErrorMessages.INVALID_REQUEST.getErrorMessage(),
          ErrorMessages.INVALID_REQUEST.getErrorCode());
    }

    if (null != jobRequest.getJobScheduleTimeUtc() && Clock.systemUTC().instant()
        .isAfter(jobRequest.getJobScheduleTimeUtc().toInstant(ZoneOffset.UTC))) {
      throw new InvalidRequestScheduleException(
          ErrorMessages.SCHEDULED_UTC_ELAPSED.getErrorMessage(),
          ErrorMessages.SCHEDULED_UTC_ELAPSED.getErrorCode());
    }

  }
}
