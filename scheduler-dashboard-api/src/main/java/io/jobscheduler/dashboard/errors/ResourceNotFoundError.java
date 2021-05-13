package io.jobscheduler.dashboard.errors;

public class ResourceNotFoundError extends JobSchedulingError {

  public ResourceNotFoundError(String message, int errorCode) {
    super(message, errorCode);
  }

  public ResourceNotFoundError(String message, int errorCode, Throwable throwable) {
    super(message, errorCode, throwable);
  }
}
