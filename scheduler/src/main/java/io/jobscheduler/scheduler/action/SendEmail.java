package io.jobscheduler.scheduler.action;

public class SendEmail<T> implements IAction<T> {

  @Override
  public boolean act(T task) throws RuntimeException {
    return false;
  }
}
