package io.jobscheduler.scheduler.components;

import io.jobscheduler.models.Priority;
import io.jobscheduler.models.Task;
import io.jobscheduler.scheduler.tasks.CallableJob;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ExecutorServiceEngineTest {

  static ThreadPoolExecutor threadPoolExecutor;
  static Semaphore semaphore;

//  @BeforeAll
  static void setup() {
    threadPoolExecutor = new ThreadPoolExecutor(0, 6,
        10L, TimeUnit.SECONDS,
        new PriorityBlockingQueue(),
        new GrowAndSubmitPolicy());
    semaphore = new Semaphore(10, false);
  }

//  @Test
  void testSchedule() {

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    final Task taskOne = new Task();

    final ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
    System.out.println(utcNow);
    System.out.println(utcNow.plusSeconds(5));
    taskOne.setPriority(Priority.LOW);
    taskOne.setTaskId("One");
    taskOne.setJobType("push-notification");

//    final CallableJob callableJobOne = new CallableJob(taskOne, semaphore);

    final Instant instantPlusTen = utcNow.plusSeconds(10).toInstant();

    taskOne.setJobScheduleTimeUtc(utcNow.minusSeconds(10).toInstant());
//
//    scheduler
//        .schedule(callableJobOne, Duration.between(utcNow.toInstant(), instantPlusTen).getSeconds(),
//            TimeUnit.SECONDS);

    try {
      scheduler.awaitTermination(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }


  }

  @Test
  void testDuration() {
    final ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
    final ZonedDateTime zonedDateTime = utcNow.minusSeconds(10);

    assertTrue(Duration.between(utcNow, zonedDateTime).toSeconds() < 0);


  }

}