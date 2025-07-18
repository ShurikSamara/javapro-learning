package ru.learning.java.spring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse(
  Long paymentId,
  String status,
  BigDecimal amount,
  String message,

  Long clientId,
  Long productId,

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime createdAt
) {
  // Конструктор для ответа на обработку платежа
  public PaymentResponse(Long paymentId, String status, BigDecimal amount, String message) {
    this(paymentId, status, amount, message, null, null, null);
  }
}