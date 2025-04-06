# Фаза сборки
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests && \
    ls -la /app/target/ # Проверка что файл создан

# Фаза запуска
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Явное копирование с проверкой имени файла
COPY --from=builder /app/target/Spring-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]