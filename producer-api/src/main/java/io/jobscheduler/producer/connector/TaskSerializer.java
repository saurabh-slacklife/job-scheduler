package io.jobscheduler.producer.connector;

import io.jobscheduler.models.Task;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
public class TaskSerializer implements
    Serializer<Task> {

  @Override
  public byte[] serialize(String topic, Task data) {
    if (null == data) {
      return new byte[0];
    }

    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
      outputStream.writeObject(data);
      outputStream.flush();
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      log.error("Unable to serialize object {}", data, e);
      throw new SerializationException(
          "Error when serializing User Data to byte[] due to unsupported encoding ");
    }
  }
}
