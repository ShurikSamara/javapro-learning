package ru.learning.java.spring.exception;

import org.springframework.http.HttpStatus;

public class ProductAlreadyExistsException extends BaseProductException {
  public ProductAlreadyExistsException(String message) {
    super(message, HttpStatus.CONFLICT, "Продукт уже существует");
  }
}