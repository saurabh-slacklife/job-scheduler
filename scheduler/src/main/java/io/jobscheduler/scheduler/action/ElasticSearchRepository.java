package io.jobscheduler.scheduler.action;

import io.jobscheduler.models.Task;

public class ElasticSearchRepository implements IRepository<Task> {

  @Override
  public boolean act(Task task) throws RuntimeException {
    //    TODO Handle the implementation logic
    return this.save(task);
  }

  @Override
  public boolean save(Task data) {
    //    TODO Handle the implementation logic
    return false;
  }
}
