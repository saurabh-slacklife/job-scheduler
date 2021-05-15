package io.jobscheduler.scheduler.action;

public interface IRepository<T> extends IAction<T> {

  boolean save(T data);

}
