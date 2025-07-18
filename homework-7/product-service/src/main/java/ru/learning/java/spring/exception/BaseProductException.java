package ru.learning.java.spring.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseProductException extends RuntimeException {
  private final HttpStatus httpStatus;
  private final String errorType;

  protected BaseProductException(String message, HttpStatus httpStatus, String errorType) {
    super(message);
    this.httpStatus = httpStatus;
    this.errorType = errorType;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }


  public String getErrorType() {
    return errorType;
  }
}