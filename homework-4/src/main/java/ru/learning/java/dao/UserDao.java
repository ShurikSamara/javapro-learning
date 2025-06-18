package ru.learning.java.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.learning.java.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {

  private final DataSource dataSource;

  @Autowired
  public UserDao(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public User save(User user) {
    String sql = "INSERT INTO users (username) VALUES (?) RETURNING id";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, user.getUsername());
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        user.setId(resultSet.getLong("id"));
      }
      return user;
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при сохранении пользователя", e);
    }
  }

  public Optional<User> findById(Long id) {
    String sql = "SELECT id, username FROM users WHERE id = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setLong(1, id);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        User user = new User(
          resultSet.getLong("id"),
          resultSet.getString("username")
        );
        return Optional.of(user);
      }
      return Optional.empty();
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при поиске пользователя", e);
    }
  }

  public List<User> findAll() {
    String sql = "SELECT id, username FROM users ORDER BY id";
    List<User> users = new ArrayList<>();

    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(sql)) {

      while (resultSet.next()) {
        User user = new User(
          resultSet.getLong("id"),
          resultSet.getString("username")
        );
        users.add(user);
      }
      return users;
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при получении всех пользователей", e);
    }
  }

  public User update(User user) {
    String sql = "UPDATE users SET username = ? WHERE id = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, user.getUsername());
      statement.setLong(2, user.getId());

      int rowsAffected = statement.executeUpdate();
      if (rowsAffected == 0) {
        throw new RuntimeException("Пользователь не найден для обновления");
      }
      return user;
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при обновлении пользователя", e);
    }
  }

  public boolean deleteById(Long id) {
    String sql = "DELETE FROM users WHERE id = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setLong(1, id);
      int rowsAffected = statement.executeUpdate();
      return rowsAffected > 0;
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при удалении пользователя", e);
    }
  }
}