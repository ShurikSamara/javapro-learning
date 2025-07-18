package ru.learning.java.spring.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.learning.java.spring.dto.ProductDto;
import ru.learning.java.spring.mapper.ProductMapper;
import ru.learning.java.spring.model.Product;
import ru.learning.java.spring.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductService productService;
  private final ProductMapper productMapper;

  public ProductController(ProductService productService, ProductMapper productMapper) {
    this.productService = productService;
    this.productMapper = productMapper;
  }

  @GetMapping("/{id}")
  public ProductDto getProductById(@PathVariable Long id) {
    Product product = productService.getProductById(id);
    return productMapper.toDto(product);
  }

  @GetMapping("/client/{clientId}")
  public List<ProductDto> getProductsByClientId(@PathVariable Long clientId) {
    List<Product> products = productService.getProductsByClientId(clientId);
    return productMapper.toDtoList(products);
  }

  @GetMapping
  public List<ProductDto> getAllProducts() {
    List<Product> products = productService.getAllProducts();
    return productMapper.toDtoList(products);
  }

  @PostMapping
  public ProductDto createProduct(@RequestBody ProductDto productDto) {
    Product product = productMapper.toEntity(productDto);
    Product savedProduct = productService.createProduct(product);
    return productMapper.toDto(savedProduct);
  }

  @PutMapping("/{id}")
  public ProductDto updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
    Product product = productMapper.toEntity(productDto);
    Product updatedProduct = productService.updateProduct(id, product);
    return productMapper.toDto(updatedProduct);
  }

  @DeleteMapping("/{id}")
  public void deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
  }

}