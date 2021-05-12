package io.jobscheduler.scheduler.notification;

import io.jobscheduler.models.Task;
import io.jobscheduler.scheduler.models.notification.TaskNotification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PushNotificationActionImpl implements INotification<Task> {

  //TODO Change Task to ES specific Index
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
