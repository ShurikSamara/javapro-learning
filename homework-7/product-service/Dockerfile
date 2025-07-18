FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/product-service-*.jar product-service.jar

EXPOSE 8081

CMD ["java", "-jar", "product-service.jar"]