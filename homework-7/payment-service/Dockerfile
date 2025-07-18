FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/payment-service-*.jar payment-service.jar

EXPOSE 8080

CMD ["java", "-jar", "payment-service.jar"]