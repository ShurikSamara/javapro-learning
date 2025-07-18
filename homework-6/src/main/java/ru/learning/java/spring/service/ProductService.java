package ru.learning.java.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.learning.java.spring.exception.ResourceNotFoundException;
import ru.learning.java.spring.model.Product;
import ru.learning.java.spring.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  @Autowired
  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  private void checkProduct(Product product) {
    if (product.getAccountNumber() == null || product.getAccountNumber().trim().isEmpty()) {
      throw new IllegalArgumentException("Номер счета не может быть пустым");
    }
    if (product.getBalance() == null) {
      throw new IllegalArgumentException("Баланс не может быть null");
    }
    if (product.getProductType() == null) {
      throw new IllegalArgumentException("Тип продукта не может быть null");
    }
    if (product.getUserId() == null) {
      throw new IllegalArgumentException("ID пользователя не может быть null");
    }
  }

  public Product createProduct(Product product) {
    if (product == null) {
      throw new IllegalArgumentException("Продукт не может быть null");
    }
    checkProduct(product);

    // Убеждаемся, что ID не установлен для нового продукта
    product.setId(null);
    return productRepository.save(product);
  }

  public Optional<Product> getProductById(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("ID продукта не может быть null");
    }
    return productRepository.findById(id);
  }

  public List<Product> getProductsByUserId(Long userId) {
    if (userId == null) {
      throw new IllegalArgumentException("ID пользователя не может быть null");
    }
    return productRepository.findByUserId(userId);
  }

  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  public Product updateProduct(Long id, Product product) {
    if (id == null) {
      throw new IllegalArgumentException("ID продукта не может быть null");
    }
    if (product == null) {
      throw new IllegalArgumentException("Продукт не может быть null");
    }

    // Проверяем, существует ли продукт
    productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Продукт с ID " + id + " не найден"));

    // Валидация данных
    checkProduct(product);

    // Устанавливаем ID из параметра
    product.setId(id);

    return productRepository.save(product);
  }

  public boolean deleteProduct(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("ID продукта не может быть null");
    }

    if (productRepository.existsById(id)) {
      productRepository.deleteById(id);
      return true;
    }
    return false;
  }
}