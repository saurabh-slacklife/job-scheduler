package io.jobscheduler.producer.repository;

import io.jobscheduler.models.TaskStatus;
import io.jobscheduler.producer.models.document.TaskDocument;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
