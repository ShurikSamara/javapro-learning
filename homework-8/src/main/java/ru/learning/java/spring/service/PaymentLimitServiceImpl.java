package ru.learning.java.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.learning.java.spring.config.LimitsServiceProperties;
import ru.learning.java.spring.dto.LimitRequest;
import ru.learning.java.spring.dto.LimitResponse;
import ru.learning.java.spring.mapper.LimitMapper;
import ru.learning.java.spring.model.PaymentLimit;
import ru.learning.java.spring.repository.PaymentLimitRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Реализация интерфейса PaymentLimitService
 */
@Service
public class PaymentLimitServiceImpl implements PaymentLimitService {
  private static final Logger log = LoggerFactory.getLogger(PaymentLimitServiceImpl.class);

  private final PaymentLimitRepository paymentLimitRepository;
  private final LimitsServiceProperties properties;
  private final LimitMapper limitMapper;

  public PaymentLimitServiceImpl(PaymentLimitRepository paymentLimitRepository,
                                 LimitsServiceProperties properties,
                                 LimitMapper limitMapper) {
    this.paymentLimitRepository = paymentLimitRepository;
    this.properties = properties;
    this.limitMapper = limitMapper;
  }

  @Override
  public LimitResponse getUserLimit(Long userId) {
    log.debug("Получение платежного лимита для пользователя с ID: {}", userId);

    PaymentLimit limit = paymentLimitRepository.findByUserId(userId)
      .orElseGet(() -> createNewUserLimit(userId));

    return limitMapper.toResponse(limit);
  }

  @Override
  @Transactional
  public LimitResponse decreaseLimit(LimitRequest request) {
    log.debug("Уменьшение лимита для пользователя с ID: {} на сумму: {}",
      request.userId(), request.amount());

    validateAmount(request.amount());

    // Убедимся, что пользователь существует, если нет - создадим
    if (!paymentLimitRepository.existsByUserId(request.userId())) {
      createNewUserLimit(request.userId());
    }

    LocalDateTime now = LocalDateTime.now();
    int updated = paymentLimitRepository.decreaseLimit(request.userId(), request.amount(), now);

    if (updated == 0) {
      // Проверяем, существует ли пользователь
      if (!paymentLimitRepository.existsByUserId(request.userId())) {
        throw new IllegalArgumentException("User not found: " + request.userId());
      }
      // Если пользователь существует, но обновление не произошло - недостаточно средств
      log.warn("У пользователя с ID: {} недостаточно лимита для суммы: {}",
        request.userId(), request.amount());
      throw new IllegalStateException("Not enough limit available");
    }

    // Получаем обновлённый лимит для ответа
    PaymentLimit limit = paymentLimitRepository.findByUserId(request.userId())
      .orElseThrow(() -> new IllegalStateException("User limit not found after update"));

    log.info("Уменьшен лимит для пользователя с ID: {} на сумму: {}. Новый лимит: {}",
      request.userId(), request.amount(), limit.getCurrentLimit());

    return limitMapper.toResponse(limit);
  }

  @Override
  @Transactional
  public LimitResponse restoreLimit(LimitRequest request) {
    log.debug("Восстановление лимита для пользователя с ID: {} на сумму: {}",
      request.userId(), request.amount());

    validateAmount(request.amount());

    // Убедимся, что пользователь существует, если нет - создадим
    if (!paymentLimitRepository.existsByUserId(request.userId())) {
      createNewUserLimit(request.userId());
    }

    LocalDateTime now = LocalDateTime.now();
    int updated = paymentLimitRepository.restoreLimit(request.userId(), request.amount(), now);

    if (updated == 0) {
      if (!paymentLimitRepository.existsByUserId(request.userId())) {
        throw new IllegalArgumentException("User not found: " + request.userId());
      }
      throw new IllegalStateException("Failed to restore limit for user: " + request.userId());
    }

    PaymentLimit limit = paymentLimitRepository.findByUserId(request.userId())
      .orElseThrow(() -> new IllegalStateException("User limit not found after update"));

    log.info("Восстановлен лимит для пользователя с ID: {} на сумму: {}. Новый лимит: {}",
      request.userId(), request.amount(), limit.getCurrentLimit());

    return limitMapper.toResponse(limit);
  }


  @Override
  @Transactional
  @Scheduled(cron = "${limits-service.reset-cron}")
  public int resetAllLimits() {
    log.info("Resetting all limits to their default values");

    LocalDateTime now = LocalDateTime.now();
    int count = paymentLimitRepository.resetAllLimits(now);

    log.info("Reset {} limits to their default values", count);

    return count;
  }

  /**
   * Создаём новый лимит платежей для пользователя с лимитом по умолчанию
   */
  private PaymentLimit createNewUserLimit(Long userId) {
    log.info("Creating new payment limit for user ID: {} with default limit: {}",
      userId, properties.getDefaultLimit());

    PaymentLimit limit = new PaymentLimit();
    limit.setUserId(userId);
    limit.setCurrentLimit(properties.getDefaultLimit());
    limit.setDefaultLimit(properties.getDefaultLimit());
    limit.setLastUpdated(LocalDateTime.now());

    return paymentLimitRepository.save(limit);
  }

  /**
   * Проверить, что сумма положительная
   */
  private void validateAmount(BigDecimal amount) {
    Objects.requireNonNull(amount, "Amount cannot be null");

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount must be positive");
    }
  }
}