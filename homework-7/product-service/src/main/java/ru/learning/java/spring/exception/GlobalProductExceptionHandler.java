package ru.learning.java.spring.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.learning.java.spring.dto.ErrorResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalProductExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalProductExceptionHandler.class);

  @ExceptionHandler(BaseProductException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleApiException(BaseProductException e, WebRequest request) {
    logger.warn("API Exception: {} - {}", e.getErrorType(), e.getMessage());
    return createErrorResponse(e.getHttpStatus(), e.getErrorType(), e.getMessage(), request);
  }

  @ExceptionHandler({ProductNotFoundException.class, ResourceNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleNotFoundException(BaseProductException e, WebRequest request) {
    logger.warn("Resource not found: {}", e.getMessage());
    return createErrorResponse(e.getHttpStatus(), e.getErrorType(), e.getMessage(), request);
  }

  @ExceptionHandler({ProductValidationException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleValidationException(BaseProductException e, WebRequest request) {
    logger.warn("Validation error: {}", e.getMessage());
    return createErrorResponse(e.getHttpStatus(), e.getErrorType(), e.getMessage(), request);
  }

  @ExceptionHandler({ProductAlreadyExistsException.class})
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse handleConflictException(BaseProductException e, WebRequest request) {
    logger.warn("Conflict: {}", e.getMessage());
    return createErrorResponse(e.getHttpStatus(), e.getErrorType(), e.getMessage(), request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleValidationExceptions(
    MethodArgumentNotValidException e, WebRequest request) {
    logger.warn("Ошибка валидации: {}", e.getMessage());

    Map<String, String> fieldErrors = new HashMap<>();
    e.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      fieldErrors.put(fieldName, errorMessage);
    });

    return createErrorResponseWithFieldErrors(
      HttpStatus.BAD_REQUEST, "Validation Failed", "Ошибка валидации полей", request, fieldErrors);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleIllegalArgumentException(
    IllegalArgumentException e, WebRequest request) {
    logger.warn("Неверный запрос: {}", e.getMessage());
    return createErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", e.getMessage(), request);
  }

  @ExceptionHandler(IllegalStateException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse handleIllegalStateException(
    IllegalStateException e, WebRequest request) {
    logger.warn("Конфликт состояния: {}", e.getMessage());
    return createErrorResponse(HttpStatus.CONFLICT, "Conflict", e.getMessage(), request);
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleRuntimeException(
    RuntimeException e, WebRequest request) {
    logger.error("Внутренняя ошибка сервера: {}", e.getMessage(), e);
    return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
      "Произошла внутренняя ошибка сервера", request);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleGeneralException(
    Exception e, WebRequest request) {
    logger.error("Неожиданная ошибка: {}", e.getMessage(), e);
    return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
      "Произошла неожиданная ошибка", request);
  }

  private ErrorResponse createErrorResponse(HttpStatus status, String error,
                                            String message, WebRequest request) {
    String path = request.getDescription(false).replace("uri=", "");
    return new ErrorResponse(LocalDateTime.now(), status.value(), error, message, path);
  }

  private ErrorResponse createErrorResponseWithFieldErrors(HttpStatus status, String error,
                                                           String message, WebRequest request,
                                                           Map<String, String> fieldErrors) {
    String path = request.getDescription(false).replace("uri=", "");
    return new ErrorResponse(LocalDateTime.now(), status.value(), error, message, path, fieldErrors);
  }
}