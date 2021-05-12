package io.jobscheduler.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.context.WebServerPortFileWriter;

@SpringBootApplication
@Slf4j
public class JobProducerApplication {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(JobProducerApplication.class);
    application.addListeners(new ApplicationPidFileWriter("/tmp/job-scheduler-api/app.pid"));
    application.addListeners(new WebServerPortFileWriter("/tmp/job-scheduler-api/server.pid"));
    application.run(args);
  }

}
