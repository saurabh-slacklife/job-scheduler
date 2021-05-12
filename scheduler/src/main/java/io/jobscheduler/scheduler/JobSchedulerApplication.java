package io.jobscheduler.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.context.WebServerPortFileWriter;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableKafkaStreams
public class JobSchedulerApplication {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(JobSchedulerApplication.class);
    application.addListeners(new ApplicationPidFileWriter("/tmp/job-scheduler/app.pid"));
    application.addListeners(new WebServerPortFileWriter("/tmp/job-scheduler/server.pid"));
    application.run(args);
  }

}
