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
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TaskScheduleServiceImpl.class})
@ActiveProfiles({"default", "dev", "qa", "prod"})
class TaskScheduleServiceImplTest {

  @Autowired
  TaskScheduleServiceImpl taskScheduleService;

  @MockBean
  KafkaConnector kafkaConnector;

  @MockBean
  MongoTaskRepositoryImpl mockMongoTaskRepository;

  @Mock
  MongoTemplate mockMongoTemplate;

  @Mock
  TaskDocument mockTaskDocument;

  JobRequest jobRequest;

  @BeforeEach
  void setUpJobRequest() {
    final LocalDateTime localDateTime = LocalDateTime.now(Clock.systemUTC()).plusSeconds(10);
    jobRequest = new JobRequest();
    jobRequest.setTaskRequest(new HashMap<>());
    jobRequest.setRequestId("taskID");
    jobRequest.setJobScheduleTimeUtc(localDateTime);
  }

  @Test
  void whenTaskProcessWithSuccess_validateUri() {
    Task task = MapperUtil.jobRequestToTask(jobRequest, Action.email);
    when(mockTaskDocument.getId()).thenReturn("jobId");

    when(mockMongoTemplate.save(mockTaskDocument)).thenReturn(mockTaskDocument);
    when(mockMongoTaskRepository.save(mockTaskDocument)).thenReturn(mockTaskDocument);

    doNothing().when(kafkaConnector).publishTask(Action.email, task);

    String uri = taskScheduleService.processTask(jobRequest, Action.email);
    assertNotNull(uri);

    String expectedJobId = Action.email.name() + ":";
    String actualUri = new String(Base64.getDecoder().decode(uri), StandardCharsets.UTF_8);
    assertEquals(expectedJobId, actualUri);

  }

  @Test
  void whenKafkaError_thenScheduleTaskAndReturnId() {

    when(mockTaskDocument.getId()).thenReturn("jobId");

    when(mockMongoTemplate.save(mockTaskDocument)).thenReturn(mockTaskDocument);
    when(mockMongoTaskRepository.save(mockTaskDocument)).thenReturn(mockTaskDocument);

    doThrow(new JobSchedulingException("Message", 500)).when(kafkaConnector)
        .publishTask(any(), any());

    String uri = taskScheduleService.processTask(jobRequest, Action.email);
    assertNotNull(uri);
    String expectedJobId = Action.email.name() + ":";
    String actualUri = new String(Base64.getDecoder().decode(uri), StandardCharsets.UTF_8);
    assertEquals(expectedJobId, actualUri);

  }

  @Test
  void whenTaskPastScheduledTime_validateThrowException() {
    final LocalDateTime localDateTime = LocalDateTime.now(Clock.systemUTC()).minusSeconds(10);
    jobRequest.setJobScheduleTimeUtc(localDateTime);
    InvalidRequestScheduleException ex = assertThrows(InvalidRequestScheduleException.class,
        () -> taskScheduleService
            .processTask(jobRequest, Action.email));

    assertEquals(ex.getMessage(), ErrorMessages.SCHEDULED_UTC_ELAPSED.getErrorMessage());
    assertEquals(ex.getErrorCode(), ErrorMessages.SCHEDULED_UTC_ELAPSED.getErrorCode());

  }

  @Test
  void whenInvaidJobRequest_throwException() {
    InvalidRequestException ex = assertThrows(InvalidRequestException.class,
        () -> taskScheduleService
            .processTask(null, Action.email));

    assertEquals(ex.getMessage(), ErrorMessages.INVALID_REQUEST.getErrorMessage());
    assertEquals(ex.getErrorCode(), ErrorMessages.INVALID_REQUEST.getErrorCode());

  }


}