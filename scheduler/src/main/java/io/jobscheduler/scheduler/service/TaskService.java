package io.jobscheduler.scheduler.service;

public interface TaskService<T> {
  void processTask(T task);
}
