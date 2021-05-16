package io.jobscheduler.scheduler.processor;

import io.jobscheduler.models.Task;
import io.jobscheduler.scheduler.serde.TaskSerdes;
import io.jobscheduler.scheduler.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskStreamProcessor {

  @Autowired
  TaskService<Task> taskService;

  @Value("${spring.kafka.consumer.topic:job-task-system-dev}")
  private String topic;

  @Autowired
  public void process(final StreamsBuilder streamsBuilder) {

    Serde<String> stringSerde = Serdes.String();
    Serde<Task> taskSerde = TaskSerdes.Task();

    streamsBuilder
        .stream(this.topic, Consumed.with(stringSerde, taskSerde))
        .foreach((key, data) -> {
              taskService.processTask(data);
            }
        );

  }

}
