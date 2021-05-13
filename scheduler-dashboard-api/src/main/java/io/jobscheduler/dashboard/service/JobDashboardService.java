package io.jobscheduler.dashboard.service;

import io.jobscheduler.dashboard.errors.ErrorMessages;
import io.jobscheduler.dashboard.errors.InvalidRequestError;
import io.jobscheduler.dashboard.errors.ResourceNotFoundError;
import io.jobscheduler.dashboard.models.document.TaskDocument;
import io.jobscheduler.dashboard.models.resources.JobResponse;
import io.jobscheduler.dashboard.repository.MongoTaskRepositoryImpl;
import io.jobscheduler.dashboard.repository.MongoUtil;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobDashboardService {

  MongoTaskRepositoryImpl mongoTaskRepository;

  JobDashboardService(@Autowired MongoTaskRepositoryImpl mongoTaskRepository) {
    this.mongoTaskRepository = mongoTaskRepository;
  }

  public JobResponse searchByJobId(String jobId) {
    if (null != jobId) {
      final TaskDocument jobScheduledDocument = this.mongoTaskRepository
          .findById(parseBase64JobId(jobId));
      if (null != jobScheduledDocument) {
        final JobResponse jobResponse = MongoUtil
            .documentToDashboardResponseMapper(jobScheduledDocument);
        return jobResponse;
      } else {
        throw new ResourceNotFoundError(ErrorMessages.RESOURCE_NOT_FOUND.getErrorMessage(),
            ErrorMessages.RESOURCE_NOT_FOUND
                .getErrorCode());
      }
    } else {
      throw new InvalidRequestError(ErrorMessages.RESOURCE_NOT_FOUND.getErrorMessage(),
          ErrorMessages.RESOURCE_NOT_FOUND
              .getErrorCode());
    }
  }

  private String parseBase64JobId(String base64JobId) {
    final String decodedUri = new String(Base64.getDecoder().decode(base64JobId),
        StandardCharsets.UTF_8);
    final String[] split = decodedUri.split(":");
    if (split.length > 1) {
      return split[1];
    } else {
      throw new InvalidRequestError(ErrorMessages.RESOURCE_NOT_FOUND.getErrorMessage(),
          ErrorMessages.RESOURCE_NOT_FOUND
              .getErrorCode());
    }


  }
}
