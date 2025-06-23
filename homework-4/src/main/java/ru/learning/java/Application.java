package ru.learning.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.learning.java.model.User;
import ru.learning.java.service.UserService;

import java.util.List;
import java.util.Optional;

@ComponentScan
public class Application {
  //test pust on master
  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);

    try (context) {
      UserService userService = context.getBean(UserService.class);
      System.out.println("=== Демонстрация работы с пользователями ===\n");
      // 1. Создание пользователей
      System.out.println("1. Создание пользователей:");
      User user1 = userService.createUser("john_doe");
      User user2 = userService.createUser("jane_smith");
      User user3 = userService.createUser("admin");

      System.out.println("Создан: " + user1);
      System.out.println("Создан: " + user2);
      System.out.println("Создан: " + user3);
      System.out.println();

      // 2. Получение всех пользователей
      System.out.println("2. Все пользователи:");
      List<User> allUsers = userService.getAllUsers();
      allUsers.forEach(System.out::println);
      System.out.println();

      // 3. Получение пользователя по ID
      System.out.println("3. Получение пользователя по ID:");
      Long userId = user1.getId();
      Optional<User> foundUser = userService.getUserById(userId);
      if (foundUser.isPresent()) {
        System.out.println("Найден пользователь: " + foundUser.get());
      } else {
        System.out.println("Пользователь с ID " + userId + " не найден");
      }
      System.out.println();

      // 4. Обновление пользователя
      System.out.println("4. Обновление пользователя:");
      User updatedUser = userService.updateUser(user2.getId(), "jane_doe_updated");
      System.out.println("Обновлен пользователь: " + updatedUser);
      System.out.println();

      // 5. Показать всех пользователей после обновления
      System.out.println("5. Все пользователи после обновления:");
      allUsers = userService.getAllUsers();
      allUsers.forEach(System.out::println);
      System.out.println();

      // 6. Удаление пользователя
      System.out.println("6. Удаление пользователя:");
      boolean deleted = userService.deleteUser(user3.getId());
      System.out.println("Пользователь удален: " + deleted);
      System.out.println();

      // 7. Показать всех пользователей после удаления
      System.out.println("7. Все пользователи после удаления:");
      allUsers = userService.getAllUsers();
      allUsers.forEach(System.out::println);

    } catch (Exception e) {
      logger.error("Ошибка при работе с пользователями: {}", e.getMessage(), e);
    }

    System.out.println("=== Завершение демонстрация работы с пользователями ===\n");
  }
}