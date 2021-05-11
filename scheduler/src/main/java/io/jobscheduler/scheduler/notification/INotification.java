package io.jobscheduler.scheduler.notification;

import io.jobscheduler.scheduler.action.IAction;

public interface INotification<T> extends IAction<T> {

  public String sendNotification(T data);

}
