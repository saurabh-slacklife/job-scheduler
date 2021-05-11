package io.jobscheduler.scheduler;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.context.WebServerPortFileWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafkaStreams
public class JobSchedulerApplication {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(JobSchedulerApplication.class);
    application.addListeners(new ApplicationPidFileWriter("/tmp/job-scheduler/app.pid"));
    application.addListeners(new WebServerPortFileWriter("/tmp/job-scheduler/server.pid"));
    application.run(args);
  }

  @Bean
  public Clock getClock() {
    return Clock.systemUTC();
  }
}
