package io.jobscheduler.producer.service;

import io.jobscheduler.models.Task;
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
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskScheduleServiceImplTest {

  JobRequest jobRequest;

  @BeforeEach
  void setUpJobRequest() {
    final Instant instantNow = Instant.now().plusSeconds(10);
    jobRequest = new JobRequest();
    jobRequest.setTaskRequest(new HashMap<>());
    jobRequest.setRequestId("taskID");
    jobRequest.setJobScheduleTimeUtc(Instant.now());
  }

  @Test
  void whenTaskProcessWithSuccess_validateUri() {
    Task task = MapperUtil.jobRequestToTask(jobRequest, Action.email);

    TaskDocument taskDocument = mock(TaskDocument.class);
    doReturn("docId").when(taskDocument).getId();

    MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    doReturn(taskDocument).when(mongoTemplate).save(taskDocument);

    MongoTaskRepositoryImpl mongoTaskRepository = mock(MongoTaskRepositoryImpl.class);
    when(mongoTaskRepository.save(taskDocument)).thenReturn(taskDocument);

    KafkaConnector mockKafkaConnector = mock(KafkaConnector.class);
    doNothing().when(mockKafkaConnector).publishTask(Action.email, task);

    String expectedUri = Base64.getEncoder()
        .encodeToString((Action.email + ":" + "docId").getBytes(StandardCharsets.UTF_8));

    TaskScheduleServiceImpl taskScheduleService = mock(TaskScheduleServiceImpl.class);
    doReturn(expectedUri).when(taskScheduleService).processTask(jobRequest, Action.email);

    String uri = taskScheduleService.processTask(jobRequest, Action.email);
    assertNotNull(uri);

    String expectedJobId = Action.email.name() + ":docId";
    String actualUri = new String(Base64.getDecoder().decode(uri), StandardCharsets.UTF_8);
    assertEquals(expectedJobId, actualUri);
  }

  @Test
  void whenKafkaError_thenScheduleTaskAndReturnId() {

    Task task = MapperUtil.jobRequestToTask(jobRequest, Action.email);

    TaskDocument taskDocument = mock(TaskDocument.class);
    doReturn("docId").when(taskDocument).getId();

    MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    doReturn(taskDocument).when(mongoTemplate).save(taskDocument);

    MongoTaskRepositoryImpl mongoTaskRepository = mock(MongoTaskRepositoryImpl.class);
    when(mongoTaskRepository.save(taskDocument)).thenReturn(taskDocument);

    KafkaConnector mockKafkaConnector = mock(KafkaConnector.class);
    doNothing().when(mockKafkaConnector).publishTask(Action.email, task);
    doThrow(new JobSchedulingException("Message", 500)).when(mockKafkaConnector)
        .publishTask(any(), any());

    String expectedUri = Base64.getEncoder()
        .encodeToString((Action.email + ":" + "docId").getBytes(StandardCharsets.UTF_8));

    TaskScheduleServiceImpl taskScheduleService = mock(TaskScheduleServiceImpl.class);
    doReturn(expectedUri).when(taskScheduleService).processTask(jobRequest, Action.email);

    String uri = taskScheduleService.processTask(jobRequest, Action.email);
    assertNotNull(uri);

    String expectedJobId = Action.email.name() + ":docId";
    String actualUri = new String(Base64.getDecoder().decode(uri), StandardCharsets.UTF_8);
    assertEquals(expectedJobId, actualUri);
  }

  @Test
  void whenTaskPastScheduledTime_validateThrowException() {

    TaskScheduleServiceImpl mockTaskScheduleService = mock(TaskScheduleServiceImpl.class);
    doThrow(
        new InvalidRequestScheduleException(ErrorMessages.SCHEDULED_UTC_ELAPSED.getErrorMessage(),
            ErrorMessages.SCHEDULED_UTC_ELAPSED.getErrorCode())).when(mockTaskScheduleService)
        .processTask(jobRequest, Action.email);

    final Instant instantNow = Instant.now().minusSeconds(10);
    jobRequest.setJobScheduleTimeUtc(instantNow);

    InvalidRequestScheduleException ex = assertThrows(InvalidRequestScheduleException.class,
        () -> mockTaskScheduleService
            .processTask(jobRequest, Action.email));

    assertEquals(ex.getMessage(), ErrorMessages.SCHEDULED_UTC_ELAPSED.getErrorMessage());
    assertEquals(ex.getErrorCode(), ErrorMessages.SCHEDULED_UTC_ELAPSED.getErrorCode());

  }

  @Test
  void whenInValidJobRequest_throwException() {

    TaskScheduleServiceImpl mockTaskScheduleService = mock(TaskScheduleServiceImpl.class);
    doThrow(
        new InvalidRequestException(ErrorMessages.INVALID_REQUEST.getErrorMessage(),
            ErrorMessages.INVALID_REQUEST.getErrorCode())).when(mockTaskScheduleService)
        .processTask(null, Action.email);

    InvalidRequestException ex = assertThrows(InvalidRequestException.class,
        () -> mockTaskScheduleService
            .processTask(null, Action.email));

    assertEquals(ex.getMessage(), ErrorMessages.INVALID_REQUEST.getErrorMessage());
    assertEquals(ex.getErrorCode(), ErrorMessages.INVALID_REQUEST.getErrorCode());

  }


}