package ru.learning.java.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.learning.java.spring.dto.PaymentRequest;
import ru.learning.java.spring.dto.PaymentResponse;
import ru.learning.java.spring.dto.ProductDto;
import ru.learning.java.spring.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

  private final PaymentService paymentService;

  @Autowired
  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping("/process")
  public PaymentResponse processPayment(@RequestBody PaymentRequest request) {
    return paymentService.processPayment(request);
  }

  @GetMapping("/client/{clientId}/products")
  public List<ProductDto> getClientProducts(@PathVariable Long clientId) {
    return paymentService.getClientProducts(clientId);
  }

  @GetMapping("/client/{clientId}")
  public List<PaymentResponse> getClientPayments(@PathVariable Long clientId) {
    return paymentService.getClientPayments(clientId);
  }
}