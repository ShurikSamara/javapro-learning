package ru.learning.java.spring.exception;

import org.springframework.http.HttpStatus;

public class InsufficientFundsException extends BasePaymentException {
  public InsufficientFundsException(String message) {
    super(message, HttpStatus.BAD_REQUEST, "Недостаточно средств");
  }
}