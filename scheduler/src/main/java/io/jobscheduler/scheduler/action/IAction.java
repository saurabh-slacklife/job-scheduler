package io.jobscheduler.scheduler.action;

public interface IAction<T> {

  boolean act(T task) throws RuntimeException;

}
