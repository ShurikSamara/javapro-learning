package ru.learning.java.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

/**
 * Класс конфигурации
 */
@ConfigurationProperties(prefix = "limits-service")
public class LimitsServiceProperties {

  private BigDecimal defaultLimit;

  private String resetCron;

  public BigDecimal getDefaultLimit() {
    return defaultLimit;
  }

  public void setDefaultLimit(BigDecimal defaultLimit) {
    this.defaultLimit = defaultLimit;
  }

  public String getResetCron() {
    return resetCron;
  }

  public void setResetCron(String resetCron) {
    this.resetCron = resetCron;
  }
}