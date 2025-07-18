package ru.learning.java.spring.exception;

import org.springframework.http.HttpStatus;

public abstract class BasePaymentException extends RuntimeException {
  private final HttpStatus httpStatus;
  private final String errorType;

  protected BasePaymentException(String message, HttpStatus httpStatus, String errorType) {
    super(message);
    this.httpStatus = httpStatus;
    this.errorType = errorType;
  }

  protected BasePaymentException(String message, Throwable cause, HttpStatus httpStatus, String errorType) {
    super(message, cause);
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