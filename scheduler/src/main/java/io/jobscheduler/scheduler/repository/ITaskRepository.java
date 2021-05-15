package io.jobscheduler.scheduler.repository;

import io.jobscheduler.models.TaskStatus;
import java.util.List;

public interface ITaskRepository<T> {

  T save(T data);
  void update(String objectId, TaskStatus status);
  List<T> getDocumentsByScheduledTime(long startTime, long elapsedTime);
}
