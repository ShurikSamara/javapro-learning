package ru.learning.java.spring.dto;

import java.time.LocalDateTime;

/**
 * Ответ-ошибка
 */
public record ErrorResponse(
  LocalDateTime timestamp,
  int status,
  String message,
  String path
) {
  /**
   * Создать ответ об ошибке
   *
   * @param status  the HTTP status code
   * @param message the error message
   * @param path    the path that caused the error
   */
  public ErrorResponse(int status, String message, String path) {
    this(LocalDateTime.now(), status, message, path);
  }
}