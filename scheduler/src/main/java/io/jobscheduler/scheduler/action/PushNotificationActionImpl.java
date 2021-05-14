package io.jobscheduler.scheduler.action;

import io.jobscheduler.models.Task;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PushNotificationActionImpl implements INotification<Task> {

  @Override
  public boolean act(Task data) {
    return this.sendNotification(data) != null;
  }

  @Override
  public String sendNotification(Task data) {
    log.info("Push Notification Send={}", data);
    return "hello";
  }
}
