package ru.learning.java.spring.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.learning.java.spring.dto.LimitRequest;
import ru.learning.java.spring.dto.LimitResponse;
import ru.learning.java.spring.service.PaymentLimitService;

/**
 * Контроллер для управления платёжными лимитами
 */
@Validated
@RestController
@RequestMapping("/limits")
public class LimitController {

  private static final Logger log = LoggerFactory.getLogger(LimitController.class);

  private final PaymentLimitService paymentLimitService;

  public LimitController(PaymentLimitService paymentLimitService) {
    this.paymentLimitService = paymentLimitService;
  }

  /**
   * Получить текущий платёжный лимит пользователя
   * Если пользователь не существует, будет создана новая запись с лимитом по умолчанию
   *
   * @param userId идентификатор пользователя
   * @return платёжный лимит
   */
  @GetMapping("/{userId}")
  public LimitResponse getUserLimit(
    @PathVariable("userId") @Min(value = 1, message = "Идентификатор пользователя должен быть положительным") Long userId) {
    log.debug("Получение платежного лимита для пользователя с ID: {}", userId);
    return paymentLimitService.getUserLimit(userId);
  }

  /**
   * Уменьшить лимит пользователя для успешного платежа
   * Если у пользователя недостаточно лимита, будет выброшено исключение
   *
   * @param request запрос на изменение лимита, содержащий ID пользователя и сумму
   * @return обновленный платежный лимит
   */
  @PostMapping("/decrease")
  public LimitResponse decreaseLimit(@RequestBody @Valid LimitRequest request) {
    log.debug("Уменьшение лимита для пользователя с ID: {} на сумму: {}",
      request.userId(), request.amount());
    return paymentLimitService.decreaseLimit(request);
  }

  /**
   * Восстановить лимит пользователя для неудавшегося платежа
   *
   * @param request запрос на изменение лимита, содержащий ID пользователя и сумму
   * @return обновлённый платёжный лимит
   */
  @PostMapping("/restore")
  public LimitResponse restoreLimit(@RequestBody @Valid LimitRequest request) {
    log.debug("Восстановление лимита для пользователя с ID: {} на сумму: {}",
      request.userId(), request.amount());
    return paymentLimitService.restoreLimit(request);
  }
}