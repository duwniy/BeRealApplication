# README

### BEREAL-LIKE SOCIAL NETWORK BACKEND

## 1. ОБЗОР ПРОЕКТА

Проект: BeReal-Like Social Network Backend (API)
Назначение: Предоставление RESTful API для мобильного приложения с логикой публикации постов в ограниченное время (концепция “BeReal”).
Технология: Java + Spring Boot 3.x

## 2. ТЕХНИЧЕСКИЙ СТЕК

* Язык: Java (JDK 21+)
* Фреймворк: Spring Boot 3.x
* ORM: Spring Data JPA / Hibernate
* База данных: PostgreSQL или MySQL
* Зависимости: Spring Web, Spring Data JPA, Lombok

## 3. ТЕКУЩАЯ СТРУКТУРА КОДА (CRUD для сущности Post)

### 3.1 Модель данных (Post.java)

Расположение: src/main/java/org/example/bereal/model/Post.java
Поля:

* id — Уникальный идентификатор
* userId — ID автора поста
* primaryImageUrl — URL изображения с задней камеры
* secondaryImageUrl — URL изображения с передней камеры (селфи)
* postedAt — Время публикации
* isLate — Флаг, указывающий, сделан ли пост поздно

### 3.2 Репозиторий (PostRepository.java)

Расположение: src/main/java/org/example/bereal/repository/PostRepository.java
Назначение: доступ к данным, расширяет JpaRepository, предоставляет CRUD-операции и кастомные запросы.

### 3.3 Сервис (PostService.java)

Расположение: src/main/java/org/example/bereal/service/PostService.java
Назначение: бизнес-логика приложения. Методы: createPost, getPostById, getAllPosts, updatePost, deletePost.

### 3.4 Контроллер (PostController.java)

Расположение: src/main/java/org/example/bereal/controller/PostController.java
Эндпоинты:

* POST /api/posts
* GET /api/posts/{id}
* GET /api/posts
* PUT /api/posts/{id}
* DELETE /api/posts/{id}

## 4. ТРЕБОВАНИЯ К НАСТРОЙКЕ

Настройка подключения к БД через src/main/resources/application.properties:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/bereal_db
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=your_postgres_user
spring.datasource.password=your_postgres_password
spring.jpa.hibernate.ddl-auto=update
```

## 5. ПЛАН РАЗВИТИЯ (TODO)

1. Безопасность: Spring Security, JWT, сущность User
2. Файлы: загрузка изображений в облачное хранилище
3. Планирование: сервис уведомлений (@Scheduled)
4. Лента: FeedService для постов друзей
5. Реакции: сущность RealMoji для реакций на посты

