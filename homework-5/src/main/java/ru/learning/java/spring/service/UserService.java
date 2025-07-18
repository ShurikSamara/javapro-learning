package ru.learning.java.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.learning.java.spring.model.User;
import ru.learning.java.spring.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createUser(String username) {
    if (username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException("Имя пользователя не может быть пустым");
    }
    username = username.trim();

    try {
      User user = new User(username);
      return userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new RuntimeException("Пользователь с таким именем уже существует: " + username);
    }
  }

  @Transactional(readOnly = true)
  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Transactional
  public User updateUser(Long id, String newUsername) {
    if (id == null) {
      throw new IllegalArgumentException("ID пользователя не может быть null");
    }
    if (newUsername == null || newUsername.trim().isEmpty()) {
      throw new IllegalArgumentException("Имя пользователя не может быть пустым");
    }

    Optional<User> existingUser = userRepository.findByUsername(newUsername.trim());
    if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
      throw new RuntimeException("Пользователь с именем '" + newUsername + "' уже существует");
    }

    User user = userRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Пользователь не найден с ID: " + id));

    user.setUsername(newUsername.trim());
    return userRepository.save(user);
  }

  public boolean deleteUser(Long id) {
    if (id == null) {
      return false;
    }

    try {
      if (userRepository.existsById(id)) {
        userRepository.deleteById(id);
        return true;
      }
      return false;
    } catch (Exception e) {
      System.err.println("Ошибка при удалении пользователя с ID " + id + ": " + e.getMessage());
      return false;
    }
  }


  @Transactional(readOnly = true)
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Transactional(readOnly = true)
  public List<User> findUsersContaining(String pattern) {
    return userRepository.findByUsernameContaining(pattern);
  }

  @Transactional(readOnly = true)
  public long getUserCount() {
    return userRepository.count();
  }
}