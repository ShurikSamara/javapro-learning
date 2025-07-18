package ru.learning.java.spring.service;

import ru.learning.java.spring.dto.LimitRequest;
import ru.learning.java.spring.dto.LimitResponse;

/**
 * Интерфейс сервиса управления лимитами
 */
public interface PaymentLimitService {

  /**
   * Получить платежный лимит пользователя
   * Создать новый лимит со значением по умолчанию, если пользователь не существует
   *
   * @param userId the user ID
   * @return the user's payment limit
   */
  LimitResponse getUserLimit(Long userId);

  /**
   * Уменьшить лимит платежей пользователя
   * Нужно выбросить исключение, если лимит недостаточен
   *
   * @param request the limit change request
   * @return the updated payment limit
   */
  LimitResponse decreaseLimit(LimitRequest request);

  /**
   * Восстановление платёжного лимита пользователя
   * Эта часть для восстановление лимита за невыполненный платёж
   *
   * @param request the limit change request
   * @return the updated payment limit
   */
  LimitResponse restoreLimit(LimitRequest request);

  /**
   * Сброс всех ограничений до значений по умолчанию
   * Запланированная операция
   *
   * @return the number of limits reset
   */
  int resetAllLimits();
}