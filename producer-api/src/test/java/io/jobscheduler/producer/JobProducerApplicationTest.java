package io.jobscheduler.producer;

import io.jobscheduler.producer.configuration.KafkaConfiguration;
import io.jobscheduler.producer.connector.KafkaConnector;
import io.jobscheduler.producer.repository.MongoTaskRepositoryImpl;
import io.jobscheduler.producer.service.TaskScheduleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"dev","qa","default","prod"})
class JobProducerApplicationTest {

  @Autowired
  private TaskScheduleServiceImpl taskScheduleService;

  @Autowired
  private KafkaConnector kafkaConnector;

  @Autowired
  private MongoTaskRepositoryImpl mongoTaskRepository;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private KafkaConfiguration kafkaConfiguration;

  @Test
  void validateContext() {
    assertNotNull(taskScheduleService);
    assertNotNull(kafkaConnector);
    assertNotNull(mongoTaskRepository);
    assertNotNull(mongoTemplate);
    assertNotNull(kafkaConfiguration);

  }
}