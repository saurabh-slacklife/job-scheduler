package io.jobscheduler.dashboard.repository;

import io.jobscheduler.dashboard.models.Action;
import io.jobscheduler.dashboard.models.document.TaskDocument;
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

  private MongoTemplate mongoTemplate;

  public MongoTaskRepositoryImpl(@Autowired MongoTemplate mongoTemplate){
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public List<TaskDocument> findByJobType(Action jobType) {
    final Criteria docCriteria = Criteria
        .where("jobType").is(jobType);
    final Query query = Query.query(docCriteria);
    return mongoTemplate.find(query, TaskDocument.class);
  }

  @Override
  public TaskDocument findById(String jobId) {
    final Criteria docCriteria = Criteria
        .where("_id").is(new ObjectId(jobId));
    final Query query = Query.query(docCriteria);
    return mongoTemplate.findOne(query, TaskDocument.class);
  }

  /**
   * Searches documents based on range of Start time and Elapsed time
   *
   * @param startTime   inclusive
   * @param elapsedTime exclusive
   * @return List<TaskDocument>
   */
  @Override
  public List<TaskDocument> findByScheduledTime(long startTime, long elapsedTime) {
    final Criteria docCriteria = Criteria
        .where("jobScheduleTimeSeconds").gte(startTime)
        .andOperator(
            Criteria.where("jobScheduleTimeSeconds").lt(elapsedTime)
        );
    final Query query = Query.query(docCriteria);
    return mongoTemplate.find(query, TaskDocument.class);
  }

}
