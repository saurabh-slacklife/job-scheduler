package io.jobscheduler.scheduler.tasks;

import io.jobscheduler.models.Task;
import io.jobscheduler.models.TaskStatus;
import io.jobscheduler.scheduler.action.Action;
import io.jobscheduler.scheduler.repository.MongoTaskRepositoryImpl;
import java.util.concurrent.Semaphore;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CallableJobTest {

  @Test
  public void whenTaskIsNotNullAndActionSucceeds_thenUpdateStatusSuccess() {
    MongoTaskRepositoryImpl mongoTaskRepository = mock(MongoTaskRepositoryImpl.class);
    Semaphore semaphore = mock(Semaphore.class);

    Task task = new Task();
    task.setJobId("Jobid");
    task.setJobType(Action.email.name());

    doNothing().when(mongoTaskRepository).update(task.getJobId(), TaskStatus.RUNNING);

    final CallableJob callableJob = new CallableJob(task, semaphore, mongoTaskRepository);
    callableJob.run();

    verify(mongoTaskRepository, times(1)).update(task.getJobId(), TaskStatus.FAILED);
    verify(mongoTaskRepository, times(0)).update(task.getJobId(), TaskStatus.SUCCESS);
  }

  @Test
  public void whenTaskIsNotNullAndActionFails_thenUpdateStatusFails() {
    MongoTaskRepositoryImpl mongoTaskRepository = mock(MongoTaskRepositoryImpl.class);
    Semaphore semaphore = mock(Semaphore.class);

    Task task = new Task();
    task.setJobId("Jobid");
    task.setJobType(Action.push_notification.name());

    doNothing().when(mongoTaskRepository).update(task.getJobId(), TaskStatus.RUNNING);

    final CallableJob callableJob = new CallableJob(task, semaphore, mongoTaskRepository);
    callableJob.run();

    verify(mongoTaskRepository, times(0)).update(task.getJobId(), TaskStatus.FAILED);
    verify(mongoTaskRepository, times(1)).update(task.getJobId(), TaskStatus.SUCCESS);
  }

  @Test
  public void whenTaskIsNotNullSemaphoreInterruption_thenUpdateStatusFail()
      throws InterruptedException {

    Semaphore semaphore = mock(Semaphore.class);
    doThrow(new InterruptedException()).when(semaphore).acquire();
    Task task = new Task();
    task.setJobId("Jobid");
    task.setJobType(Action.push_notification.name());

    MongoTaskRepositoryImpl mongoTaskRepository = mock(MongoTaskRepositoryImpl.class);
    doNothing().when(mongoTaskRepository).update(task.getJobId(), TaskStatus.RUNNING);

    final CallableJob callableJob = new CallableJob(task, semaphore, mongoTaskRepository);
    callableJob.run();

    verify(mongoTaskRepository, times(0)).update(task.getJobId(), TaskStatus.FAILED);
    verify(mongoTaskRepository, times(0)).update(task.getJobId(), TaskStatus.SUCCESS);
  }

}