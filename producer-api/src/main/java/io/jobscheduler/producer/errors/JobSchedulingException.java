package io.jobscheduler.producer.errors;

import lombok.Getter;

@Getter
public class JobSchedulingException extends RuntimeException {

  private int errorCode;

  public JobSchedulingException(String message, int errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public JobSchedulingException(String message, int errorCode, Throwable th) {
    super(message, th);
    this.errorCode = errorCode;
  }

}
