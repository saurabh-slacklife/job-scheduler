package io.jobscheduler.scheduler.repository;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
import io.jobscheduler.models.TaskStatus;
import io.jobscheduler.scheduler.models.document.TaskDocument;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MongoTaskRepositoryImpl implements ITaskRepository<TaskDocument> {

  @Autowired
  private MongoTemplate mongoTemplate;

  /**
   * Persist taskDocument
   *
   * @param data {@link TaskDocument}
   * @return boolean
   */
  @Override
  public TaskDocument save(TaskDocument data) {
    log.info("Persisting data in Mongo={}", data);
    data = mongoTemplate.save(data);
    return data;
  }

  public void update(String objectId, TaskStatus status) {
    final Criteria taskCriteria = Criteria
        .where("_id").is(objectId);
    final Query query = Query.query(taskCriteria);

    Update update = new Update();
    update.set("taskStatus", status);

    mongoTemplate.upsert(query, update, TaskDocument.class);
  }

  public void update(String taskId, TaskStatus status, long scheduledTime) {

    final Criteria taskCriteria = Criteria
        .where("_id").is(taskId);
    final Query query = Query.query(taskCriteria);

    Update update = new Update();
    update.set("taskStatus", status);
    update.set("jobScheduleTimeSeconds", scheduledTime);

    mongoTemplate.upsert(query, update, TaskDocument.class);
  }

  /**
   * Searches documents based on range of Start time and Elapsed time
   *
   * @param startTime   inclusive
   * @param elapsedTime exclusive
   * @return List<TaskDocument>
   */
  public List<TaskDocument> getDocumentsByScheduledTime(long startTime, long elapsedTime) {
    final Criteria taskCriteria = Criteria
        .where("taskStatus").is(TaskStatus.SCHEDULED)
        .andOperator(
            Criteria.where("jobScheduleTimeSeconds").gte(startTime),
            Criteria.where("jobScheduleTimeSeconds").lt(elapsedTime)
        );
    final Query query = Query.query(taskCriteria);
    return mongoTemplate.find(query, TaskDocument.class);
  }


  @Override
  public boolean act(TaskDocument task) {
    return this.save(task) != null;
  }


}
