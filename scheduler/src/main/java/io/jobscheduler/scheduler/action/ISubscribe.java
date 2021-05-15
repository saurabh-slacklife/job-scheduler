package io.jobscheduler.scheduler.action;

public interface ISubscribe<T> extends IAction<T> {

  boolean subscribe(T data);

}
