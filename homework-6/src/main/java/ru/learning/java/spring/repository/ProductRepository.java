package ru.learning.java.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.learning.java.spring.model.Product;
import ru.learning.java.spring.model.ProductType;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findByUserId(Long userId);

  List<Product> findByProductType(ProductType productType);

  @Query("SELECT p FROM Product p WHERE p.userId = :userId AND p.productType = :productType")
  List<Product> findByUserIdAndProductType(@Param("userId") Long userId, @Param("productType") ProductType productType);

  @Query("SELECT p FROM Product p WHERE p.balance >= :minBalance")
  List<Product> findByBalanceGreaterThanEqual(@Param("minBalance") BigDecimal minBalance);
}