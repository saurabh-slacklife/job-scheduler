package io.jobscheduler.producer.resources;

import io.jobscheduler.producer.errors.InvalidRequestException;
import io.jobscheduler.producer.errors.InvalidRequestScheduleException;
import io.jobscheduler.producer.errors.JobSchedulingException;
import io.jobscheduler.producer.models.resources.JobRequest;
import io.jobscheduler.producer.service.ITaskScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
public class JobResource {

  private ITaskScheduleService taskScheduleService;

  @Autowired
  JobResource(ITaskScheduleService taskScheduleService) {
    this.taskScheduleService = taskScheduleService;
  }

  @PostMapping("/job/{jobType}")
  public ResponseEntity createJob(@RequestBody JobRequest jobRequest,
      @PathVariable String jobType) {
    try {
      log.info("request for:{}", jobRequest.toString());
      String jobId = taskScheduleService.processTask(jobRequest, jobType);
      HttpHeaders httpHeaders = this.generateHeaders(jobId);
      return ResponseEntity.accepted().headers(httpHeaders).build();

    } catch (JobSchedulingException ex) {
      log.error("Error handling request for request={}", jobRequest);
      throw new ResponseStatusException(ex.getErrorCode(), ex.getMessage(), ex);
    }

  }

  private HttpHeaders generateHeaders(String jobId) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setCacheControl(CacheControl.noStore());
    httpHeaders.add("Location", "api/status/job/" + jobId);
    httpHeaders.add("Retry-After", "2");
    return httpHeaders;
  }

}
