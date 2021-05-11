package io.jobscheduler.scheduler.service;

import io.jobscheduler.models.Task;
import io.jobscheduler.scheduler.tasks.CallableTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService<Task> {

  ExecutorService executorService;
  Semaphore semaphore;

  public TaskServiceImpl(@Autowired ExecutorService executorService,
      @Value("${service.thread.semaphore.count}") int semaphoreCount,
      @Value("${service.thread.semaphore.fairness:true}") boolean fairness) {
    this.executorService = executorService;
    this.semaphore = new Semaphore(semaphoreCount, fairness);
  }

  @Override
  public void processTask(Task task) {
    this.executorService.execute(new CallableTask(task, semaphore));
  }


}
