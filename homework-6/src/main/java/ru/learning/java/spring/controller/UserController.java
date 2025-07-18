package ru.learning.java.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.learning.java.spring.dto.UserRequest;
import ru.learning.java.spring.exception.UserNotFoundException;
import ru.learning.java.spring.model.User;
import ru.learning.java.spring.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
    Optional<User> user = userService.getUserById(id);
    return user.map(ResponseEntity::ok)
      .orElseThrow(() -> new UserNotFoundException(id));
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {
    User createdUser = userService.createUser(userRequest.getUsername());
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody UserRequest userRequest) {
    User updatedUser = userService.updateUser(id, userRequest.getUsername());
    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
    boolean deleted = userService.deleteUser(id);
    if (deleted) {
      return ResponseEntity.noContent().build();
    } else {
      throw new UserNotFoundException(id);
    }
  }

  @GetMapping("/search")
  public ResponseEntity<User> findByUsername(@RequestParam String username) {
    Optional<User> user = userService.findByUsername(username);
    return user.map(ResponseEntity::ok)
      .orElseThrow(() -> new UserNotFoundException("Пользователь с именем '" + username + "' не найден"));
  }

  @GetMapping("/search/containing")
  public ResponseEntity<List<User>> findUsersContaining(@RequestParam String pattern) {
    List<User> users = userService.findUsersContaining(pattern);
    return ResponseEntity.ok(users);
  }

  @GetMapping("/count")
  public ResponseEntity<Long> getUserCount() {
    long count = userService.getUserCount();
    return ResponseEntity.ok(count);
  }
}