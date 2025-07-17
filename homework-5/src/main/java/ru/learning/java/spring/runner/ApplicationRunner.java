package ru.learning.java.spring.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.learning.java.spring.model.User;
import ru.learning.java.spring.service.UserService;

import java.util.List;
import java.util.Optional;

@Component
public class ApplicationRunner implements CommandLineRunner {

  private final UserService userService;

  @Autowired
  public ApplicationRunner(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void run(String... args) {
    System.out.println("=== Демонстрация работы с пользователями ===");
    System.out.println();

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
    Optional<User> foundUser = userService.getUserById(user1.getId());
    foundUser.ifPresent(user -> System.out.println("Найден пользователь: " + user));
    System.out.println();

    // 4. Обновление пользователя
    System.out.println("4. Обновление пользователя:");
    User updatedUser = userService.updateUser(user2.getId(), "jane_doe_updated");
    System.out.println("Обновлен пользователь: " + updatedUser);
    System.out.println();

    // 5. Все пользователи после обновления
    System.out.println("5. Все пользователи после обновления:");
    allUsers = userService.getAllUsers();
    allUsers.forEach(System.out::println);
    System.out.println();

    // 6. Удаление пользователя
    System.out.println("6. Удаление пользователя:");
    boolean deleted = userService.deleteUser(user3.getId());
    System.out.println("Пользователь удален: " + deleted);
    System.out.println();

    // 7. Все пользователи после удаления
    System.out.println("7. Все пользователи после удаления:");
    allUsers = userService.getAllUsers();
    allUsers.forEach(System.out::println);

    System.out.println("=== Завершение демонстрация работы с пользователями ===");
  }
}
