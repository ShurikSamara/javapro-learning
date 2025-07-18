package ru.learning.java.spring.dto;

import java.math.BigDecimal;

public record PaymentRequest(
  Long clientId,
  Long productId,
  BigDecimal amount,
  String description
) {}