package io.jobscheduler.scheduler.repository;

import io.jobscheduler.models.TaskStatus;
import io.jobscheduler.scheduler.models.document.TaskDocument;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
        .where("_id").is(new ObjectId(objectId));
    final Query query = Query.query(taskCriteria);
    final TaskDocument taskDoc = mongoTemplate.findOne(query, TaskDocument.class);

    taskDoc.setTaskStatus(status);
    this.save(taskDoc);
  }

  public void update(String objectId, TaskStatus status, long scheduledTime) {

    final Criteria docCriteria = Criteria
        .where("_id").is(new ObjectId(objectId));
    final Query query = Query.query(docCriteria);
    final TaskDocument taskDoc = mongoTemplate.findOne(query, TaskDocument.class);

    taskDoc.setTaskStatus(status);
    taskDoc.setJobScheduleTimeSeconds(scheduledTime);
    this.save(taskDoc);
  }

  public void update(String objectId, TaskStatus status, String reason) {

    final Criteria docCriteria = Criteria
        .where("_id").is(new ObjectId(objectId));
    final Query query = Query.query(docCriteria);
    final TaskDocument taskDoc = mongoTemplate.findOne(query, TaskDocument.class);

    taskDoc.setTaskStatus(status);
    taskDoc.setReason(reason);

    this.save(taskDoc);
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
