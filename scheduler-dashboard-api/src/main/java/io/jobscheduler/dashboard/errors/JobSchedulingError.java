package io.jobscheduler.dashboard.errors;

import lombok.Getter;

@Getter
public class JobSchedulingError extends RuntimeException {

  private int errorCode;

  public JobSchedulingError(String message, int errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public JobSchedulingError(String message, int errorCode, Throwable th) {
    super(message, th);
    this.errorCode = errorCode;
  }

}
