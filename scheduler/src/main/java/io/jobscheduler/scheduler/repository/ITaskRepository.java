package io.jobscheduler.scheduler.repository;

import io.jobscheduler.scheduler.action.IAction;

public interface ITaskRepository<T> extends IAction<T> {

  T save(T data);
}
