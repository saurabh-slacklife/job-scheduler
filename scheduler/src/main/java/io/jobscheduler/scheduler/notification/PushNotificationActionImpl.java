package io.jobscheduler.scheduler.notification;

import io.jobscheduler.models.Task;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PushNotificationActionImpl implements INotification<Task> {
  //TODO Change Task to ES specific Index
  @Override
  public void act(Task data) {
    this.sendNotification(data);
  }

  @Override
  public String sendNotification(Task data) {
    log.info("Push Notification Send={}", data);
    return data.getTaskId();
  }
}
