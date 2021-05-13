package io.jobscheduler.producer.repository;

import io.jobscheduler.producer.models.document.TaskDocument;
import io.jobscheduler.producer.service.TaskScheduleServiceImpl;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {MongoTemplate.class})
@ActiveProfiles({"default", "dev", "qa", "prod"})
class MongoTaskRepositoryImplTest {

  @Mock
  MongoTemplate mockMongoTemplate;
  @MockBean
  MongoTaskRepositoryImpl mockMongoTaskRepository;


  @Test
  public void whenTimesBounded_theGetFromMongo() {
    when(mockMongoTemplate.find(any(), any())).thenReturn(new ArrayList<>());
    final Instant instant = Clock.systemUTC().instant().plusSeconds(5);
    List<TaskDocument> result = mockMongoTaskRepository
        .getDocumentsByScheduledTime(instant.getEpochSecond(),
            instant.plusSeconds(20).getEpochSecond());
    assertNotNull(result);

  }
}