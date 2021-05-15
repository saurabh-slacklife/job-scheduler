package io.jobscheduler.producer.repository;

import io.jobscheduler.models.TaskStatus;
import io.jobscheduler.producer.models.document.TaskDocument;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class MongoTaskRepositoryImplTest {

  @Test
  public void whenTimeBounded_thenGetFromMongo() {

    MongoTemplate mockMongoTemplate = mock(MongoTemplate.class);
    when(mockMongoTemplate.find(any(), any())).thenReturn(new ArrayList<>());

    TaskDocument mockTaskDocument = mock(TaskDocument.class);
    doReturn("docId").when(mockTaskDocument).getId();

    MongoTaskRepositoryImpl mockMongoTaskRepository = mock(MongoTaskRepositoryImpl.class);
    when(mockMongoTaskRepository.save(mockTaskDocument)).thenReturn(mockTaskDocument);

    List<TaskDocument> responseList = new ArrayList<>();
    responseList.add(mockTaskDocument);

    final Instant instant = Clock.systemUTC().instant().plusSeconds(5);
    doReturn(responseList).when(mockMongoTaskRepository)
        .getDocumentsByScheduledTime(instant.getEpochSecond(),
            instant.plusSeconds(20).getEpochSecond());

    List<TaskDocument> result = mockMongoTaskRepository
        .getDocumentsByScheduledTime(instant.getEpochSecond(),
            instant.plusSeconds(20).getEpochSecond());
    assertEquals(1, result.size());
  }

  @Test
  public void whenTimeSecondsNotMatch_thenGetFromMongo() {

    MongoTemplate mockMongoTemplate = mock(MongoTemplate.class);
    when(mockMongoTemplate.find(any(), any())).thenReturn(new ArrayList<>());

    TaskDocument mockTaskDocument = mock(TaskDocument.class);
    doReturn("docId").when(mockTaskDocument).getId();

    MongoTaskRepositoryImpl mockMongoTaskRepository = mock(MongoTaskRepositoryImpl.class);
    when(mockMongoTaskRepository.save(mockTaskDocument)).thenReturn(mockTaskDocument);

    List<TaskDocument> responseList = new ArrayList<>();

    final Instant instant = Clock.systemUTC().instant().plusSeconds(5);
    doReturn(responseList).when(mockMongoTaskRepository)
        .getDocumentsByScheduledTime(instant.getEpochSecond(),
            instant.plusSeconds(20).getEpochSecond());

    List<TaskDocument> result = mockMongoTaskRepository
        .getDocumentsByScheduledTime(instant.getEpochSecond(),
            instant.plusSeconds(20).getEpochSecond());
    assertEquals(0, result.size());
  }

  @Test
  public void whenTaskDocumentPersistCall_thenSaveInMongo() {

    TaskDocument taskDocument = new TaskDocument();

    MongoTemplate mockMongoTemplate = mock(MongoTemplate.class);
    doReturn(taskDocument).when(mockMongoTemplate).save(taskDocument);

    MongoTaskRepositoryImpl mockMongoTaskRepository = mock(MongoTaskRepositoryImpl.class);
    doReturn(taskDocument).when(mockMongoTaskRepository).save(taskDocument);

    TaskDocument responseDocument = mockMongoTaskRepository.save(taskDocument);

    assertEquals(taskDocument, responseDocument);


  }

  @Test
  public void whenValidIdStatusEpoch_thenFindAndUpdateInMongo() {

    TaskDocument taskDocument = new TaskDocument();

    Query mockQuery = mock(Query.class);
    MongoTemplate mockMongoTemplate = mock(MongoTemplate.class);
    doReturn(taskDocument).when(mockMongoTemplate).findOne(mockQuery, TaskDocument.class);

    MongoTaskRepositoryImpl mockMongoTaskRepository = mock(MongoTaskRepositoryImpl.class);
    doReturn(taskDocument).when(mockMongoTaskRepository).save(taskDocument);

    mockMongoTaskRepository.update("ObjectID", TaskStatus.RUNNING, 12432542534L);
  }
}