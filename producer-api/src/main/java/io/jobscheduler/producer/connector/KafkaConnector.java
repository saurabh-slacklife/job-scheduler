package io.jobscheduler.producer.connector;

import io.jobscheduler.models.Task;
import io.jobscheduler.producer.configuration.KafkaConfiguration;
import io.jobscheduler.producer.errors.ErrorMessages;
import io.jobscheduler.producer.errors.JobSchedulingException;
import io.jobscheduler.producer.models.Action;
import java.time.Duration;
import java.util.concurrent.Future;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConnector {

  Producer<String, Task> producer;
  String topicName;

  public KafkaConnector(@Qualifier("kafkaConfiguration") KafkaConfiguration kafkaConfiguration) {
    this.producer = new KafkaProducer<>(kafkaConfiguration.toMap());
    this.topicName = kafkaConfiguration.getTopicName();
  }

  public void publishTask(Action key, Task task) {
    ProducerRecord<String, Task> record = new ProducerRecord<>(
        this.topicName, key.name(), task);
    try {
      producer.send(record);
    } catch (KafkaException ex) {
      log.error(ErrorMessages.KAFKA_ERROR.getErrorMessage(), ex);
      throw new JobSchedulingException(ErrorMessages.KAFKA_ERROR.getErrorMessage(),
          ErrorMessages.KAFKA_ERROR
              .getErrorCode(), ex);
    }
  }

  public void close() {
    if (this.producer != null) {
      this.producer.close(Duration.ofSeconds(5));
    }
  }

  @PreDestroy
  public void shutDownHook() {
    log.info("Starting to close KafkaConnector with wait ts");
    this.close();
    try {
      Thread.sleep(120);
      log.info("Closing now KafkaConnector with wait ts");
    } catch (InterruptedException e) {
      log.error("Unable to close", e);
    }
  }


}
