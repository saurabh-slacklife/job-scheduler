package io.jobscheduler.producer.models.resources;

import io.jobscheduler.models.Priority;
import java.time.Instant;
import java.util.Map;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Data
public class JobRequest {

  private Map<String, String> taskRequest;
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private Instant jobScheduleTimeUtc;
  private Priority priority;

}
