package ru.learning.java.spring.mapper;

import org.springframework.stereotype.Component;
import ru.learning.java.spring.dto.ProductDto;
import ru.learning.java.spring.model.Product;
import ru.learning.java.spring.model.ProductType;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

  public ProductDto toDto(Product product) {
    if (product == null) {
      return null;
    }
    return new ProductDto(
      product.getId(),
      product.getAccountNumber(),
      product.getBalance(),
      product.getProductType().name(),
      product.getClientId()
    );
  }

  public Product toEntity(ProductDto productDto) {
    if (productDto == null) {
      return null;
    }
    return new Product(
      productDto.id(),
      productDto.accountNumber(),
      productDto.balance(),
      ProductType.valueOf(productDto.productType()),
      productDto.clientId()
    );
  }

  public List<ProductDto> toDtoList(List<Product> products) {
    if (products == null) {
      return null;
    }
    return products.stream()
      .map(this::toDto)
      .collect(Collectors.toList());
  }
}