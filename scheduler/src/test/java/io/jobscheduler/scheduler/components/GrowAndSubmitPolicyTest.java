package io.jobscheduler.scheduler.components;

import io.jobscheduler.scheduler.tasks.CallableJob;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GrowAndSubmitPolicyTest {

  @Test
  public void whenActiveThreadCountIsEqualToMax_thenIncreaseByOneAndResubmitJob() {
    Runnable runnableTask = mock(CallableJob.class);
    ThreadPoolExecutor executor = mock(ThreadPoolExecutor.class);
    when(executor.getActiveCount()).thenReturn(100);
    when(executor.getMaximumPoolSize()).thenReturn(100);
    when(executor.isShutdown()).thenReturn(false);

    Future runnableFuture = mock(Future.class);

    when(executor.submit(runnableTask)).thenReturn(runnableFuture);

    final GrowAndSubmitPolicy growAndSubmitPolicy = new GrowAndSubmitPolicy();
    growAndSubmitPolicy.rejectedExecution(runnableTask, executor);
    verify(executor).setMaximumPoolSize(101);
    verify(executor).isShutdown();
    verify(executor).submit(runnableTask);

  }

  @Test
  public void whenActiveThreadCountIsEqualToMaxExecutorShutdown_thenIncreaseByOneAndResubmitJob() {
    Runnable runnableTask = mock(CallableJob.class);
    ThreadPoolExecutor executor = mock(ThreadPoolExecutor.class);
    when(executor.getActiveCount()).thenReturn(100);
    when(executor.getMaximumPoolSize()).thenReturn(100);
    when(executor.isShutdown()).thenReturn(true);

    final GrowAndSubmitPolicy growAndSubmitPolicy = new GrowAndSubmitPolicy();
    growAndSubmitPolicy.rejectedExecution(runnableTask, executor);
    verify(executor,times(0)).setMaximumPoolSize(101);
    verify(executor).isShutdown();
    verify(executor,times(0)).submit(runnableTask);

  }

}