package io.jobscheduler.scheduler.repository;

import io.jobscheduler.models.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MongoTaskRepositoryImpl implements ITaskRepository<Task> {
  //TODO Change Task to Mongo specific Index
  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public boolean save(Task data) {
    log.info("Persisting data in Mongo={}", data);
    return false;
  }

  @Override
  public void act(Task task) {
    this.save(task);
  }
}
