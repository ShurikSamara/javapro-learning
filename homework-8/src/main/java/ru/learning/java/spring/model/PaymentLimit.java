package ru.learning.java.spring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Лимит платежа пользователя (что кладём в БД)
 */
@Entity
@Table(name = "payment_limits")
public class PaymentLimit {

  @Id
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "current_limit", nullable = false)
  private BigDecimal currentLimit;

  @Column(name = "default_limit", nullable = false)
  private BigDecimal defaultLimit;

  @Column(name = "last_updated", nullable = false)
  private LocalDateTime lastUpdated;

  public PaymentLimit() {
  }

  public PaymentLimit(Long userId, BigDecimal currentLimit, BigDecimal defaultLimit, LocalDateTime lastUpdated) {
    this.userId = userId;
    this.currentLimit = currentLimit;
    this.defaultLimit = defaultLimit;
    this.lastUpdated = lastUpdated;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public BigDecimal getCurrentLimit() {
    return currentLimit;
  }

  public void setCurrentLimit(BigDecimal currentLimit) {
    this.currentLimit = currentLimit;
  }

  public BigDecimal getDefaultLimit() {
    return defaultLimit;
  }

  public void setDefaultLimit(BigDecimal defaultLimit) {
    this.defaultLimit = defaultLimit;
  }

  public LocalDateTime getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(LocalDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PaymentLimit that = (PaymentLimit) o;
    return Objects.equals(userId, that.userId) &&
      Objects.equals(currentLimit, that.currentLimit) &&
      Objects.equals(defaultLimit, that.defaultLimit) &&
      Objects.equals(lastUpdated, that.lastUpdated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, currentLimit, defaultLimit, lastUpdated);
  }

  @Override
  public String toString() {
    return "PaymentLimit{" +
      "userId=" + userId +
      ", currentLimit=" + currentLimit +
      ", defaultLimit=" + defaultLimit +
      ", lastUpdated=" + lastUpdated +
      '}';
  }
}