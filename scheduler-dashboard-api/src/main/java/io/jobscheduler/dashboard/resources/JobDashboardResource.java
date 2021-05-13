package io.jobscheduler.dashboard.resources;

import io.jobscheduler.dashboard.models.resources.JobResponse;
import io.jobscheduler.dashboard.service.JobDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class JobDashboardResource {

  @Autowired
  JobDashboardService jobDashboardService;

  @GetMapping("/api/job/status/{jobId}")
  public ResponseEntity<JobResponse> getJobStatus(@PathVariable String jobId) {
    final JobResponse jobResponse = jobDashboardService.searchByJobId(jobId);
    return ResponseEntity.ok(jobResponse);
  }

}
