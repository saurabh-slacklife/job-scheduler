package io.jobscheduler.scheduler.repository;

public interface ITaskRepository<T> {

  T save(T data);
}
