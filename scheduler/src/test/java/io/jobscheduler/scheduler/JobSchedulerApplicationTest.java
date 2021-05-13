package io.jobscheduler.scheduler;

import io.jobscheduler.scheduler.components.DaemonTaskScheduler;
import io.jobscheduler.scheduler.components.ExecutorServiceEngine;
import io.jobscheduler.scheduler.processor.TaskStreamProcessor;
import io.jobscheduler.scheduler.repository.MongoTaskRepositoryImpl;
import io.jobscheduler.scheduler.service.TaskServiceImpl;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"dev", "qa", "default", "prod"})
class JobSchedulerApplicationTest {

  @Autowired
  private DaemonTaskScheduler daemonTaskScheduler;

  @Autowired
  private ThreadPoolExecutor priorityThreadPoolExecutor;

  @Autowired
  private TaskServiceImpl taskService;

  @Autowired
  private MongoTaskRepositoryImpl mongoTaskRepository;

  @Autowired
  private ScheduledThreadPoolExecutor jobScheduledThreadPoolExecutor;

  @Autowired
  private ExecutorServiceEngine executorServiceEngine;

  @Autowired
  private TaskStreamProcessor taskStreamProcessor;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Test
  public void validateContextLoads() {
    assertThat(daemonTaskScheduler).isNotNull();
    assertThat(priorityThreadPoolExecutor).isNotNull();
    assertThat(taskService).isNotNull();
    assertThat(mongoTaskRepository).isNotNull();
    assertThat(jobScheduledThreadPoolExecutor).isNotNull();
    assertThat(executorServiceEngine).isNotNull();
    assertThat(taskStreamProcessor).isNotNull();
    assertThat(mongoTemplate).isNotNull();

  }

}