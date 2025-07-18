package ru.learning.java.spring.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "account_number", nullable = false)
  private String accountNumber;

  @Column(nullable = false)
  private BigDecimal balance;

  @Enumerated(EnumType.STRING)
  @Column(name = "product_type", nullable = false)
  private ProductType productType;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  public Product() {}

  public Product(String accountNumber, BigDecimal balance, ProductType productType, Long userId) {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.productType = productType;
    this.userId = userId;
  }

  public Product(Long id, String accountNumber, BigDecimal balance, ProductType productType, Long userId) {
    this.id = id;
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.productType = productType;
    this.userId = userId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public ProductType getProductType() {
    return productType;
  }

  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  @Override
  public String toString() {
    return "Product{" +
      "id=" + id +
      ", accountNumber='" + accountNumber + '\'' +
      ", balance=" + balance +
      ", productType=" + productType +
      ", userId=" + userId +
      '}';
  }
}