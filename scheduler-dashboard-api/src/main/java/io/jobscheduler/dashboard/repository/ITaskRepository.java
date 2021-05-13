package io.jobscheduler.dashboard.repository;

import io.jobscheduler.dashboard.models.Action;
import java.util.List;

public interface ITaskRepository<T> {

  List<T> findByJobType(Action jobType);

  T findById(String jobId);

  List<T> findByScheduledTime(long startTime, long elapsedTime);
}
