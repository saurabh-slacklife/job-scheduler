package io.jobscheduler.models;

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import lombok.Data;

@Data
public class Task implements Serializable {

  private String jobType;
  private String jobId;
  private String requestId;
  private Map<String, String> taskRequest;
  private Instant jobScheduleTimeUtc;
  private Priority priority;

}
