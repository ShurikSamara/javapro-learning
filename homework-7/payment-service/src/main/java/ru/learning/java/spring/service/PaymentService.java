package ru.learning.java.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.learning.java.spring.dto.PaymentRequest;
import ru.learning.java.spring.dto.PaymentResponse;
import ru.learning.java.spring.dto.ProductDto;
import ru.learning.java.spring.exception.InsufficientFundsException;
import ru.learning.java.spring.exception.PaymentProcessingException;
import ru.learning.java.spring.model.Payment;
import ru.learning.java.spring.model.PaymentStatus;
import ru.learning.java.spring.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
  private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

  private final PaymentRepository paymentRepository;
  private final ProductIntegrationService productIntegrationService;

  public PaymentService(PaymentRepository paymentRepository, ProductIntegrationService productIntegrationService) {
    this.paymentRepository = paymentRepository;
    this.productIntegrationService = productIntegrationService;
  }

  /**
   * Получаем список продуктов клиента
   *
   * @param clientId the client ID
   * @return list of products
   */
  public List<ProductDto> getClientProducts(Long clientId) {
    log.debug("Fetching products for client ID: {}", clientId);
    return productIntegrationService.getProductsByClientId(clientId);
  }

  /**
   * Платёж
   *
   * @param request the payment request
   * @return payment response with status and details
   * @throws PaymentProcessingException if there's an error during payment processing
   */
  @Transactional(rollbackFor = {PaymentProcessingException.class, RuntimeException.class})
  public PaymentResponse processPayment(PaymentRequest request) {
    log.debug("Processing payment for client ID: {}, product ID: {}", request.clientId(), request.productId());

    ProductDto product = productIntegrationService.getProductById(request.productId());
    log.debug("Retrieved product: {}", product);
    
    if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
      log.warn("Invalid payment amount: {}", request.amount());
      throw new PaymentProcessingException("Payment amount must be greater than zero", null);
    }
    
    if (product.balance().compareTo(request.amount()) < 0) {
      log.warn("Insufficient funds: balance={}, amount={}", product.balance(), request.amount());
      throw new InsufficientFundsException("Insufficient funds in the account. Balance: " + product.balance() + ", Required: " + request.amount());
    }

    Payment payment = new Payment();
    payment.setClientId(request.clientId());
    payment.setProductId(request.productId());
    payment.setAmount(request.amount());
    payment.setStatus(PaymentStatus.PENDING);
    payment.setCreatedAt(LocalDateTime.now());

    payment = paymentRepository.save(payment);
    log.debug("Created payment with ID: {}", payment.getId());

    try {
      log.debug("Processing payment for client ID: {}, amount: {}", request.clientId(), request.amount());
      log.debug("Would update product balance from {} to {}", 
                product.balance(), product.balance().subtract(request.amount()));

      payment.setStatus(PaymentStatus.COMPLETED);
      payment = paymentRepository.save(payment);
      log.info("Payment completed successfully for client ID: {}, payment ID: {}", request.clientId(), payment.getId());

      return new PaymentResponse(payment.getId(), "COMPLETED", payment.getAmount(), "Payment processed successfully");
    } catch (Exception e) {
      log.error("Payment processing failed: {}", e.getMessage(), e);
      payment.setStatus(PaymentStatus.FAILED);
      paymentRepository.save(payment);
      throw new PaymentProcessingException("Payment processing failed: " + e.getMessage(), e);
    }
  }

  /**
   * Получить все платежи клиента
   *
   * @param clientId the client ID
   * @return list of payments
   */
  public List<PaymentResponse> getClientPayments(Long clientId) {
    log.debug("Fetching payments for client ID: {}", clientId);
    List<Payment> payments = paymentRepository.findByClientId(clientId);

    return payments.stream()
      .map(this::convertToResponse)
      .toList();
  }

  /**
   * Преобразовать Payment entity в PaymentResponse для списка, чтобы Entity не вылетал в контроллер
    */
  private PaymentResponse convertToResponse(Payment payment) {
    return new PaymentResponse(
      payment.getId(),
      payment.getStatus().name(),
      payment.getAmount(),
      null, // исключить message, но надо удалить
      payment.getClientId(),
      payment.getProductId(),
      payment.getCreatedAt()
    );
  }
}