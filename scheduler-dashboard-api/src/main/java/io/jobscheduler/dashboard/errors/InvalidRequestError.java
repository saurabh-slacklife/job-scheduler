package io.jobscheduler.dashboard.errors;

public class InvalidRequestError extends JobSchedulingError {

  public InvalidRequestError(String message, int errorCode){
    super(message, errorCode);
  }

  public InvalidRequestError(String message, int errorCode, Throwable throwable){
    super(message,errorCode, throwable);
  }

}
