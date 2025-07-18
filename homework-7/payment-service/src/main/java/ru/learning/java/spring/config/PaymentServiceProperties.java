package ru.learning.java.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Конфиг для платёжного сервиса
 */
@ConfigurationProperties(prefix = "payment-service")
public class PaymentServiceProperties {

  private int maxFailedPayments;

  private int connectionTimeout;

  private int readTimeout;

  private String productServiceUrl;

  public int getMaxFailedPayments() {
    return maxFailedPayments;
  }

  public void setMaxFailedPayments(int maxFailedPayments) {
    this.maxFailedPayments = maxFailedPayments;
  }

  public int getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public int getReadTimeout() {
    return readTimeout;
  }

  public void setReadTimeout(int readTimeout) {
    this.readTimeout = readTimeout;
  }

  public String getProductServiceUrl() {
    return productServiceUrl;
  }

  public void setProductServiceUrl(String productServiceUrl) {
    this.productServiceUrl = productServiceUrl;
  }
}