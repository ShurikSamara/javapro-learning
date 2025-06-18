FROM openjdk:17-jdk-slim

WORKDIR /app

# Копирование файлов Maven
COPY pom.xml .
COPY src ./src

# Установка Maven
RUN apt-get update && apt-get install -y maven

# Сборка приложения
RUN mvn clean compile

# Команда запуска
CMD ["mvn", "exec:java", "-Dexec.mainClass=ru.learning.java.Application"]