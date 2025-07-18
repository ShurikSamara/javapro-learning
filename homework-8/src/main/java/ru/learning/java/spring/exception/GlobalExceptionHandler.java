package ru.learning.java.spring.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.learning.java.spring.dto.ErrorResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Обработчик исключений
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * NoResourceFoundException
   *
   * @param ex      the exception
   * @param request the web request
   * @return the error response
   */
  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleResourceNotFound(NoResourceFoundException ex, WebRequest request) {
    log.debug("Resource not found: {}", ex.getMessage());
    String path = request.getDescription(false).replace("uri=", "");
    return new ErrorResponse(
      HttpStatus.NOT_FOUND.value(),
      "Resource not found: " + ex.getMessage(),
      path
    );
  }

  /**
   * IllegalArgumentException
   *
   * @param ex      the exception
   * @param request the web request
   * @return the error response
   */
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
    log.error("Invalid argument: {}", ex.getMessage(), ex);

    return new ErrorResponse(
      HttpStatus.BAD_REQUEST.value(),
      ex.getMessage(),
      request.getDescription(false)
    );
  }

  /**
   * IllegalStateException
   *
   * @param ex      the exception
   * @param request the web request
   * @return the error response
   */
  @ExceptionHandler(IllegalStateException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse handleIllegalStateException(IllegalStateException ex, WebRequest request) {
    log.error("Invalid state: {}", ex.getMessage(), ex);

    return new ErrorResponse(
      HttpStatus.CONFLICT.value(),
      ex.getMessage(),
      request.getDescription(false)
    );
  }

  /**
   * MethodArgumentNotValidException
   *
   * @param ex      the exception
   * @param request the web request
   * @return the error response
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleMethodArgumentNotValidException(
    MethodArgumentNotValidException ex, WebRequest request) {
    log.error("Validation error: {}", ex.getMessage(), ex);

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      if (error instanceof FieldError fieldError) {
        String fieldName = fieldError.getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
      } else {
        errors.put("global", error.getDefaultMessage());
      }
    });

    String errorMessage = errors.entrySet().stream()
      .map(entry -> entry.getKey() + ": " + entry.getValue())
      .collect(Collectors.joining(", "));

    return new ErrorResponse(
      HttpStatus.BAD_REQUEST.value(),
      "Validation error: " + errorMessage,
      request.getDescription(false)
    );
  }

  /**
   * Handle ConstraintViolationException
   *
   * @param ex      the exception
   * @param request the web request
   * @return the error response
   */
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleConstraintViolationException(
    ConstraintViolationException ex, WebRequest request) {
    log.error("Validation error: {}", ex.getMessage(), ex);

    String errorMessage = ex.getConstraintViolations().stream()
      .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
      .collect(Collectors.joining(", "));

    return new ErrorResponse(
      HttpStatus.BAD_REQUEST.value(),
      "Validation error: " + errorMessage,
      request.getDescription(false)
    );
  }

  /**
   * Handle all other exceptions
   *
   * @param ex      the exception
   * @param request the web request
   * @return the error response
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleAllExceptions(Exception ex, WebRequest request) {
    log.error("Unexpected error: {}", ex.getMessage(), ex);

    return new ErrorResponse(
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      "An unexpected error occurred: " + ex.getMessage(),
      request.getDescription(false)
    );
  }
}