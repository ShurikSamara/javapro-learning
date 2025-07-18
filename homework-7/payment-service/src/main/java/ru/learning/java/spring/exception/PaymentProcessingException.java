package ru.learning.java.spring.exception;

import org.springframework.http.HttpStatus;

public class PaymentProcessingException extends BasePaymentException {
  public PaymentProcessingException(String message, Throwable cause) {
    super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR, "Payment Processing Error");
  }
}