================================================================================
BEREAL-LIKE SOCIAL NETWORK BACKEND (API)
================================================================================

1. ОБЗОР ПРОЕКТА
----------------
Проект:           BeReal-Like Social Network Backend (REST API)
Назначение:       RESTful API для мобильного приложения с логикой BeReal —
                  публикация постов с двух камер в ограниченное время.
Технологии:       Java 21+, Spring Boot 3.x, Spring Data JPA, Lombok, PostgreSQL

--------------------------------------------------------------------------------

2. ТЕКУЩЕЕ СОСТОЯНИЕ
--------------------
Реализована базовая архитектура с CRUD для сущности `Post` и поддержкой DTO,
Mapper, глобальной обработкой ошибок и выборкой постов за текущий день.

--------------------------------------------------------------------------------

3. АРХИТЕКТУРА ПРОЕКТА
-----------------------

📁 **controller/**
- `PostController.java`  
  REST API для управления постами.  
  Поддерживает эндпоинты:
  - `POST /api/posts` — создать пост  
  - `GET /api/posts/{id}` — получить пост по ID  
  - `GET /api/posts` — получить все посты  
  - `GET /api/posts/today` — посты за сегодня  
  - `PUT /api/posts/{id}` — обновить пост  
  - `DELETE /api/posts/{id}` — удалить пост  

📁 **dto/**
- `PostDTO.java`  
  Record (иммутабельный объект передачи данных).  
  Используется между контроллером и сервисом.

📁 **mapper/**
- `PostMapper.java`  
  Преобразует сущность `Post` ↔ DTO `PostDTO`.

📁 **model/**
- `Post.java`  
  Сущность JPA для таблицы `posts`.  
  Поля:
  - `id`, `userId`, `primaryImageUrl`, `secondaryImageUrl`
  - `postedAt` (автоматически устанавливается при создании)
  - `isLate`, `caption`, `visibility (PUBLIC, FRIENDS_ONLY, PRIVATE)`

📁 **repository/**
- `PostRepository.java`  
  Интерфейс `JpaRepository<Post, Long>` с кастомными методами поиска:
  - `findByUserIdAndPostedAtBetween(...)`
  - `findByPostedAtBetween(...)`

📁 **service/**
- `PostService.java`  
  Содержит бизнес-логику:
  - Проверка “опоздания” поста (`checkIfLate`)
  - CRUD-операции через `PostRepository`
  - Выборка постов за сегодня

📁 **exception/**
- `GlobalExceptionHandler.java`  
  Централизованная обработка ошибок (400, 404) с JSON-ответом.

--------------------------------------------------------------------------------

4. КОНФИГУРАЦИЯ БАЗЫ ДАННЫХ
----------------------------
Создайте файл:  
`src/main/resources/application.properties`

Пример (PostgreSQL):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bereal_db
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=your_postgres_user
spring.datasource.password=your_postgres_password
spring.jpa.hibernate.ddl-auto=update

