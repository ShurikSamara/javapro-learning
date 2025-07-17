package ru.learning.java.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.learning.java.spring.exception.UserAlreadyExistsException;
import ru.learning.java.spring.exception.UserConstraintViolationException;
import ru.learning.java.spring.exception.UserNotFoundException;
import ru.learning.java.spring.model.User;
import ru.learning.java.spring.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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
      throw new UserAlreadyExistsException(username);
    }
  }

  @Transactional(readOnly = true)
  public Optional<User> getUserById(Long id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("ID должен быть положительным числом");
    }
    return userRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Transactional
  public User updateUser(Long id, String newUsername) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("ID должен быть положительным числом");
    }
    if (newUsername == null || newUsername.trim().isEmpty()) {
      throw new IllegalArgumentException("Имя пользователя не может быть пустым");
    }

    newUsername = newUsername.trim();

    // Проверяем, существует ли пользователь
    User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

    // Проверяем, не занято ли имя другим пользователем
    Optional<User> userWithSameName = userRepository.findByUsername(newUsername);
    if (userWithSameName.isPresent() && !userWithSameName.get().getId().equals(id)) {
      throw new UserAlreadyExistsException(newUsername);
    }

    existingUser.setUsername(newUsername);
    return userRepository.save(existingUser);
  }

  public boolean deleteUser(Long id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("ID должен быть положительным числом");
    }

    try {
      if (!userRepository.existsById(id)) {
        logger.debug("Попытка удалить несуществующего пользователя с ID: {}", id);
        return false;
      }

      userRepository.deleteById(id);
      logger.info("Пользователь с ID {} успешно удален", id);
      return true;

    } catch (DataIntegrityViolationException e) {
      logger.error("Не удалось удалить пользователя с ID {} из-за нарушения ограничений БД: {}", id, e.getMessage());
      throw new UserConstraintViolationException("Невозможно удалить пользователя: на него ссылаются другие данные", e);

    } catch (DataAccessException e) {
      logger.error("Ошибка доступа к базе данных при удалении пользователя с ID {}: {}", id, e.getMessage());
      throw new RuntimeException("Ошибка базы данных при удалении пользователя", e);
    }
  }

  @Transactional(readOnly = true)
  public Optional<User> findByUsername(String username) {
    if (username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException("Имя пользователя не может быть пустым");
    }
    return userRepository.findByUsername(username.trim());
  }

  @Transactional(readOnly = true)
  public List<User> findUsersContaining(String pattern) {
    if (pattern == null) {
      throw new IllegalArgumentException("Шаблон поиска не может быть null");
    }
    return userRepository.findByUsernameContaining(pattern);
  }

  @Transactional(readOnly = true)
  public long getUserCount() {
    return userRepository.count();
  }
}