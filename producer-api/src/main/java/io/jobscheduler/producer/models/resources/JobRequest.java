package io.jobscheduler.producer.models.resources;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.jobscheduler.models.Priority;
import io.jobscheduler.producer.common.LocalDateTimeDeserializer;
import io.jobscheduler.producer.common.LocalDateTimeSerializer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {

  @NonNull
  private String requestId;

  @NonNull
  private Map<String, String> taskRequest;

  @NonNull
//  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime jobScheduleTimeUtc;

  private Priority priority = Priority.LOW;

}
