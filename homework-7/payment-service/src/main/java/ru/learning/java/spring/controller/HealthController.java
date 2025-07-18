package ru.learning.java.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.learning.java.spring.dto.HealthCheckResponse;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Controller для health check endpoints
 */
@RestController
@RequestMapping("/payments/health")
public class HealthController {
  private static final Logger log = LoggerFactory.getLogger(HealthController.class);
  private final LocalDateTime startTime = LocalDateTime.now();

  /**
   * Создадим health-check endpoint для удобства отслеживания живой ли сервис
   */
  @GetMapping
  public HealthCheckResponse healthCheck() {
    log.debug("Health check requested");

    return new HealthCheckResponse(
      "UP",
      LocalDateTime.now(),
      getUptime()
    );
  }

  private String getUptime() {
    LocalDateTime now = LocalDateTime.now();
    Duration duration = Duration.between(startTime, now);
    long days = duration.toDays();
    long hours = duration.toHours();
    long minutes = duration.toMinutes();
    long seconds = duration.toSeconds();

    return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
  }
}