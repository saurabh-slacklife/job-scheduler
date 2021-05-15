package io.jobscheduler.scheduler.action;

import io.jobscheduler.models.Task;

public class EmailSubscribeImpl implements ISubscribe<Task>{

  @Override
  public boolean act(Task task) throws RuntimeException {
//    TODO Handle the implementation logic
    return this.subscribe(task);
  }

  @Override
  public boolean subscribe(Task data) {
    //    TODO Handle the implementation logic
    return true;
  }
}
