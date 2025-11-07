================================================================================
README.TXT - BEREAL-LIKE SOCIAL NETWORK BACKEND
================================================================================

1. ОБЗОР ПРОЕКТА
----------------
Проект:           BeReal-Like Social Network Backend (API)
Назначение:       Предоставление RESTful API для мобильного приложения с логикой
                  публикации постов в ограниченное время (концепция BeReal).
Технология:       Java, Spring Boot 3.x

--------------------------------------------------------------------------------

2. ТЕХНИЧЕСКИЙ СТЕК

- Язык:           Java (JDK 21+)
- Фреймворк:      Spring Boot 3.x
- ORM:            Spring Data JPA / Hibernate
- База данных:    PostgreSQL / MySQL (необходимо настроить)
- Зависимости:    Spring Web, Spring Data JPA, Lombok.

--------------------------------------------------------------------------------

3. ТЕКУЩАЯ СТРУКТУРА КОДА (CRUD для POST)

На данный момент реализована базовая архитектура для работы с сущностью Post.

3.1. МОДЕЛЬ ДАННЫХ (Model/Entity): Post.java
-------------------------------------------
Расположение:      src/main/java/org/example/bereal/model/Post.java
Назначение:       Представляет собой запись о посте в базе данных (таблица 'posts').

Ключевые поля:
- id:               Уникальный идентификатор
- userId:           ID автора поста
- primaryImageUrl:  URL изображения с задней камеры
- secondaryImageUrl:URL изображения с передней камеры (селфи)
- postedAt:         Время публикации
- isLate:           Флаг, указывающий, сделан ли пост поздно

3.2. РЕПОЗИТОРИЙ (Repository): PostRepository.java
--------------------------------------------------
Расположение:      src/main/java/org/example/bereal/repository/PostRepository.java
Назначение:       Уровень доступа к данным. Расширяет JpaRepository для
                  предоставления базовых CRUD-операций и кастомных запросов
                  (например, поиск постов за сегодняшний день).

3.3. СЕРВИС (Service): PostService.java
---------------------------------------
Расположение:      src/main/java/org/example/bereal/service/PostService.java
Назначение:       Уровень бизнес-логики.
Реализовано:      createPost, getPostById, getAllPosts, updatePost, deletePost.
(На доработке):   Логика проверки 2-минутного окна и загрузки файлов.

3.4. КОНТРОЛЛЕР (Controller): PostController.java
-------------------------------------------------
Расположение:      src/main/java/org/example/bereal/controller/PostController.java
Назначение:       REST API интерфейс для внешних запросов.
Реализованные эндпоинты (CRUD):
- POST    /api/posts
- GET     /api/posts/{id}
- GET     /api/posts
- PUT     /api/posts/{id}
- DELETE  /api/posts/{id}

--------------------------------------------------------------------------------

4. ТРЕБОВАНИЯ К НАСТРОЙКЕ (ВАЖНО!)

Приложение завершило работу с ошибкой: "Failed to determine a suitable driver class".
Это означает, что НЕ НАСТРОЕНЫ параметры подключения к базе данных.

ДЕЙСТВИЕ:
Создайте или отредактируйте файл 'src/main/resources/application.properties'
и добавьте следующую конфигурацию (пример для PostgreSQL):

spring.datasource.url=jdbc:postgresql://localhost:5432/bereal_db
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=your_postgres_user
spring.datasource.password=your_postgres_password
spring.jpa.hibernate.ddl-auto=update

--------------------------------------------------------------------------------

5. ПЛАН РАЗВИТИЯ (TODO)

Для полноценной работы необходимо реализовать:

1. БЕЗОПАСНОСТЬ: Внедрение Spring Security, JWT-аутентификации, сущности User.
2. ФАЙЛЫ: Реализация загрузки изображений в облачное хранилище (S3/MinIO).
3. ПЛАНИРОВАНИЕ: Сервис для отправки ежедневных случайных уведомлений (@Scheduled).
4. ЛЕНТА: Логика FeedService для показа постов только от друзей и только при условии,
   что пользователь опубликовал свой пост за сегодня.
5. РЕАКЦИИ: Сущность RealMoji для обработки реакций на посты.

================================================================================
