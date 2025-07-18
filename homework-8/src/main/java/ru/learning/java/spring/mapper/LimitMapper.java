package ru.learning.java.spring.mapper;

import org.springframework.stereotype.Component;
import ru.learning.java.spring.dto.LimitResponse;
import ru.learning.java.spring.model.PaymentLimit;

/**
 * Mapper для конвертирования между сущностями PaymentLimit и DTO LimitResponse
 */
@Component
public class LimitMapper {

  /**
   * Преобразование сущности PaymentLimit в LimitResponse DTO
   *
   * @param limit the payment limit entity
   * @return the response DTO
   */
  public LimitResponse toResponse(PaymentLimit limit) {
    return new LimitResponse(
      limit.getUserId(),
      limit.getCurrentLimit(),
      limit.getDefaultLimit(),
      limit.getLastUpdated()
    );
  }
}