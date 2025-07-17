package ru.learning.java.spring.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
  public ResourceAlreadyExistsException(String message) {
    super(message);
  }
}