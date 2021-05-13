package io.jobscheduler.dashboard.service;

import io.jobscheduler.dashboard.errors.InvalidRequestError;
import io.jobscheduler.dashboard.errors.ResourceNotFoundError;
import io.jobscheduler.dashboard.models.Action;
import io.jobscheduler.dashboard.models.document.TaskDocument;
import io.jobscheduler.dashboard.models.resources.JobResponse;
import io.jobscheduler.dashboard.repository.MongoTaskRepositoryImpl;
import io.jobscheduler.models.TaskStatus;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ActiveProfiles({"default", "dev", "qa", "prod"})
class JobDashboardServiceTest {

  @Mock
  MongoTaskRepositoryImpl mongoTaskRepository;

  @Test
  public void whenValidJobIdInput_thenReturnValidDocument() {
    final JobDashboardService jobDashboardService = new JobDashboardService(mongoTaskRepository);
    String jobIdUri = Action.email.name() + ":" + "12345thdfr";
    final String encodedUri = Base64.getEncoder()
        .encodeToString(jobIdUri.getBytes(StandardCharsets.UTF_8));

    TaskDocument taskDocument = new TaskDocument();
    taskDocument.setTaskStatus(TaskStatus.SUCCESS);
    taskDocument.setJobType(Action.email.name());
    taskDocument.setId(encodedUri);
    taskDocument.setRequestId("12345");
    taskDocument.setTaskRequest(new HashMap<>());

    doReturn(taskDocument).when(mongoTaskRepository).findById(any());

    JobResponse jobResponse = jobDashboardService.searchByJobId(encodedUri);

    verify(mongoTaskRepository, times(1)).findById(any());
    assertNotNull(jobResponse);
    assertEquals(jobResponse.getRequestId(), taskDocument.getRequestId());


  }

  @Test
  public void whenInValidJobIdInput_thenThrowInvalidRequestError() {
    final JobDashboardService jobDashboardService = new JobDashboardService(mongoTaskRepository);

    doReturn(null).when(mongoTaskRepository).findById(any());
    assertThrows(InvalidRequestError.class, () -> jobDashboardService.searchByJobId(null));

    verify(mongoTaskRepository, times(0)).findById(any());


  }

  @Test
  public void whenJobIdNotFound_thenThrowResourceNotFoundError() {
    final JobDashboardService jobDashboardService = new JobDashboardService(mongoTaskRepository);

    String jobIdUri = Action.email.name() + ":" + "12345thdfr";
    final String encodedUri = Base64.getEncoder()
        .encodeToString(jobIdUri.getBytes(StandardCharsets.UTF_8));

    assertThrows(ResourceNotFoundError.class, () -> jobDashboardService.searchByJobId(encodedUri));

    verify(mongoTaskRepository, times(1)).findById(any());


  }

}