package ru.learning.java.spring.service;

import org.springframework.stereotype.Service;
import ru.learning.java.spring.exception.ProductAlreadyExistsException;
import ru.learning.java.spring.exception.ProductNotFoundException;
import ru.learning.java.spring.exception.ProductValidationException;
import ru.learning.java.spring.exception.ResourceNotFoundException;
import ru.learning.java.spring.model.Product;
import ru.learning.java.spring.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Product updateProduct(Long id, Product product) {
    if (id == null) {
      throw new IllegalArgumentException("ID продукта не может быть null");
    }

    if (product == null) {
      throw new ProductNotFoundException("Продукт не может быть null");
    }

    if (!productRepository.existsById(id)) {
      throw new ProductNotFoundException("Продукт с ID " + id + " не найден");
    }

    if (product.getId() != null && !product.getId().equals(id)) {
      throw new ProductValidationException("ID продукта в path [" + id + "] не совпадает с ID в теле [" + product.getId() + "]");
    }

    validateProduct(product);
    product.setId(id);

    return productRepository.save(product);
  }

  private void validateProduct(Product product) {
    if (product.getAccountNumber() == null || product.getAccountNumber().trim().isEmpty()) {
      throw new ProductValidationException("Номер счета обязателен");
    }

    if (product.getBalance() == null) {
      throw new ProductValidationException("Цена обязательна");
    }

    if (product.getBalance().compareTo(BigDecimal.ZERO) < 0) {
      throw new ProductValidationException("Цена не может быть отрицательной");
    }

    if (product.getProductType() == null) {
      throw new ProductValidationException("Тип продукта обязателен");
    }

    if (product.getClientId() == null) {
      throw new ProductValidationException("ID клиента обязателен");
    }
  }

  public void deleteProduct(Long id) {
    if (!productRepository.existsById(id)) {
      throw new ProductNotFoundException("Продукт с ID " + id + " не найден для удаления");
    }
    productRepository.deleteById(id);
  }

  public Product createProduct(Product product) {
    if (productRepository.existsByAccountNumber(product.getAccountNumber())) {
      throw new ProductAlreadyExistsException("Продукт с номером счета " + product.getAccountNumber() + " уже существует");
    }
    return productRepository.save(product);
  }

  public Product getProductById(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("ID продукта не может быть null");
    }
    return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Продукт с ID " + id + " не найден"));
  }

  public List<Product> getProductsByClientId(Long clientId) {
    if (clientId == null) {
      throw new IllegalArgumentException("ID пользователя не может быть null");
    }
    return productRepository.findByClientId(clientId);
  }

  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }
}