package ru.learning.java.spring.exception;

public class UserConstraintViolationException extends RuntimeException {
  public UserConstraintViolationException(String message, Throwable cause) {
    super(message, cause);
  }
}