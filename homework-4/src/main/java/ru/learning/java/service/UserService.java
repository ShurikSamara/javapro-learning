package ru.learning.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.learning.java.dao.UserDao;
import ru.learning.java.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

  private final UserDao userDao;

  @Autowired
  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  public User createUser(String username) {
    if (username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException("Имя пользователя не может быть пустым");
    }
    User user = new User(username.trim());
    return userDao.save(user);
  }

  public Optional<User> getUserById(Long id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("ID должен быть положительным числом");
    }
    return userDao.findById(id);
  }

  public List<User> getAllUsers() {
    return userDao.findAll();
  }

  public User updateUser(Long id, String username) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("ID должен быть положительным числом");
    }
    if (username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException("Имя пользователя не может быть пустым");
    }

    User user = new User(id, username.trim());
    return userDao.update(user);
  }

  public boolean deleteUser(Long id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("ID должен быть положительным числом");
    }
    return userDao.deleteById(id);
  }
}