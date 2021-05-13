package io.jobscheduler.dashboard.repository;

import io.jobscheduler.dashboard.models.document.TaskDocument;
import io.jobscheduler.dashboard.models.resources.JobResponse;
import io.jobscheduler.models.Task;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.lang.NonNull;

public class MongoUtil {

  public static JobResponse documentToDashboardResponseMapper(@NonNull TaskDocument taskDocument) {
    final JobResponse jobResponse = new JobResponse();
    jobResponse.setRequestId(taskDocument.getRequestId());
    jobResponse.setJobRequest(taskDocument.getTaskRequest());
    jobResponse.setPriority(taskDocument.getPriority());
    jobResponse.setJobType(taskDocument.getJobType());

    final LocalDateTime localDateTime = LocalDateTime
        .ofEpochSecond(taskDocument.getJobScheduleTimeSeconds(), 0,
            ZoneOffset.UTC);

    jobResponse.setJobScheduleAtTimeUtc(localDateTime.toInstant(ZoneOffset.UTC).plusSeconds(10));
    return jobResponse;
  }

}
