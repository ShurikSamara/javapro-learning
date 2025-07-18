package ru.learning.java.spring.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Ответ
 */
public record LimitResponse(
  Long userId,
  BigDecimal currentLimit,
  BigDecimal defaultLimit,
  LocalDateTime lastUpdated
) {}