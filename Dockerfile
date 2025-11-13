# СТАДИЯ 1: СБОРКА ПРОЕКТА
FROM maven:3.9.5-eclipse-temurin-17 AS build

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файлы конфигурации и исходники
COPY pom.xml .
COPY src ./src

# Собираем проект, пропуская тесты (для ускорения)
RUN mvn clean package -DskipTests

# СТАДИЯ 2: ЗАПУСК ПРИЛОЖЕНИЯ
# Используем минимальный образ JRE для меньшего размера и безопасности
FROM eclipse-temurin:17-jre-focal

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл из СТАДИИ СБОРКИ
COPY --from=build /app/target/*.jar app.jar

# Порт, который слушает Spring Boot
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]