package io.jobscheduler.scheduler.components;

import io.jobscheduler.models.Task;
import io.jobscheduler.scheduler.models.document.TaskDocument;
import io.jobscheduler.scheduler.repository.MongoTaskRepositoryImpl;
import io.jobscheduler.scheduler.service.TaskServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {TaskServiceImpl.class, MongoTaskRepositoryImpl.class})
@TestPropertySource(properties = {"task.daemon.execution.interval=5"})
class DaemonTaskSchedulerTest {

  @SpyBean
  DaemonTaskScheduler daemonTaskScheduler;

  @MockBean
  TaskServiceImpl taskService;

  @MockBean
  MongoTaskRepositoryImpl mongoTaskRepository;

  @Test
  public void testValid() {
    List<TaskDocument> taskDocuments = new ArrayList<>();
    final Task task = new Task();

    when(mongoTaskRepository.getDocumentsByScheduledTime(anyLong(), anyLong()))
        .thenReturn(taskDocuments);
    when(taskService.computeExecutionDelayTime(task)).thenReturn(10L);
    doNothing().when(taskService).executeTask(task, 10L);

    await().atMost(new Duration(30L, TimeUnit.SECONDS))
        .untilAsserted(() -> verify(daemonTaskScheduler, atLeast(1)).scheduleTask());
  }

}