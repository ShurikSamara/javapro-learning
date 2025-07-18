package ru.learning.java.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.learning.java.spring.exception.ResourceNotFoundException;
import ru.learning.java.spring.model.Product;
import ru.learning.java.spring.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
    Optional<Product> product = productService.getProductById(id);
    return product.map(ResponseEntity::ok)
      .orElseThrow(() -> new ResourceNotFoundException("Продукт с ID " + id + " не найден"));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Product>> getProductsByUserId(@PathVariable("userId") Long userId) {
    List<Product> products = productService.getProductsByUserId(userId);
    return ResponseEntity.ok(products);
  }

  @GetMapping
  public ResponseEntity<List<Product>> getAllProducts() {
    List<Product> products = productService.getAllProducts();
    return ResponseEntity.ok(products);
  }

  @PostMapping
  public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    Product createdProduct = productService.createProduct(product);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
    if (product.getId() != null && !product.getId().equals(id)) {
      throw new IllegalArgumentException("ID в пути и в теле запроса не совпадают");
    }
    product.setId(id);
    Product updatedProduct = productService.updateProduct(id, product);
    return ResponseEntity.ok(updatedProduct);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
    boolean deleted = productService.deleteProduct(id);
    if (deleted) {
      return ResponseEntity.noContent().build();
    } else {
      throw new ResourceNotFoundException("Продукт с ID " + id + " не найден");
    }
  }
}