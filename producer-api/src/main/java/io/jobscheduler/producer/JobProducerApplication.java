package io.jobscheduler.producer;

import java.time.Clock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.context.WebServerPortFileWriter;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class JobProducerApplication {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(JobProducerApplication.class);
    application.addListeners(new ApplicationPidFileWriter("/tmp/job-scheduler-api/app.pid"));
    application.addListeners(new WebServerPortFileWriter("/tmp/job-scheduler-api/server.pid"));
    application.run(args);
  }

  @Bean
  public Clock getClock() {
    return Clock.systemUTC();
  }

}
