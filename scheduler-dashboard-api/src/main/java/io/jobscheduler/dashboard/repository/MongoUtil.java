package io.jobscheduler.dashboard.repository;

import io.jobscheduler.dashboard.models.document.TaskDocument;
import io.jobscheduler.dashboard.models.resources.JobResponse;
import java.time.Instant;
import org.springframework.lang.NonNull;

public class MongoUtil {

  public static JobResponse documentToDashboardResponseMapper(@NonNull TaskDocument taskDocument) {
    final JobResponse jobResponse = new JobResponse();
    jobResponse.setRequestId(taskDocument.getRequestId());
    jobResponse.setJobRequest(taskDocument.getTaskRequest());
    jobResponse.setPriority(taskDocument.getPriority());
    jobResponse.setJobType(taskDocument.getJobType());
    jobResponse.setJobStatus(taskDocument.getTaskStatus());

    jobResponse.setJobScheduleAtTimeUtc(Instant.ofEpochSecond(taskDocument.getJobScheduleTimeSeconds()));
    return jobResponse;
  }

}
