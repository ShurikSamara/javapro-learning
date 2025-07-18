package ru.learning.java.spring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime timestamp,

  int status,
  String error,
  String message,
  String path,

  /*
   * Дополнительное поле для ошибок валидации
  */
  Map<String, String> fieldErrors
) {

  /*
   * Конструктор для ошибок, которые будут без fieldErrors
   */
  public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
    this(timestamp, status, error, message, path, null);
  }
}