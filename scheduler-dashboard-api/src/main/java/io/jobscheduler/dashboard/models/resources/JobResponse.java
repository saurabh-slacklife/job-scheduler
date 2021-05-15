package io.jobscheduler.dashboard.models.resources;

import io.jobscheduler.models.Priority;
import io.jobscheduler.models.TaskStatus;
import java.time.Instant;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
public class JobResponse {

  @NonNull
  private String requestId;
  @NonNull
  private Map<String, String> jobRequest;
  @NonNull
  private Instant jobScheduleAtTimeUtc;
  private Priority priority = Priority.LOW;
  private TaskStatus jobStatus;
  private String jobType;
}
