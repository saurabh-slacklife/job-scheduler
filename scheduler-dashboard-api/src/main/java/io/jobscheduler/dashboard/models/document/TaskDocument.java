package io.jobscheduler.dashboard.models.document;

import io.jobscheduler.models.Priority;
import io.jobscheduler.models.TaskStatus;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "TaskDocument")
@Data
@NoArgsConstructor
public class TaskDocument {

  @Id
  private String id;
  private String requestId;
  private String jobType;
  private Map<String, String> taskRequest;
  private long jobScheduleTimeSeconds;
  private Priority priority;
  private TaskStatus taskStatus;
  private String reason;

}
