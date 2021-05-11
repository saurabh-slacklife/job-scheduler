package io.jobscheduler.producer.configuration;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
@Configuration("kafkaConfiguration")
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfiguration {

  private String bootstrapServer;
  private String topicName;
  private String acks;
  private String keySerializer;
  private String valueSerializer;

  public Map<String, Object> toMap() {
    Map<String, Object> kafkaPropMap = new HashMap<>();
    kafkaPropMap.put("bootstrap.servers", bootstrapServer);
    kafkaPropMap.put("topic", topicName);
    kafkaPropMap.put("acks", acks);
    kafkaPropMap.put("key.serializer", keySerializer);
    kafkaPropMap.put("value.serializer", valueSerializer);
    return kafkaPropMap;
  }

}
