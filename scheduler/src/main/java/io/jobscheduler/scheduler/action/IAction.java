package io.jobscheduler.scheduler.action;

public interface IAction<T> {

  void act(T task);

}
