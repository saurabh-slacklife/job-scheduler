package io.jobscheduler.scheduler.serde;

import io.jobscheduler.models.Task;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class TaskSerdes extends Serdes {

  static public final Serde<Task> Task() {
    return Serdes.serdeFrom(new TaskSerializer(), new TaskDeserializer());
  }

}
