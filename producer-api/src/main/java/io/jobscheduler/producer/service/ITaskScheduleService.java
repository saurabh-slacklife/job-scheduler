package io.jobscheduler.producer.service;

import io.jobscheduler.producer.models.Action;
import io.jobscheduler.producer.models.resources.JobRequest;
import org.springframework.lang.NonNull;

public interface ITaskScheduleService {

  String processTask(@NonNull JobRequest jobRequest, Action jobType);
}
