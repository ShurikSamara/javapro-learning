package ru.learning.java.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.learning.java.spring.model.PaymentLimit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Репозиторий управления лимитами платежей
 */
@Repository
public interface PaymentLimitRepository extends JpaRepository<PaymentLimit, Long> {

    /**
     * Ищем лимит по ID пользователя
     *
     * @param userId the user ID
     * @return the payment limit, if found
     */
    Optional<PaymentLimit> findByUserId(Long userId);

    /**
     * Проверим существование лимита для пользователя
     *
     * @param userId the user ID
     * @return true if limit found
     */
    boolean existsByUserId(Long userId);


    /**
     * Уменьшаем лимит атомарно с проверкой достаточности средств
     *
     * @param userId       ID пользователя
     * @param amount       сумма для уменьшения
     * @param lastUpdated  timestamp обновления
     * @return количество обновлённых строк (1 - успех, 0 - недостаточно средств или пользователь не найден)
     */
    @Modifying
    @Transactional
    @Query("UPDATE PaymentLimit pl SET pl.currentLimit = pl.currentLimit - :amount, pl.lastUpdated = :lastUpdated " +
      "WHERE pl.userId = :userId AND pl.currentLimit >= :amount")
    int decreaseLimit(@Param("userId") Long userId,
                      @Param("amount") BigDecimal amount,
                      @Param("lastUpdated") LocalDateTime lastUpdated);

    /**
     * Восстановление лимита атомарно с ограничением по максимальному значению
     *
     * @param userId       ID пользователя
     * @param amount       сумма для восстановления
     * @param lastUpdated  timestamp обновления
     * @return количество обновлённых строк
     */
    @Modifying
    @Transactional
    @Query("UPDATE PaymentLimit pl SET pl.currentLimit = " +
      "CASE WHEN pl.currentLimit + :amount > pl.defaultLimit " +
      "THEN pl.defaultLimit " +
      "ELSE pl.currentLimit + :amount END, " +
      "pl.lastUpdated = :lastUpdated " +
      "WHERE pl.userId = :userId AND :amount > 0")
    int restoreLimit(@Param("userId") Long userId,
                     @Param("amount") BigDecimal amount,
                     @Param("lastUpdated") LocalDateTime lastUpdated);

    /**
     * Сбросить все лимиты до значений по умолчанию
     *
     * @param lastUpdated the last updated timestamp
     * @return the number of rows affected
     */
    @Modifying
    @Transactional
    @Query("UPDATE PaymentLimit pl SET pl.currentLimit = pl.defaultLimit, pl.lastUpdated = :lastUpdated")
    int resetAllLimits(@Param("lastUpdated") LocalDateTime lastUpdated);
}