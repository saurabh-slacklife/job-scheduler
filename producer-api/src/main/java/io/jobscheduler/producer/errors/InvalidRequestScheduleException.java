package io.jobscheduler.producer.errors;

public class InvalidRequestScheduleException extends JobSchedulingException {

  public InvalidRequestScheduleException(String message, int errorCode) {
    super(message, errorCode);
  }

  public InvalidRequestScheduleException(String message, int errorCode, Throwable throwable) {
    super(message, errorCode, throwable);
  }

}
