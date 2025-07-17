package ru.learning.java.spring.model;

public enum ProductType {
  ACCOUNT("счет"),
  CARD("карта");

  private final String displayName;

  ProductType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}