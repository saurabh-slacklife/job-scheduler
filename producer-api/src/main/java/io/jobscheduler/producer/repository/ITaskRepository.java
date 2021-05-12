package io.jobscheduler.producer.repository;

public interface ITaskRepository<T> {

  T save(T data);
}
