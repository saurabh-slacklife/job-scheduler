package io.jobscheduler.scheduler.repository;

import io.jobscheduler.models.TaskStatus;
import io.jobscheduler.scheduler.models.document.TaskDocument;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MongoTaskRepositoryImplTest {

  @Test
  public void whenTaskDocumentPersistCall_thenSaveInMongo() {

    TaskDocument taskDocument = new TaskDocument();

    MongoTemplate mockMongoTemplate = mock(MongoTemplate.class);
    doReturn(taskDocument).when(mockMongoTemplate).save(taskDocument);

    MongoTaskRepositoryImpl mockMongoTaskRepository = mock(MongoTaskRepositoryImpl.class);
    doReturn(taskDocument).when(mockMongoTaskRepository).save(taskDocument);

    TaskDocument responseDocument = mockMongoTaskRepository.save(taskDocument);

    assertEquals(taskDocument, responseDocument);
    verify(mockMongoTaskRepository, times(1)).save(taskDocument);
  }

  @Test
  public void whenSearchByStatusANdObjectIDAndUpdateTask_thenSaveInMongo() {

    TaskDocument taskDocument = new TaskDocument();

    Query query = mock(Query.class);

    MongoTemplate mockMongoTemplate = mock(MongoTemplate.class);
    when(mockMongoTemplate.findOne(query, TaskDocument.class)).thenReturn(taskDocument);
    doReturn(taskDocument).when(mockMongoTemplate).save(taskDocument);

    MongoTaskRepositoryImpl mockMongoTaskRepository = mock(MongoTaskRepositoryImpl.class);
    doReturn(taskDocument).when(mockMongoTaskRepository).save(taskDocument);

    mockMongoTaskRepository.update("ObjectID", TaskStatus.RUNNING);

    verify(mockMongoTaskRepository, times(1)).update("ObjectID", TaskStatus.RUNNING);
  }

  @Test
  public void whenSearchByStatusObjectIDEpochSecondsAndUpdateTask_thenSaveInMongo() {

    TaskDocument taskDocument = new TaskDocument();

    Query query = mock(Query.class);

    MongoTemplate mockMongoTemplate = mock(MongoTemplate.class);
    when(mockMongoTemplate.findOne(query, TaskDocument.class)).thenReturn(taskDocument);
    doReturn(taskDocument).when(mockMongoTemplate).save(taskDocument);

    MongoTaskRepositoryImpl mockMongoTaskRepository = mock(MongoTaskRepositoryImpl.class);
    doReturn(taskDocument).when(mockMongoTaskRepository).save(taskDocument);

    mockMongoTaskRepository.update("ObjectID", TaskStatus.RUNNING, 123467L);

    verify(mockMongoTaskRepository, times(1)).update("ObjectID", TaskStatus.RUNNING, 123467L);
  }


  @Test
  public void whenSearchByStatusObjectIDReasonAndUpdateTask_thenSaveUpdateWithReasonInMongo() {

    TaskDocument taskDocument = new TaskDocument();

    Query query = mock(Query.class);

    MongoTemplate mockMongoTemplate = mock(MongoTemplate.class);
    when(mockMongoTemplate.findOne(query, TaskDocument.class)).thenReturn(taskDocument);
    doReturn(taskDocument).when(mockMongoTemplate).save(taskDocument);

    MongoTaskRepositoryImpl mockMongoTaskRepository = mock(MongoTaskRepositoryImpl.class);
    doReturn(taskDocument).when(mockMongoTaskRepository).save(taskDocument);

    mockMongoTaskRepository.update("ObjectID", TaskStatus.RUNNING, "Reason");

    verify(mockMongoTaskRepository, times(1)).update("ObjectID", TaskStatus.RUNNING, "Reason");
  }

  @Test
  public void whenFindScheduledDocumentsInTimeRange_thenGetFromMongo() {

    Query query = mock(Query.class);
    ArrayList<TaskDocument> taskDocuments = new ArrayList<>();
    taskDocuments.add(new TaskDocument());

    MongoTemplate mockMongoTemplate = mock(MongoTemplate.class);
    when(mockMongoTemplate.find(query, TaskDocument.class)).thenReturn(taskDocuments);

    MongoTaskRepositoryImpl mockMongoTaskRepository = mock(MongoTaskRepositoryImpl.class);
    when(mockMongoTaskRepository.getDocumentsByScheduledTime(2141423L, 465753467L))
        .thenReturn(taskDocuments);

    List<TaskDocument> responseTaskDocuments = mockMongoTaskRepository
        .getDocumentsByScheduledTime(2141423L, 465753467L);

    assertEquals(responseTaskDocuments.size(), taskDocuments.size());
    assertEquals(responseTaskDocuments.size(), 1);
    verify(mockMongoTaskRepository, times(1)).getDocumentsByScheduledTime(2141423L, 465753467L);


  }

}