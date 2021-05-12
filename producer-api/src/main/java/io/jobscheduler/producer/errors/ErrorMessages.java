package io.jobscheduler.producer.errors;

import lombok.Getter;

@Getter
public enum ErrorMessages {

  INVALID_REQUEST(400, "Invalid request input"),
  SCHEDULED_UTC_ELAPSED(400, "Requested Schedul UTC time has elapsed"),
  KAFKA_ERROR(400, "Unable to process due to Kafka exception");


  private int errorCode;
  private String errorMessage;


  ErrorMessages(int errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }
}
