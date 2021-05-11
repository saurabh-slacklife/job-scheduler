package io.jobscheduler.models;

import java.io.Serializable;
import java.time.Clock;
import java.util.Map;
import lombok.Data;

@Data
public class Task implements Serializable {

  private String jobType;
  private String taskId;
  private Map<String, String> taskRequest;
  private Clock jobScheduleTimeUtc;
  private int priority = Integer.MAX_VALUE;

}
