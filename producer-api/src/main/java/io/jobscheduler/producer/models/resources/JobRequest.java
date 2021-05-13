package io.jobscheduler.producer.models.resources;

import io.jobscheduler.models.Priority;
import java.time.Instant;
import java.util.Map;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.lang.NonNull;

@Data
public class JobRequest {

  @NonNull
  private String requestId;
  @NonNull
  private Map<String, String> taskRequest;
  @NonNull
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private Instant jobScheduleTimeUtc;
  private Priority priority = Priority.LOW;

}
