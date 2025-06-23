package ru.learning.java.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:database.properties")
public class DatabaseProperties {

  @Value("${db.host}")
  private String host;

  @Value("${db.port}")
  private String port;

  @Value("${db.name}")
  private String name;

  @Value("${db.user}")
  private String user;

  @Value("${db.password}")
  private String password;

  @Value("${db.driver}")
  private String driver;

  @Value("${hikari.maximumPoolSize}")
  private int maximumPoolSize;

  @Value("${hikari.minimumIdle}")
  private int minimumIdle;

  @Value("${hikari.connectionTimeout}")
  private long connectionTimeout;

  @Value("${hikari.idleTimeout}")
  private long idleTimeout;

  @Value("${hikari.maxLifetime}")
  private long maxLifetime;

  @Value("${hikari.leakDetectionThreshold}")
  private long leakDetectionThreshold;

  @Value("${hikari.connectionTestQuery}")
  private String connectionTestQuery;

  // Getters
  public String getHost() { return host; }
  public String getPort() { return port; }
  public String getName() { return name; }
  public String getUser() { return user; }
  public String getPassword() { return password; }
  public String getDriver() { return driver; }
  public int getMaximumPoolSize() { return maximumPoolSize; }
  public int getMinimumIdle() { return minimumIdle; }
  public long getConnectionTimeout() { return connectionTimeout; }
  public long getIdleTimeout() { return idleTimeout; }
  public long getMaxLifetime() { return maxLifetime; }
  public long getLeakDetectionThreshold() { return leakDetectionThreshold; }
  public String getConnectionTestQuery() { return connectionTestQuery; }

  public String getJdbcUrl() {
    return String.format("jdbc:postgresql://%s:%s/%s", getHost(), getPort(), getName());
  }
}