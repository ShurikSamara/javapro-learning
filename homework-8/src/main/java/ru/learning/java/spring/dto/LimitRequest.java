package ru.learning.java.spring.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Запрос
 */
public record LimitRequest(

  @NotNull(message = "User ID is required")
  Long userId,

  @NotNull(message = "Amount is required")
  @DecimalMin(value = "0.01", message = "Amount must be positive")
  BigDecimal amount
) {}