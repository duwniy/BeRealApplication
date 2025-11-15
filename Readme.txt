# BEREAL APPLICATION - ПОЛНАЯ ДОКУМЕНТАЦИЯ

```
  ____       ____            _ 
 |  _ \     |  _ \          | |
 | |_) | ___| |_) | ___  ___| |
 |  _ < / _ \  _ < / _ \/ __| |
 | |_) |  __/ |_) |  __/\__ \_|
 |____/ \___|____/ \___||___(_)
```

**Версия:** 1.0.0  
**Дата обновления:** 13 ноября 2024  
**Лицензия:** MIT

---

## 📋 СОДЕРЖАНИЕ

1. [О проекте](#о-проекте)
2. [Быстрый старт](#быстрый-старт)
3. [Технологии](#технологии)
4. [Архитектура](#архитектура)
5. [Установка](#установка)
6. [Docker деплой](#docker-деплой)
7. [API документация](#api-документация)
8. [Конфигурация](#конфигурация)
9. [Тестирование](#тестирование)
10. [Production](#production)
11. [Troubleshooting](#troubleshooting)
12. [FAQ](#faq)

---

## 🎯 О ПРОЕКТЕ

BeReal Application - это REST API клон популярного приложения BeReal, построенный на Spring Boot. Приложение позволяет пользователям делиться аутентичными моментами своей жизни один раз в день в случайно выбранное время.

### Ключевые особенности

- 🔐 **JWT аутентификация** - Безопасная система входа
- 📅 **Один пост в день** - Ограничение на публикацию
- ⏰ **Случайное время BeReal** - Генерируется каждый день
- 📸 **Двойная камера** - Два изображения на пост
- 👥 **Система друзей** - Запросы и управление дружбой
- 🔒 **Уровни приватности** - PUBLIC / FRIENDS_ONLY / PRIVATE
- 🐳 **Docker ready** - Полная контейнеризация

### Что приложение умеет

- Регистрация и авторизация пользователей
- Создание постов с двумя изображениями
- Автоматическое определение опозданий (late posts)
- Отправка и принятие запросов в друзья
- Фильтрация постов по видимости
- Хранение изображений на сервере
- Пагинация списков

---

## ⚡ БЫСТРЫЙ СТАРТ

### Вариант 1: Docker (рекомендуется)

```bash
# Клонировать репозиторий
git clone https://github.com/yourusername/BeRealApplication.git
cd BeRealApplication

# Создать .env файл
cat > .env << 'EOF'
POSTGRES_DB=bereal_db
POSTGRES_USER=bereal_user
POSTGRES_PASSWORD=change_me_strong_password_123
JWT_SECRET=change_me_generate_with_openssl_rand_base64_64
JWT_EXPIRATION=86400000
SERVER_PORT=8080
EOF

# Запустить через Docker
docker-compose up -d

# Проверить статус
docker-compose ps
```

Приложение доступно на `http://localhost:8080`

### Вариант 2: Локальная установка

```bash
# Предварительные требования
# - Java 17+
# - Maven 3.6+
# - PostgreSQL 15+

# Создать БД
createdb bereal_db

# Настроить application.properties
# (см. раздел Конфигурация)

# Собрать и запустить
mvn clean install
mvn spring-boot:run
```

### Первый тест

```bash
# Регистрация
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice_example",
    "email": "alice@example.com",
    "password": "SecurePassword123",
    "fullName": "Alice Example"
  }'

# Сохраните токен из ответа
# TOKEN="<ваш_токен>"

# Получить посты
curl -X GET http://localhost:8080/api/posts \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🛠 ТЕХНОЛОГИИ

### Backend
- **Java 17** - LTS версия
- **Spring Boot 3.2.0** - Основной фреймворк
- **Spring Security** - Безопасность
- **Spring Data JPA** - ORM
- **Hibernate** - JPA реализация

### База данных
- **PostgreSQL 15** - Основная БД
- **HikariCP** - Connection pool

### Безопасность
- **JWT (jjwt 0.12.3)** - Токены аутентификации
- **BCrypt** - Хеширование паролей
- **HS512** - Алгоритм подписи JWT

### Инструменты
- **Maven** - Сборка и зависимости
- **Lombok** - Уменьшение boilerplate
- **SLF4J + Logback** - Логирование
- **Jakarta Validation** - Валидация

### DevOps
- **Docker** - Контейнеризация
- **Docker Compose** - Оркестрация
- **Alpine Linux** - Базовый образ

---

## 🏗 АРХИТЕКТУРА

### Слои приложения

```
┌─────────────────────────────┐
│   Presentation Layer        │  Controllers
├─────────────────────────────┤
│   Service Layer             │  Business Logic
├─────────────────────────────┤
│   Repository Layer          │  Data Access (JPA)
├─────────────────────────────┤
│   Database Layer            │  PostgreSQL
└─────────────────────────────┘
```

### Структура проекта

```
BeRealApplication/
├── src/main/java/org/example/bereal/
│   ├── config/
│   │   └── FileStorageConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── PostController.java
│   │   ├── FriendshipController.java
│   │   └── FileController.java
│   ├── dto/
│   │   ├── AuthRequest.java
│   │   ├── AuthResponse.java
│   │   ├── RegisterRequest.java
│   │   ├── PostDTO.java
│   │   └── FriendshipDTO.java
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── PostNotFoundException.java
│   │   ├── UserAlreadyPostedException.java
│   │   └── UnauthorizedException.java
│   ├── mapper/
│   │   ├── PostMapper.java
│   │   └── FriendshipMapper.java
│   ├── model/
│   │   ├── User.java
│   │   ├── Post.java
│   │   ├── Friendship.java
│   │   └── BeRealTime.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── PostRepository.java
│   │   ├── FriendshipRepository.java
│   │   └── BeRealTimeRepository.java
│   ├── security/
│   │   ├── SecurityConfig.java
│   │   ├── JwtUtil.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── CustomUserDetailsService.java
│   ├── service/
│   │   ├── AuthService.java
│   │   ├── PostService.java
│   │   ├── FriendshipService.java
│   │   ├── BeRealTimeService.java
│   │   ├── ImageService.java
│   │   └── PostVisibilityChecker.java
│   └── BeRealApplication.java
├── src/main/resources/
│   └── application.properties
├── uploads/images/                 (создается автоматически)
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── .env.example
├── pom.xml
└── README.md
```

### Паттерны проектирования

- **DTO Pattern** - Передача данных между слоями
- **Repository Pattern** - Абстракция доступа к данным
- **Service Layer** - Инкапсуляция бизнес-логики
- **Dependency Injection** - Внедрение зависимостей
- **Filter Chain** - JWT фильтры

---

## 📦 УСТАНОВКА

### Системные требования

**Минимальные:**
- CPU: 2 cores
- RAM: 2 GB
- Disk: 10 GB
- OS: Linux, macOS, Windows 10+

**Рекомендуемые:**
- CPU: 4+ cores
- RAM: 4+ GB
- Disk: 50 GB SSD
- OS: Ubuntu 22.04 LTS, macOS 13+

### Необходимое ПО

#### Java 17+

**Linux:**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
java -version
```

**macOS:**
```bash
brew install openjdk@17
java -version
```

**Windows:**
Скачать с https://adoptium.net/

#### Maven 3.6+

**Linux:**
```bash
sudo apt install maven
mvn -version
```

**macOS:**
```bash
brew install maven
mvn -version
```

#### PostgreSQL 15+

**Linux:**
```bash
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql
```

**macOS:**
```bash
brew install postgresql@15
brew services start postgresql@15
```

**Создание БД:**
```bash
psql -U postgres
CREATE DATABASE bereal_db;
CREATE USER bereal_user WITH PASSWORD 'your_secure_password_here';
GRANT ALL PRIVILEGES ON DATABASE bereal_db TO bereal_user;
\q
```

### Установка без Docker

```bash
# 1. Клонировать репозиторий
git clone https://github.com/yourusername/BeRealApplication.git
cd BeRealApplication

# 2. Настроить application.properties
nano src/main/resources/application.properties

# Измените на свои данные:
spring.datasource.url=jdbc:postgresql://localhost:5432/bereal_db
spring.datasource.username=bereal_user
spring.datasource.password=your_secure_password_here

# 3. Собрать проект
mvn clean install

# 4. Запустить
mvn spring-boot:run

# Или через JAR
java -jar target/bereal-1.0.0.jar
```

---

## 🐳 DOCKER ДЕПЛОЙ

### Файловая структура

Создайте следующие файлы в корне проекта:

#### 1. Dockerfile

```dockerfile
# Build stage
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -B
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
RUN mkdir -p /app/uploads/images && chmod -R 755 /app/uploads
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### 2. .dockerignore

```
target/
.idea/
*.iml
.vscode/
.DS_Store
logs/
*.log
uploads/
.git/
.gitignore
README.md
.env
```

#### 3. docker-compose.yml

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    container_name: bereal-postgres
    restart: unless-stopped
    
    environment:
      POSTGRES_DB: ${POSTGRES_DB}
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      PGDATA: /var/lib/postgresql/data/pgdata
    
    ports:
      - "5433:5432"
    
    volumes:
      - postgres_data:/var/lib/postgresql/data
    
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${POSTGRES_USER} -d ${POSTGRES_DB}"]
      interval: 10s
      timeout: 5s
      retries: 5
    
    networks:
      - bereal-network

  bereal-app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: bereal-app
    restart: unless-stopped
    
    ports:
      - "8080:8080"
    
    environment:
      DB_URL: jdbc:postgresql://postgres:5432/${POSTGRES_DB}
      DB_USERNAME: ${POSTGRES_USER}
      DB_PASSWORD: ${POSTGRES_PASSWORD}
      JWT_SECRET: ${JWT_SECRET}
      JWT_EXPIRATION: ${JWT_EXPIRATION}
      SERVER_PORT: ${SERVER_PORT}
      FILE_UPLOAD_DIR: /app/uploads/images
    
    volumes:
      - app_uploads:/app/uploads
    
    depends_on:
      postgres:
        condition: service_healthy
    
    networks:
      - bereal-network

volumes:
  postgres_data:
  app_uploads:

networks:
  bereal-network:
    driver: bridge
```

#### 4. .env.example (шаблон)

```env
# Database Configuration
POSTGRES_DB=bereal_db
POSTGRES_USER=bereal_user
POSTGRES_PASSWORD=CHANGE_ME_use_strong_password_here

# JWT Configuration (generate with: openssl rand -base64 64)
JWT_SECRET=CHANGE_ME_generate_secure_random_string_min_256_bits
JWT_EXPIRATION=86400000

# Server Configuration
SERVER_PORT=8080
```

**⚠️ ВАЖНО:** Создайте свой `.env` файл на основе `.env.example`!

```bash
# Скопируйте шаблон
cp .env.example .env

# Откройте и измените значения
nano .env

# Добавьте .env в .gitignore (если еще не добавлено)
echo ".env" >> .gitignore
```

**🔐 Генерация безопасных значений:**

```bash
# Генерация JWT секрета
openssl rand -base64 64

# Генерация пароля для БД
openssl rand -base64 24
```

### Команды Docker

```bash
# Сборка образов
docker-compose build

# Запуск контейнеров
docker-compose up -d

# Просмотр статуса
docker-compose ps

# Просмотр логов
docker-compose logs -f

# Просмотр логов конкретного сервиса
docker-compose logs -f bereal-app

# Остановка
docker-compose stop

# Остановка и удаление контейнеров
docker-compose down

# Остановка и удаление контейнеров + volumes (удалит данные!)
docker-compose down -v

# Перезапуск
docker-compose restart

# Пересборка и перезапуск одного сервиса
docker-compose build bereal-app
docker-compose up -d bereal-app
```

### Управление контейнерами

```bash
# Войти в контейнер app
docker exec -it bereal-app sh

# Войти в контейнер postgres
docker exec -it bereal-postgres bash

# Подключиться к PostgreSQL
docker exec -it bereal-postgres psql -U bereal_user -d bereal_db

# Выполнить команду без входа
docker exec bereal-app ls -la /app/uploads/images

# Мониторинг ресурсов
docker stats bereal-app bereal-postgres

# Посмотреть логи с конкретной даты
docker-compose logs --since 2024-11-13T10:00:00 bereal-app
```

---

## 📡 API ДОКУМЕНТАЦИЯ

### Базовый URL

```
http://localhost:8080
```

### Authentication Endpoints

#### POST /api/auth/register

Регистрация нового пользователя.

**Request:**
```json
{
  "username": "alice_smith",
  "email": "alice.smith@example.com",
  "password": "SecurePassword123!",
  "fullName": "Alice Smith"
}
```

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOjEsInN1YiI6ImFsaWNlX3NtaXRoIiwiaWF0IjoxNzAwMDAwMDAwLCJleHAiOjE3MDAwODY0MDB9...",
  "userId": 1,
  "username": "alice_smith",
  "email": "alice.smith@example.com"
}
```

#### POST /api/auth/login

Авторизация пользователя.

**Request:**
```json
{
  "username": "alice_smith",
  "password": "SecurePassword123!"
}
```

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": 1,
  "username": "alice_smith",
  "email": "alice.smith@example.com"
}
```

### Post Endpoints

Все endpoints требуют JWT токен:
```
Authorization: Bearer <token>
```

#### POST /api/posts

Создать новый пост.

**Request (multipart/form-data):**
- `primaryImage`: file (required)
- `secondaryImage`: file (required)
- `caption`: string (optional, max 200)
- `visibility`: string (optional, default: PUBLIC)

**Response (201):**
```json
{
  "id": 1,
  "userId": 1,
  "primaryImageUrl": "http://localhost:8080/images/1_primary_a1b2c3d4.jpg",
  "secondaryImageUrl": "http://localhost:8080/images/1_secondary_e5f6g7h8.jpg",
  "postedAt": "2024-11-13T10:00:00",
  "isLate": false,
  "caption": "My first BeReal!",
  "visibility": "PUBLIC"
}
```

#### GET /api/posts

Получить список постов с пагинацией.

**Query Parameters:**
- `page`: int (default: 0)
- `size`: int (default: 20)
- `sortBy`: string (default: postedAt)
- `sortDir`: string (default: desc)

**Response (200):**
```json
{
  "content": [...],
  "pageable": {...},
  "totalPages": 5,
  "totalElements": 100,
  "size": 20,
  "number": 0
}
```

#### GET /api/posts/{id}

Получить пост по ID.

#### GET /api/posts/today

Получить посты за сегодня.

#### GET /api/posts/friends

Получить посты друзей.

#### GET /api/posts/user/{userId}

Получить посты конкретного пользователя.

#### PUT /api/posts/{id}

Обновить пост (только caption и visibility).

#### DELETE /api/posts/{id}

Удалить пост.

### Friendship Endpoints

#### POST /api/friends/request/{friendId}

Отправить запрос в друзья.

#### POST /api/friends/accept/{requesterId}

Принять запрос в друзья.

#### POST /api/friends/reject/{requesterId}

Отклонить запрос.

#### DELETE /api/friends/{friendId}

Удалить из друзей.

#### GET /api/friends

Получить список ID друзей.

#### GET /api/friends/requests/pending

Получить входящие запросы.

#### GET /api/friends/requests/sent

Получить исходящие запросы.

### File Endpoints

#### GET /images/{filename}

Получить изображение.

### HTTP Status Codes

- `200 OK` - Успешный GET/PUT
- `201 Created` - Успешный POST
- `204 No Content` - Успешный DELETE
- `400 Bad Request` - Ошибка валидации
- `401 Unauthorized` - Неверные credentials
- `403 Forbidden` - Нет прав доступа
- `404 Not Found` - Ресурс не найден
- `409 Conflict` - Конфликт бизнес-логики
- `413 Payload Too Large` - Файл слишком большой
- `500 Internal Server Error` - Ошибка сервера

---

## ⚙️ КОНФИГУРАЦИЯ

### application.properties

```properties
# Database
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/bereal_db}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Server
server.port=${SERVER_PORT:8080}

# JWT
jwt.secret=${JWT_SECRET:CHANGE_ME_INSECURE_DEFAULT}
jwt.expiration=${JWT_EXPIRATION:86400000}

# File Upload
file.upload-dir=${FILE_UPLOAD_DIR:uploads/images}
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging
logging.level.org.example.bereal=INFO
logging.level.org.springframework.security=INFO
```

### Переменные окружения

Все настройки можно переопределить через переменные окружения:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/bereal_db
export DB_USERNAME=bereal_user
export DB_PASSWORD=your_secure_password
export JWT_SECRET=your_generated_jwt_secret
export JWT_EXPIRATION=86400000
export SERVER_PORT=8080
```

### Генерация JWT секрета

```bash
# Генерация 64-байтового ключа
openssl rand -base64 64
```

**Пример результата:**
```
Zx9kLm3nP7qR2tU5wX8yA1bC4dE6fG9hI0jK3lM6nO8pQ1rS4tU7vW0xY3zA6b9cD2eF5gH8iJ1kL4mN7oP0qR3sT6uV9wX2yZ5aB8cD1eF4gG7hI
```

---

## 🧪 ТЕСТИРОВАНИЕ

### Запуск тестов

```bash
# Все тесты
mvn test

# Конкретный класс
mvn test -Dtest=PostServiceTest

# С покрытием кода
mvn clean test jacoco:report

# Просмотр отчета
open target/site/jacoco/index.html
```

### Примеры тестов

**Unit Test:**
```java
@Test
void createPost_ThrowsException_WhenUserAlreadyPostedToday() {
    when(postRepository.findByUserIdAndPostedAtBetween(anyLong(), any(), any()))
        .thenReturn(List.of(testPost));
    
    assertThrows(UserAlreadyPostedException.class, () -> {
        postService.createPost(testPost);
    });
}
```

**Integration Test:**
```java
@Test
void createPost_ReturnsCreated_WhenValidRequest() throws Exception {
    mockMvc.perform(multipart("/api/posts")
            .file(primaryImage)
            .file(secondaryImage)
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists());
}
```

---

## 🚀 PRODUCTION

### Подготовка к production

**Checklist:**

- [ ] Изменить `jwt.secret` на безопасный
- [ ] Установить `spring.jpa.hibernate.ddl-auto=validate`
- [ ] Настроить production БД
- [ ] Включить HTTPS
- [ ] Настроить CORS для frontend
- [ ] Добавить rate limiting
- [ ] Настроить логирование в файл
- [ ] Настроить мониторинг
- [ ] Создать backup стратегию
- [ ] Настроить CI/CD

### Production .env

```env
# Production Database
POSTGRES_DB=bereal_prod
POSTGRES_USER=bereal_prod_user
POSTGRES_PASSWORD=GENERATE_STRONG_PASSWORD_HERE

# Production JWT (ОБЯЗАТЕЛЬНО сгенерируйте новый!)
JWT_SECRET=GENERATE_NEW_SECRET_WITH_OPENSSL
JWT_EXPIRATION=3600000

# Server
SERVER_PORT=8080
```

### Nginx Reverse Proxy

```nginx
upstream bereal {
    server localhost:8080;
}

server {
    listen 80;
    server_name yourdomain.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name yourdomain.com;
    
    ssl_certificate /etc/letsencrypt/live/yourdomain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/yourdomain.com/privkey.pem;
    
    client_max_body_size 10M;
    
    location / {
        proxy_pass http://bereal;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
    
    location /images/ {
        proxy_pass http://bereal/images/;
        expires 7d;
        add_header Cache-Control "public, immutable";
    }
}
```

### Backup базы данных

```bash
# Создать backup
docker exec bereal-postgres pg_dump -U bereal_user bereal_db > backup_$(date +%Y%m%d).sql

# Восстановить backup
docker exec -i bereal-postgres psql -U bereal_user bereal_db < backup_20241113.sql

# Автоматический backup (crontab)
0 2 * * * /path/to/backup.sh
```

---

## 🔧 TROUBLESHOOTING

### Проблема: Port 5432 already in use

**Решение:** Измените внешний порт в docker-compose.yml

```yaml
ports:
  - "5433:5432"  # Вместо 5432:5432
```

### Проблема: role "bereal_user" does not exist

**Решение:** Пересоздайте контейнеры с volumes

```bash
docker-compose down -v
docker-compose up -d
```

### Проблема: 403 Forbidden

**Решение:** Добавьте JWT токен в заголовок

```bash
curl -H "Authorization: Bearer <token>" http://localhost:8080/api/posts
```

### Проблема: Connection refused к PostgreSQL

**Решение:** Проверьте что PostgreSQL запущен

```bash
docker-compose logs postgres
docker-compose ps
```

### Проблема: JWT secret too short

**Решение:** Используйте минимум 256-битный секрет

```bash
# Генерация безопасного секрета
openssl rand -base64 64
```

### Логи и диагностика

```bash
# Все логи
docker-compose logs -f

# Логи приложения
docker-compose logs -f bereal-app

# Логи PostgreSQL
docker-compose logs -f postgres

# Последние 100 строк
docker-compose logs --tail=100 bereal-app
```

---

## ❓ FAQ

**Q: Как изменить максимальный размер файла?**

A: В application.properties:
```properties
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB
```

**Q: Как подключить frontend приложение?**

A: Настройте CORS в SecurityConfig:
```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    config.setAllowedHeaders(Arrays.asList("*"));
    config.setAllowCredentials(true);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
}
```

**Q: Где хранятся загруженные изображения?**

A: В директории `uploads/images/` на хосте или в Docker volume `app_uploads`.

**Q: Срок жизни JWT токена?**

A: По умолчанию 24 часа (86400000 ms). Настраивается через:
```properties
jwt.expiration=3600000  # 1 час
```

**Q: Как очистить Docker и начать заново?**

A:
```bash
docker-compose down -v
docker rmi $(docker images -q bereal*)
docker system prune -a
docker-compose build --no-cache
docker-compose up -d
```

---

## 📚 ДОПОЛНИТЕЛЬНЫЕ РЕСУРСЫ

### Полезные ссылки

- **Spring Boot Docs:** https://spring.io/projects/spring-boot
- **Spring Security:** https://spring.io/projects/spring-security
- **JWT.io:** https://jwt.io/
- **Docker Docs:** https://docs.docker.com/
- **PostgreSQL Docs:** https://www.postgresql.org/docs/

### Инструменты для тестирования API

**Postman Collection:**
```json
{
  "info": {
    "name": "BeReal API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Auth",
      "item": [
        {
          "name": "Register",
          "request": {
            "method": "POST",
            "url": "{{base_url}}/api/auth/register"
          }
        },
        {
          "name": "Login",
          "request": {
            "method": "POST",
            "url": "{{base_url}}/api/auth/login"
          }
        }
      ]
    }
  ]
}
```

**cURL примеры:**

```bash
# 1. Регистрация
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "TestPass123",
    "fullName": "Test User"
  }' | jq -r '.token')

# 2. Создание поста
curl -X POST http://localhost:8080/api/posts \
  -H "Authorization: Bearer $TOKEN" \
  -F "primaryImage=@image1.jpg" \
  -F "secondaryImage=@image2.jpg" \
  -F "caption=My moment" \
  -F "visibility=PUBLIC"

# 3. Получение постов
curl -X GET "http://localhost:8080/api/posts?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN"

# 4. Отправка запроса в друзья
curl -X POST http://localhost:8080/api/friends/request/2 \
  -H "Authorization: Bearer $TOKEN"
```

---

## 📊 СТРУКТУРА БД

### ER Диаграмма

```
┌─────────────────┐
│      Users      │
├─────────────────┤
│ id (PK)         │
│ username        │
│ email           │
│ password_hash   │
│ full_name       │
│ created_at      │
│ updated_at      │
└────────┬────────┘
         │
         ├─────────────┬──────────────┐
         │             │              │
         ↓             ↓              ↓
    ┌────────────┐ ┌──────────────┐ ┌──────────────┐
    │   Posts    │ │ Friendships  │ │ BeRealTimes  │
    ├────────────┤ ├──────────────┤ ├──────────────┤
    │ id (PK)    │ │ id (PK)      │ │ id (PK)      │
    │ user_id(FK)│ │ user_id (FK) │ │ user_id (FK) │
    │ caption    │ │ friend_id(FK)│ │ time_of_day  │
    │ visibility │ │ status       │ │ created_at   │
    │ is_late    │ │ created_at   │ │ updated_at   │
    │ posted_at  │ │ updated_at   │ └──────────────┘
    │ created_at │ └──────────────┘
    │ updated_at │
    └────────────┘

┌──────────────────────┐
│   PostImages         │
├──────────────────────┤
│ id (PK)              │
│ post_id (FK)         │
│ image_url            │
│ image_type           │
│ (PRIMARY/SECONDARY)  │
│ created_at           │
└──────────────────────┘
```

### SQL Schema

```sql
-- Users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Posts
CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    caption VARCHAR(200),
    visibility VARCHAR(50) DEFAULT 'PUBLIC',
    is_late BOOLEAN DEFAULT FALSE,
    posted_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Post Images
CREATE TABLE post_images (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    image_url VARCHAR(500) NOT NULL,
    image_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Friendships
CREATE TABLE friendships (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    friend_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, friend_id)
);

-- BeReal Times
CREATE TABLE bereal_times (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    time_of_day TIME NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_posts_user_id ON posts(user_id);
CREATE INDEX idx_posts_posted_at ON posts(posted_at);
CREATE INDEX idx_friendships_user_id ON friendships(user_id);
CREATE INDEX idx_friendships_status ON friendships(status);
```

---

## 🔄 WORKFLOW ПРИМЕРЫ

### Workflow 1: Ежедневный пост

```
1. Система генерирует случайное время для пользователя
   └─> BeRealTime запись создана/обновлена
   
2. В случайное время уведомление пользователю
   └─> Frontend получает время через /api/bereal/time
   
3. Пользователь загружает две фотографии
   └─> POST /api/posts (с двумя файлами)
   
4. Система проверяет:
   ├─> Прошло ли 24 часа с последнего поста?
   ├─> Вовремя ли пост? (is_late = false)
   └─> Корректны ли изображения?
   
5. Пост сохраняется в БД
   └─> Друзьям отправляется уведомление
   
6. Друзья видят пост в ленте
   └─> Только если visibility = PUBLIC/FRIENDS_ONLY
```

### Workflow 2: Добавление в друзья

```
1. Пользователь A отправляет запрос
   └─> POST /api/friends/request/{userId_B}
   
2. В БД создается запись Friendship
   └─> status = 'PENDING'
   
3. Пользователь B видит входящий запрос
   └─> GET /api/friends/requests/pending
   
4. Пользователь B принимает запрос
   └─> POST /api/friends/accept/{userId_A}
   
5. Оба видят друг друга в списке друзей
   └─> GET /api/friends (возвращает ID всех друзей)
   
6. Теперь видят посты друг друга
   └─> GET /api/posts/friends (получить посты друзей)
```

### Workflow 3: Просмотр постов

```
Видимость: PUBLIC
├─> Видят: Все авторизованные пользователи
├─> Условие: visibility = 'PUBLIC'
└─> Запрос: GET /api/posts (все посты)

Видимость: FRIENDS_ONLY
├─> Видят: Только друзья автора
├─> Условие: visibility = 'FRIENDS_ONLY' И user_id в списке друзей
└─> Запрос: GET /api/posts/friends

Видимость: PRIVATE
├─> Видят: Только автор
├─> Условие: user_id = current_user_id
└─> Запрос: GET /api/posts/user/{userId}
```

---

## 🔐 SECURITY BEST PRACTICES

### На что обратить внимание

1. **JWT Token Security:**
   - Никогда не передавайте токен в URL
   - Используйте только HTTPS в production
   - Установите короткий TTL (1-2 часа)
   - Экспортируйте токен в HttpOnly cookies

2. **Пароли:**
   - Минимум 8 символов
   - Требуйте большие/маленькие буквы, цифры, спецсимволы
   - Используйте BCrypt с cost factor 10+
   - Никогда не логируйте пароли

3. **Файлы:**
   - Валидируйте тип файла (магические байты)
   - Ограничивайте размер (по умолчанию 10MB)
   - Храните вне web root
   - Генерируйте случайные имена файлов
   - Сканируйте на вирусы (в production)

4. **Database:**
   - Используйте параметризованные запросы (JPA)
   - Ограничьте права пользователя БД
   - Регулярно обновляйте PostgreSQL
   - Включите SSL для подключений

5. **API:**
   - Добавьте rate limiting
   - Валидируйте все input данные
   - Используйте CORS правильно
   - Логируйте попытки атак
   - Скрывайте версии фреймворков

### Rate Limiting (пример с Spring)

```java
@Configuration
public class RateLimitConfig {
    @Bean
    public RemoteIpFilter remoteIpFilter() {
        return new RemoteIpFilter();
    }
}

@Component
@Aspect
public class RateLimitAspect {
    private final Map<String, RateLimiter> limiters = new ConcurrentHashMap<>();
    
    @Before("@annotation(rateLimit)")
    public void checkRateLimit(JoinPoint joinPoint, RateLimit rateLimit) 
            throws Throwable {
        String key = getCurrentUserIp();
        RateLimiter limiter = limiters.computeIfAbsent(key, 
            k -> RateLimiter.create(rateLimit.value()));
        
        if (!limiter.tryAcquire()) {
            throw new TooManyRequestsException("Rate limit exceeded");
        }
    }
    
    private String getCurrentUserIp() {
        ServletRequestAttributes attrs = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest().getRemoteAddr();
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimit {
    double value() default 10.0; // requests per second
}
```

---

## 🎓 ПРИМЕРЫ ИСПОЛЬЗОВАНИЯ

### Сценарий 1: Регистрация и создание первого поста

```bash
#!/bin/bash

BASE_URL="http://localhost:8080"

# Регистрация
echo "🔐 Registering user..."
RESPONSE=$(curl -s -X POST $BASE_URL/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "SecurePass123!",
    "fullName": "John Doe"
  }')

TOKEN=$(echo $RESPONSE | jq -r '.token')
echo "✅ Token: $TOKEN"

# Создание поста
echo "📸 Creating post..."
curl -X POST $BASE_URL/api/posts \
  -H "Authorization: Bearer $TOKEN" \
  -F "primaryImage=@front_camera.jpg" \
  -F "secondaryImage=@back_camera.jpg" \
  -F "caption=First BeReal!" \
  -F "visibility=PUBLIC"

echo "✅ Post created!"
```

### Сценарий 2: Добавление друга

```bash
#!/bin/bash

TOKEN="your_token_here"
FRIEND_ID=2

# Отправить запрос
curl -X POST http://localhost:8080/api/friends/request/$FRIEND_ID \
  -H "Authorization: Bearer $TOKEN"

echo "✅ Friend request sent"

# Получить входящие запросы (от друга)
curl -X GET http://localhost:8080/api/friends/requests/pending \
  -H "Authorization: Bearer $TOKEN"
```

### Сценарий 3: Просмотр постов друзей

```bash
#!/bin/bash

TOKEN="your_token_here"

# Получить посты друзей
curl -X GET http://localhost:8080/api/posts/friends?page=0&size=20 \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

---

## 🚨 ИЗВЕСТНЫЕ ПРОБЛЕМЫ И РЕШЕНИЯ

### Issue 1: OutOfMemoryError при загрузке больших файлов

**Симптомы:** Приложение падает при загрузке файлов > 50MB

**Решение:**
```bash
# Увеличьте heap size
export JAVA_OPTS="-Xms512m -Xmx2g"
docker-compose restart bereal-app
```

### Issue 2: Duplicate key value violates unique constraint "friendships_user_id_friend_id_key"

**Симптомы:** Ошибка при отправке второго запроса в друзья

**Решение:** Проверьте статус существующего запроса перед отправкой
```java
public void sendFriendRequest(Long userId, Long friendId) {
    Optional<Friendship> existing = friendshipRepository
        .findByUserIdAndFriendId(userId, friendId);
    
    if (existing.isPresent()) {
        throw new FriendshipAlreadyExistsException("Request already sent");
    }
    // ... создайте новый запрос
}
```

### Issue 3: Изображения не загружаются после перезапуска

**Симптомы:** 404 ошибка при попытке получить загруженное изображение

**Решение:** Используйте Docker volumes (см. docker-compose.yml)

### Issue 4: JWT токен истекает слишком быстро

**Симптомы:** 401 Unauthorized через 5-10 минут

**Решение:** Проверьте значение `jwt.expiration`
```bash
# Текущее значение
docker exec bereal-app grep jwt.expiration /app/config/application.properties

# Измените на 24 часа (86400000 ms)
JWT_EXPIRATION=86400000
```

---

## 📈 PERFORMANCE OPTIMIZATION

### Кэширование

```java
@Service
public class PostService {
    
    @Cacheable("posts")
    public Page<PostDTO> getAllPosts(int page, int size) {
        return postRepository.findAll(PageRequest.of(page, size))
            .map(postMapper::toDTO);
    }
    
    @CacheEvict(value = "posts", allEntries = true)
    public PostDTO createPost(PostRequest request) {
        // ... создание поста
    }
}
```

**application.properties:**
```properties
spring.cache.type=caffeine
spring.cache.caffeine.spec=maximumSize=500,expireAfterWrite=10m
```

### Batch операции

```java
// Вместо отдельных запросов
List<Post> posts = new ArrayList<>();
for (PostDTO dto : dtos) {
    posts.add(postMapper.toEntity(dto));
}
postRepository.saveAll(posts); // Одна batch операция

// Вместо N+1 queries
@Query("SELECT DISTINCT p FROM Post p JOIN FETCH p.user u")
List<Post> getAllPostsWithUsers();
```

### Query Optimization

```java
// Добавьте @NamedQuery для часто используемых запросов
@NamedQueries({
    @NamedQuery(name = "Post.findTodayPosts",
        query = "SELECT p FROM Post p WHERE DATE(p.postedAt) = CURRENT_DATE"),
    @NamedQuery(name = "Post.findFriendPosts",
        query = "SELECT p FROM Post p WHERE p.user IN " +
                "(SELECT f.friend FROM Friendship f WHERE f.user.id = ?1)")
})
public class Post { ... }
```

---

## 🧠 BEST PRACTICES

### Code Organization

✅ **ДА:**
```java
// Четкое разделение ответственности
@Service
public class PostService {
    private final PostRepository postRepository;
    private final ImageService imageService;
    
    public PostDTO createPost(PostRequest request) {
        validatePost(request);
        Post post = savePost(request);
        String[] imageUrls = imageService.saveImages(request.getImages());
        return mapToDTO(post, imageUrls);
    }
}
```

❌ **НЕТ:**
```java
// Смешивание ответственностей
@Service
public class PostService {
    public void createPost(PostRequest request) {
        // Валидация, сохранение, обработка файлов - все вместе
        // ...1000 строк кода...
    }
}
```

### Error Handling

✅ **ДА:**
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyPostedException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyPosted(
            UserAlreadyPostedException ex) {
        return ResponseEntity.status(409)
            .body(new ErrorResponse("USER_ALREADY_POSTED", ex.getMessage()));
    }
}
```

❌ **НЕТ:**
```java
// Ловить все исключения
try {
    postService.createPost(request);
} catch (Exception e) {
    return ResponseEntity.status(500).body("Error");
}
```

### Logging

✅ **ДА:**
```java
private static final Logger logger = LoggerFactory.getLogger(PostService.class);

public PostDTO createPost(PostRequest request) {
    logger.info("Creating post for user: {}", request.getUserId());
    try {
        Post post = postRepository.save(mapToEntity(request));
        logger.info("Post created successfully: {}", post.getId());
        return mapToDTO(post);
    } catch (Exception e) {
        logger.error("Error creating post for user: {}", request.getUserId(), e);
        throw new PostCreationException("Failed to create post", e);
    }
}
```

❌ **НЕТ:**
```java
System.out.println("Creating post"); // Плохо
// или
logger.info("user password: " + password); // Опасно
```

---

## 🎉 ЗАКЛЮЧЕНИЕ

BeReal Application - это полнофункциональное приложение для обмена аутентичными моментами жизни. Документация охватывает все аспекты разработки, развертывания и поддержки приложения.

### Основные достижения

✅ REST API с JWT аутентификацией  
✅ PostgreSQL база данных с правильной схемой  
✅ Docker контейнеризация и orchestration  
✅ Security best practices  
✅ Production-ready конфигурация  
✅ Полная документация и примеры  

### Дальнейшее развитие

- Добавить real-time notifications (WebSocket)
- Реализовать image compression и optimization
- Добавить API rate limiting и throttling
- Настроить CI/CD pipeline (GitHub Actions)
- Добавить monitoring и alerting (Prometheus + Grafana)
- Реализовать кэширование (Redis)

### Контакты и поддержка

Для вопросов и предложений создавайте issues в репозитории GitHub или обращайтесь к команде разработки.

**Спасибо за использование BeReal Application!** 🚀
