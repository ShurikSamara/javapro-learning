package ru.learning.java.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

  @Bean
  public DataSource dataSource() {
    HikariConfig config = new HikariConfig();

    // Получение параметров из переменных окружения
    String dbHost = System.getenv().getOrDefault("DB_HOST", "localhost");
    String dbPort = System.getenv().getOrDefault("DB_PORT", "5432");
    String dbName = System.getenv().getOrDefault("DB_NAME", "userdb");
    String dbUser = System.getenv().getOrDefault("DB_USER", "postgres");
    String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "password");

    config.setJdbcUrl(String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName));
    config.setUsername(dbUser);
    config.setPassword(dbPassword);
    config.setDriverClassName("org.postgresql.Driver");

    // Настройки пула соединений
    config.setMaximumPoolSize(10);
    config.setMinimumIdle(5);
    config.setConnectionTimeout(30000);
    config.setIdleTimeout(600000);
    config.setMaxLifetime(1800000);
    config.setLeakDetectionThreshold(60000);
    config.setConnectionTestQuery("SELECT 1");

    return new HikariDataSource(config);
  }
}