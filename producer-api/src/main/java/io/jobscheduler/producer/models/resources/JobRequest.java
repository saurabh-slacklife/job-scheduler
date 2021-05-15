package io.jobscheduler.producer.models.resources;

import io.jobscheduler.models.Priority;
import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
  private Instant jobScheduleTimeUtc;

  private Priority priority = Priority.LOW;

}
