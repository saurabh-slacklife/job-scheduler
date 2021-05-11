package io.jobscheduler.scheduler.serde;

import io.jobscheduler.models.Task;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class TaskDeserializer implements Deserializer<Task> {

  @Override
  public Task deserialize(String topic, byte[] data) {

    try {
      ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
      Task task = (Task) objectInputStream.readObject();
      objectInputStream.close();
      return task;
    } catch (IOException | ClassNotFoundException e) {
      log.error("Unable to deserialize byte stream {}", data.toString(), e);
      throw new SerializationException(
          "Error when deserializing byte[] due to unsupported encoding ");
    }
  }
}
