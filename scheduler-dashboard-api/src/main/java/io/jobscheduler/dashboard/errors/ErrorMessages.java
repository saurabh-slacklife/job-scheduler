package io.jobscheduler.dashboard.errors;

import lombok.Getter;

@Getter
public enum ErrorMessages {

  INVALID_REQUEST(400, "Invalid request input"),
  RESOURCE_NOT_FOUND(404, "Resource not found");


  private int errorCode;
  private String errorMessage;


  ErrorMessages(int errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }
}
