package io.jobscheduler.producer.errors;

public class InvalidRequestException extends JobSchedulingException{

  public InvalidRequestException(String message, int errorCode){
    super(message, errorCode);
  }

  public InvalidRequestException(String message, int errorCode, Throwable throwable){
    super(message,errorCode, throwable);
  }

}
