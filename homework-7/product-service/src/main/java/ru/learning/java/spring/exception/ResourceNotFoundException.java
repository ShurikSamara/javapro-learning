package ru.learning.java.spring.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseProductException {
  public ResourceNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND, "Ресурс не найден");
  }
}