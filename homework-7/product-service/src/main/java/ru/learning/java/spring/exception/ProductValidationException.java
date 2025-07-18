package ru.learning.java.spring.exception;

import org.springframework.http.HttpStatus;

public class ProductValidationException extends BaseProductException {
  public ProductValidationException(String message) {
    super(message, HttpStatus.BAD_REQUEST, "Ошибка валидации продукта");
  }
}