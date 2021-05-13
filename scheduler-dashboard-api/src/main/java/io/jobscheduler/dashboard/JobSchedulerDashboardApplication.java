package io.jobscheduler.dashboard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.context.WebServerPortFileWriter;

@SpringBootApplication
@Slf4j
public class JobSchedulerDashboardApplication {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(JobSchedulerDashboardApplication.class);
    application.addListeners(new ApplicationPidFileWriter("/tmp/job-dashboard-api/app.pid"));
    application.addListeners(new WebServerPortFileWriter("/tmp/job-dashboard-api/server.pid"));
    application.run(args);
  }

}
