package io.jobscheduler.producer.repository;

import io.jobscheduler.models.TaskStatus;
import io.jobscheduler.producer.models.document.TaskDocument;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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
    TaskDocument resultDocument = mongoTemplate.save(data);
    return resultDocument;
  }

  public void update(String objectId, TaskStatus status, long newScheduledEpoch) {
    final Criteria docCriteria = Criteria
        .where("_id").is(new ObjectId(objectId));
    final Query query = Query.query(docCriteria);
    final TaskDocument taskDoc = mongoTemplate.findOne(query, TaskDocument.class);

    taskDoc.setTaskStatus(status);
    taskDoc.setJobScheduleTimeSeconds(newScheduledEpoch);
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
    final Criteria guideCriteria = Criteria
        .where("taskStatus").is(TaskStatus.SCHEDULED)
        .andOperator(
            Criteria.where("jobScheduleTimeSeconds").gte(startTime),
            Criteria.where("jobScheduleTimeSeconds").lt(elapsedTime)
        );
    final Query query = Query.query(guideCriteria);
    return mongoTemplate.find(query, TaskDocument.class);
  }

}
