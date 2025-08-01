package ru.learning.java.model;

public class User {
  private Long id;
  private String username;

  public User() {}

  public User(String username) {
    this.username = username;
  }

  public User(Long id, String username) {
    this.id = id;
    this.username = username;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }


  @Override
  public String toString() {
    return "User{id=" + id + ", username='" + username + "'}";
  }
}