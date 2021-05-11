package io.jobscheduler.producer.models.resources;

import java.time.Clock;
import java.util.Map;
import lombok.Data;

@Data
public class JobRequest {

  private String taskId;
  private Map<String, String> taskRequest;
  private Clock jobScheduleTimeUtc;
  private int priority = Integer.MAX_VALUE;

}
