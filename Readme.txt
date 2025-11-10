# BEREAL APPLICATION - ПОЛНАЯ ДОКУМЕНТАЦИЯ

================================================================================
                            СОДЕРЖАНИЕ
================================================================================

1. ВВЕДЕНИЕ
2. ОСНОВНЫЕ КОНЦЕПЦИИ
3. ТЕХНОЛОГИЧЕСКИЙ СТЕК
4. АРХИТЕКТУРА ПРОЕКТА
5. СТРУКТУРА ПРОЕКТА
6. УСТАНОВКА И НАСТРОЙКА
7. КОНФИГУРАЦИЯ
8. БЕЗОПАСНОСТЬ И АУТЕНТИФИКАЦИЯ
9. API ENDPOINTS
10. МОДЕЛИ ДАННЫХ
11. БИЗНЕС-ЛОГИКА
12. СИСТЕМА ДРУЗЕЙ
13. УПРАВЛЕНИЕ ИЗОБРАЖЕНИЯМИ
14. BEREAL TIME SYSTEM
15. ОБРАБОТКА ОШИБОК
16. ПРИМЕРЫ ИСПОЛЬЗОВАНИЯ
17. ТЕСТИРОВАНИЕ
18. PRODUCTION DEPLOYMENT
19. TROUBLESHOOTING
20. FAQ
21. ROADMAP
22. ВКЛАД В ПРОЕКТ


================================================================================
                            1. ВВЕДЕНИЕ
================================================================================

BeReal Application — это полнофункциональный RESTful API клон популярного 
мобильного приложения BeReal, разработанный на Spring Boot. Приложение 
позволяет пользователям делиться аутентичными моментами своей жизни один 
раз в день в случайно выбранное время.

МИССИЯ ПРОЕКТА:
Создать безопасное, масштабируемое и производительное backend решение для
социальной сети с акцентом на аутентичность и приватность пользователей.

ЧТО ДЕЛАЕТ ПРИЛОЖЕНИЕ:
- Позволяет пользователям регистрироваться и авторизоваться
- Генерирует случайное время каждый день для публикации
- Принимает два изображения (передняя и задняя камера)
- Управляет системой друзей с запросами
- Фильтрует видимость постов по уровням приватности
- Автоматически определяет опоздавших пользователей
- Хранит изображения на локальном сервере


================================================================================
                        2. ОСНОВНЫЕ КОНЦЕПЦИИ
================================================================================

2.1 ФИЛОСОФИЯ BEREAL
--------------------
BeReal строится на принципе "будь настоящим". Основные концепции:

1. ОДИН ПОСТ В ДЕНЬ
   - Каждый пользователь может опубликовать только один пост за сутки
   - Это ограничение обеспечивает аутентичность контента
   - Попытка создать второй пост за день приводит к ошибке
   - Счетчик постов сбрасывается в полночь по системному времени

2. СЛУЧАЙНОЕ ВРЕМЯ УВЕДОМЛЕНИЯ
   - Каждый день система генерирует случайное время между 9:00 и 23:00
   - Это время называется "Time to BeReal"
   - Пользователи получают уведомление в это время
   - У пользователей есть 2 минуты, чтобы не считаться опоздавшими

3. ДВОЙНАЯ КАМЕРА
   - Каждый пост содержит два изображения
   - Primary image (передняя камера) - что видит пользователь
   - Secondary image (задняя камера) - что видит мир
   - Это создает более полную картину момента

4. SOCIAL NETWORK
   - Система друзей с отправкой запросов
   - Три уровня приватности постов
   - Фильтрация контента по дружбе
   - Просмотр постов друзей и глобальной ленты

2.2 УРОВНИ ПРИВАТНОСТИ
----------------------
PUBLIC:
- Пост виден всем пользователям системы
- Отображается в глобальной ленте
- Доступен для просмотра без дружбы

FRIENDS_ONLY:
- Пост виден только друзьям пользователя
- Требуется подтвержденная дружба (статус ACCEPTED)
- Не отображается в глобальной ленте

PRIVATE:
- Пост виден только владельцу
- Полностью скрыт от других пользователей
- Используется для черновиков или личных воспоминаний

2.3 СТАТУСЫ ДРУЖБЫ
------------------
PENDING:
- Запрос отправлен, ожидает ответа
- Получатель видит входящий запрос
- Отправитель видит исходящий запрос

ACCEPTED:
- Запрос принят, пользователи теперь друзья
- Могут видеть FRIENDS_ONLY посты друг друга
- Отображаются в списке друзей

REJECTED:
- Запрос отклонен получателем
- Дружба не установлена
- Можно отправить новый запрос позже

BLOCKED:
- Пользователь заблокирован
- Полное отсутствие взаимодействия
- Не видят посты друг друга


================================================================================
                        3. ТЕХНОЛОГИЧЕСКИЙ СТЕК
================================================================================

3.1 BACKEND ФРЕЙМВОРК
---------------------
Java 17 (LTS версия)
Причины выбора:
- Долгосрочная поддержка до 2029 года
- Современные language features (Records, Pattern Matching)
- Отличная производительность и стабильность
- Обширная экосистема библиотек

Spring Boot 3.2.0
Причины выбора:
- Автоконфигурация и Convention over Configuration
- Встроенный сервер (Tomcat/Jetty/Undertow)
- Production-ready features (Actuator, Metrics)
- Огромное сообщество и документация

Spring Framework Modules:
- Spring MVC: RESTful контроллеры
- Spring Data JPA: Абстракция над базой данных
- Spring Security: Аутентификация и авторизация
- Spring AOP: Aspect-oriented programming для логирования

3.2 БАЗА ДАННЫХ
---------------
PostgreSQL 15
Причины выбора:
- ACID транзакции для целостности данных
- Богатые типы данных (JSON, Arrays, ENUM)
- Высокая производительность для read-heavy нагрузок
- Отличная поддержка индексов

JPA/Hibernate
- ORM маппинг Entity ↔ Таблицы
- Автоматическая генерация схемы
- Ленивая загрузка связанных сущностей
- HQL для сложных запросов

3.3 БЕЗОПАСНОСТЬ
----------------
Spring Security 6.x
- Защита endpoints через фильтры
- Поддержка различных механизмов аутентификации
- CSRF защита
- Session management

JWT (JSON Web Tokens)
Библиотека: jjwt 0.12.3
Алгоритм: HS512 (HMAC-SHA512)
Структура токена:
- Header: алгоритм и тип токена
- Payload: userId, username, iat, exp
- Signature: криптографическая подпись

BCrypt Password Encoder
- Adaptive hashing function
- Автоматическое добавление соли (salt)
- Стойкость к brute-force атакам
- Rounds: 10 (по умолчанию)

3.4 ИНСТРУМЕНТЫ РАЗРАБОТКИ
--------------------------
Maven 3.6+
- Управление зависимостями
- Жизненный цикл сборки проекта
- Плагины для тестирования и packaging

Lombok
- Генерация boilerplate кода (@Data, @Getter, @Setter)
- @Slf4j для логирования
- @Builder для паттерна Builder
- Уменьшение размера кода на 30-40%

SLF4J + Logback
- Фасад для логирования (SLF4J)
- Реализация логирования (Logback)
- Конфигурация через logback.xml
- Уровни: TRACE, DEBUG, INFO, WARN, ERROR

Jakarta Validation
- Декларативная валидация (@NotNull, @Size, @Email)
- Валидация на уровне контроллера
- Кастомные валидаторы
- Автоматические сообщения об ошибках

3.5 ХРАНИЛИЩЕ ФАЙЛОВ
--------------------
Локальная файловая система
Директория: uploads/images/
Формат имен: {userId}_{type}_{uuid}.{extension}
Поддержка форматов: JPG, JPEG, PNG, GIF, WEBP
Максимальный размер: 10 MB

Альтернативы для Production:
- Amazon S3 (рекомендуется)
- Google Cloud Storage
- Azure Blob Storage
- MinIO (self-hosted S3-compatible)


================================================================================
                        4. АРХИТЕКТУРА ПРОЕКТА
================================================================================

4.1 МНОГОСЛОЙНАЯ АРХИТЕКТУРА (LAYERED ARCHITECTURE)
----------------------------------------------------

Проект следует классической многослойной архитектуре с четким разделением
ответственности между слоями:

┌─────────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                            │
│  (REST Controllers - обработка HTTP запросов)                   │
│                                                                   │
│  - AuthController      : Регистрация, логин                      │
│  - PostController      : CRUD операции с постами                 │
│  - FriendshipController: Управление друзьями                     │
│  - FileController      : Раздача статических файлов              │
└─────────────────────────────────────────────────────────────────┘
                              ↓ ↑
                      (DTOs, HTTP Status)
                              ↓ ↑
┌─────────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER                               │
│  (Бизнес-логика приложения)                                     │
│                                                                   │
│  - AuthService           : Аутентификация пользователей          │
│  - PostService           : Управление постами                    │
│  - FriendshipService     : Логика дружбы                         │
│  - BeRealTimeService     : Генерация случайного времени          │
│  - ImageService          : Загрузка/удаление изображений         │
│  - PostVisibilityChecker : Проверка видимости постов             │
└─────────────────────────────────────────────────────────────────┘
                              ↓ ↑
                         (Entities)
                              ↓ ↑
┌─────────────────────────────────────────────────────────────────┐
│                    REPOSITORY LAYER                              │
│  (Доступ к данным через JPA)                                    │
│                                                                   │
│  - UserRepository        : CRUD для пользователей                │
│  - PostRepository        : CRUD для постов                       │
│  - FriendshipRepository  : CRUD для дружбы                       │
│  - BeRealTimeRepository  : CRUD для времени BeReal               │
└─────────────────────────────────────────────────────────────────┘
                              ↓ ↑
                         (SQL Queries)
                              ↓ ↑
┌─────────────────────────────────────────────────────────────────┐
│                         DATABASE                                 │
│  (PostgreSQL - Persistent Storage)                              │
│                                                                   │
│  Tables: users, posts, friendships, bereal_times                │
└─────────────────────────────────────────────────────────────────┘

ДОПОЛНИТЕЛЬНЫЕ КОМПОНЕНТЫ:

┌─────────────────────────────────────────────────────────────────┐
│                    SECURITY LAYER                                │
│  (Фильтры безопасности и JWT)                                   │
│                                                                   │
│  - JwtAuthenticationFilter : Проверка токенов                    │
│  - JwtUtil                 : Генерация/валидация JWT             │
│  - SecurityConfig          : Конфигурация Spring Security        │
│  - CustomUserDetailsService: Загрузка пользователя               │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                 EXCEPTION HANDLING LAYER                         │
│  (Централизованная обработка ошибок)                            │
│                                                                   │
│  - GlobalExceptionHandler   : @RestControllerAdvice              │
│  - Custom Exceptions        : Специфичные исключения             │
│  - Error DTOs               : Структурированные ответы           │
└─────────────────────────────────────────────────────────────────┘

4.2 ПРИНЦИПЫ ПРОЕКТИРОВАНИЯ
----------------------------

SOLID PRINCIPLES:

S - Single Responsibility Principle
Каждый класс имеет одну ответственность:
- Controllers: только обработка HTTP
- Services: только бизнес-логика
- Repositories: только доступ к данным

O - Open/Closed Principle
Классы открыты для расширения, закрыты для модификации:
- Использование интерфейсов (JpaRepository)
- Стратегия для visibility checking

L - Liskov Substitution Principle
Наследуемые классы не нарушают контракт родителя:
- Все репозитории наследуют JpaRepository
- Кастомные исключения наследуют RuntimeException

I - Interface Segregation Principle
Клиенты не зависят от неиспользуемых методов:
- Разделение на специфичные репозитории
- Минимальные интерфейсы для сервисов

D - Dependency Inversion Principle
Зависимость от абстракций, а не реализаций:
- Внедрение зависимостей через конструктор
- Использование интерфейсов репозиториев

DRY (Don't Repeat Yourself):
- Переиспользуемые компоненты (PostVisibilityChecker)
- Mappers для конвертации Entity ↔ DTO
- Общий GlobalExceptionHandler

KISS (Keep It Simple, Stupid):
- Простые и понятные имена классов/методов
- Избегание преждевременной оптимизации
- Читаемый код без излишней сложности

4.3 ПАТТЕРНЫ ПРОЕКТИРОВАНИЯ
----------------------------

DTO (Data Transfer Object):
Используется для передачи данных между слоями
Примеры: PostDTO, AuthRequest, AuthResponse

Repository Pattern:
Абстракция доступа к данным
Интерфейсы: UserRepository, PostRepository

Service Layer Pattern:
Инкапсуляция бизнес-логики
Примеры: PostService, AuthService

Mapper Pattern:
Конвертация между Entity и DTO
Примеры: PostMapper, FriendshipMapper

Dependency Injection:
Внедрение зависимостей через конструктор
Все сервисы используют constructor injection

Factory Method:
Создание объектов через статические методы
Пример: ResponseEntity.ok(), ResponseEntity.notFound()

Strategy Pattern:
Различные стратегии проверки видимости постов
Реализация: PostVisibilityChecker

Filter Chain Pattern:
Цепочка фильтров безопасности
Реализация: JwtAuthenticationFilter

4.4 ПОТОК ОБРАБОТКИ ЗАПРОСА
----------------------------

ПРИМЕР: Создание поста

1. HTTP Request приходит на контроллер
   POST /api/posts
   Headers: Authorization: Bearer <token>
   Body: multipart/form-data (images + metadata)

2. JwtAuthenticationFilter перехватывает запрос
   - Извлекает JWT токен из заголовка
   - Валидирует токен (подпись, срок действия)
   - Устанавливает Authentication в SecurityContext

3. PostController получает запрос
   - Извлекает userId из JWT токена
   - Валидирует входные данные (@Valid)
   - Вызывает ImageService для загрузки файлов
   - Вызывает PostService для создания поста

4. ImageService обрабатывает изображения
   - Валидирует тип файла и размер
   - Генерирует уникальное имя файла
   - Сохраняет файл в uploads/images/
   - Возвращает URL для доступа

5. PostService выполняет бизнес-логику
   - Проверяет, не постил ли пользователь сегодня
   - Получает время BeReal на сегодня
   - Определяет, опоздал ли пользователь
   - Создает объект Post

6. PostRepository сохраняет в БД
   - JPA генерирует SQL INSERT
   - Hibernate выполняет запрос
   - Возвращает сохраненную сущность с ID

7. PostMapper конвертирует Entity → DTO
   - Преобразует внутреннюю модель в DTO
   - Скрывает служебную информацию

8. PostController возвращает ответ
   - HTTP 201 Created
   - Body: PostDTO в формате JSON
   - Headers: Content-Type: application/json

9. Клиент получает ответ
   {
     "id": 1,
     "userId": 2,
     "primaryImageUrl": "http://localhost:8080/images/...",
     "isLate": false,
     ...
   }

В случае ошибки:
- Любое исключение перехватывается GlobalExceptionHandler
- Возвращается структурированный ErrorResponse
- HTTP статус соответствует типу ошибки (400, 403, 404, 500)


================================================================================
                        5. СТРУКТУРА ПРОЕКТА
================================================================================

ПОЛНОЕ ДЕРЕВО ПРОЕКТА:

bereal/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/
│   │   │       └── example/
│   │   │           └── bereal/
│   │   │               │
│   │   │               ├── config/
│   │   │               │   └── FileStorageConfig.java
│   │   │               │
│   │   │               ├── controller/
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── FileController.java
│   │   │               │   ├── FriendshipController.java
│   │   │               │   └── PostController.java
│   │   │               │
│   │   │               ├── dto/
│   │   │               │   ├── AuthRequest.java
│   │   │               │   ├── AuthResponse.java
│   │   │               │   ├── FriendshipDTO.java
│   │   │               │   ├── PostDTO.java
│   │   │               │   └── RegisterRequest.java
│   │   │               │
│   │   │               ├── exception/
│   │   │               │   ├── ErrorResponse.java
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── ImageUploadException.java
│   │   │               │   ├── PostNotFoundException.java
│   │   │               │   ├── UnauthorizedException.java
│   │   │               │   ├── UserAlreadyPostedException.java
│   │   │               │   └── ValidationErrorResponse.java
│   │   │               │
│   │   │               ├── mapper/
│   │   │               │   ├── FriendshipMapper.java
│   │   │               │   └── PostMapper.java
│   │   │               │
│   │   │               ├── model/
│   │   │               │   ├── BeRealTime.java
│   │   │               │   ├── Friendship.java
│   │   │               │   ├── Post.java
│   │   │               │   └── User.java
│   │   │               │
│   │   │               ├── repository/
│   │   │               │   ├── BeRealTimeRepository.java
│   │   │               │   ├── FriendshipRepository.java
│   │   │               │   ├── PostRepository.java
│   │   │               │   └── UserRepository.java
│   │   │               │
│   │   │               ├── security/
│   │   │               │   ├── CustomUserDetailsService.java
│   │   │               │   ├── JwtAuthenticationFilter.java
│   │   │               │   ├── JwtUtil.java
│   │   │               │   └── SecurityConfig.java
│   │   │               │
│   │   │               ├── service/
│   │   │               │   ├── AuthService.java
│   │   │               │   ├── BeRealTimeService.java
│   │   │               │   ├── FriendshipService.java
│   │   │               │   ├── ImageService.java
│   │   │               │   ├── PostService.java
│   │   │               │   └── PostVisibilityChecker.java
│   │   │               │
│   │   │               └── BeRealApplication.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── logback.xml (опционально)
│   │       └── banner.txt (опционально)
│   │
│   └── test/
│       └── java/
│           └── org/
│               └── example/
│                   └── bereal/
│                       ├── controller/
│                       ├── service/
│                       └── repository/
│
├── uploads/
│   └── images/
│       └── (загруженные изображения пользователей)
│
├── .gitignore
├── pom.xml
└── README.md

ДЕТАЛЬНОЕ ОПИСАНИЕ КАЖДОГО ПАКЕТА:

═══════════════════════════════════════════════════════════════════

📁 config/
═══════════════════════════════════════════════════════════════════

Назначение: Конфигурационные классы Spring

FileStorageConfig.java
----------------------
Функции:
- Создает директорию для хранения файлов при запуске
- Предоставляет путь к директории другим компонентам
- Использует @PostConstruct для инициализации

Аннотации:
@Configuration - помечает класс как конфигурационный
@Value - инжектит значения из application.properties

Методы:
- init(): создает папку uploads/images/ если не существует
- getUploadDir(): возвращает путь к директории

═══════════════════════════════════════════════════════════════════

📁 controller/
═══════════════════════════════════════════════════════════════════

Назначение: REST контроллеры для обработки HTTP запросов

AuthController.java
-------------------
Эндпоинты:
POST /api/auth/register - Регистрация нового пользователя
POST /api/auth/login    - Авторизация существующего пользователя

Функции:
- Принимает JSON с данными пользователя
- Валидирует входные данные через @Valid
- Вызывает AuthService для обработки
- Возвращает JWT токен в ответе

Обработка ошибок:
- 400 Bad Request: невалидные данные
- 409 Conflict: username/email уже существует
- 401 Unauthorized: неверные credentials

PostController.java
-------------------
Эндпоинты:
POST   /api/posts              - Создать пост
GET    /api/posts              - Получить посты (пагинация)
GET    /api/posts/{id}         - Получить пост по ID
GET    /api/posts/today        - Посты за сегодня
GET    /api/posts/friends      - Посты друзей
GET    /api/posts/user/{userId}- Посты пользователя
PUT    /api/posts/{id}         - Обновить пост
DELETE /api/posts/{id}         - Удалить пост

Функции:
- Извлекает userId из JWT токена
- Управляет загрузкой изображений
- Применяет фильтры видимости
- Поддерживает пагинацию и сортировку

Параметры пагинации:
- page: номер страницы (default: 0)
- size: размер страницы (default: 20)
- sortBy: поле сортировки (default: postedAt)
- sortDir: направление (asc/desc, default: desc)

FriendshipController.java
--------------------------
Эндпоинты:
POST   /api/friends/request/{friendId}     - Отправить запрос
POST   /api/friends/accept/{requesterId}   - Принять запрос
POST   /api/friends/reject/{requesterId}   - Отклонить запрос
DELETE /api/friends/{friendId}              - Удалить из друзей
GET    /api/friends                         - Список друзей
GET    /api/friends/requests/pending        - Входящие запросы
GET    /api/friends/requests/sent           - Исходящие запросы

Функции:
- Управляет жизненным циклом дружбы
- Проверяет права доступа
- Предотвращает дублирование запросов

FileController.java
-------------------
Эндпоинты:
GET /images/{filename} - Получить изображение

Функции:
- Раздает статические файлы (изображения)
- Определяет MIME-тип по расширению
- Устанавливает правильные HTTP заголовки
- Защита от path traversal атак

Поддерживаемые форматы:
- image/jpeg (.jpg, .jpeg)
- image/png  (.png)
- image/gif  (.gif)
- image/webp (.webp)

═══════════════════════════════════════════════════════════════════

📁 dto/
═══════════════════════════════════════════════════════════════════

Назначение: Data Transfer Objects для передачи данных

AuthRequest.java
----------------
Поля:
- username: String (required, not blank)
- password: String (required, not blank)

Использование: Логин пользователя

AuthResponse.java
-----------------
Поля:
- token: String (JWT токен)
- userId: Long
- username: String
- email: String

Использование: Ответ после успешной аутентификации

RegisterRequest.java
--------------------
Поля:
- username: String (required, 3-20 символов)
- email: String (required, valid email)
- password: String (required, 6-40 символов)
- fullName: String (optional)

Валидация:
@NotBlank - поле не должно быть пустым
@Size - ограничение длины
@Email - валидация email формата

PostDTO.java
------------
Поля:
- id: Long
- userId: Long
- primaryImageUrl: String
- secondaryImageUrl: String
- postedAt: LocalDateTime
- isLate: boolean
- caption: String (max 200 символов)
- visibility: String (PUBLIC/FRIENDS_ONLY/PRIVATE)

Использование: Передача данных поста между слоями

FriendshipDTO.java
------------------
Поля:
- id: Long
- userId: Long
- friendId: Long
- status: String (PENDING/ACCEPTED/REJECTED/BLOCKED)
- createdAt: LocalDateTime
- acceptedAt: LocalDateTime (nullable)

Использование: Информация о дружбе

ErrorResponse.java
------------------
Поля:
- timestamp: LocalDateTime
- status: int (HTTP код)
- error: String (название ошибки)
- message: String (описание)

Использование: Стандартизированный ответ при ошибках

ValidationErrorResponse.java
----------------------------
Поля:
- timestamp: LocalDateTime
- status: int (400)
- error: String ("Validation Failed")
- validationErrors: Map<String, String> (поле → сообщение)

Использование: Ответ при ошибках валидации

═══════════════════════════════════════════════════════════════════

📁 exception/
═══════════════════════════════════════════════════════════════════

Назначение: Кастомные исключения и их обработка

GlobalExceptionHandler.java
----------------------------
Аннотация: @RestControllerAdvice

Функции:
- Централизованная обработка всех исключений
- Логирование ошибок
- Возврат структурированных ответов
- Правильные HTTP статус коды

Обрабатываемые исключения:

@ExceptionHandler(IllegalArgumentException.class)
→ 400 Bad Request

@ExceptionHandler(PostNotFoundException.class)
→ 404 Not Found

@ExceptionHandler(UserAlreadyPostedException.class)
→ 409 Conflict

@ExceptionHandler(UnauthorizedException.class)
→ 403 Forbidden

@ExceptionHandler(ImageUploadException.class)
→ 500 Internal Server Error

@ExceptionHandler(AuthenticationException.class)
→ 401 Unauthorized

@ExceptionHandler(MethodArgumentNotValidException.class)
→ 400 Bad Request с деталями валидации

@ExceptionHandler(MaxUploadSizeExceededException.class)
→ 413 Payload Too Large

@ExceptionHandler(Exception.class)
→ 500 Internal Server Error (fallback)

Кастомные исключения:

PostNotFoundException
- Наследует: RuntimeException
- Когда: Пост не найден по ID
- Сообщение: "Post not found with id: {id}"

UserAlreadyPostedException
- Наследует: RuntimeException
- Когда: Попытка создать второй пост за день
- Сообщение: "You have already posted today"

UnauthorizedException
- Наследует: RuntimeException
- Когда: Попытка изменить чужой контент
- Сообщение: "You can only update your own posts"

ImageUploadException
- Наследует: RuntimeException
- Когда: Ошибка при загрузке файла
- Сообщение: "Failed to upload image: {details}"

═══════════════════════════════════════════════════════════════════

📁mapper/
═══════════════════════════════════════════════════════════════════

Назначение: Преобразование между Entity и DTO

PostMapper.java
---------------
Статические методы:

toDto(Post post) → PostDTO
Преобразует JPA Entity в DTO для отправки клиенту
Скрывает внутренние детали реализации
Конвертирует Enum в String (Visibility → "PUBLIC")

fromDto(PostDTO dto) → Post
Преобразует DTO в Entity для сохранения в БД
Устанавливает только изменяемые поля
Не включает id (генерируется БД)
Не включает postedAt (устанавливается @PrePersist)

Пример использования:
Post post = PostMapper.fromDto(postDTO);
PostDTO dto = PostMapper.toDto(post);

FriendshipMapper.java
---------------------
Статические методы:

toDto(Friendship friendship) → FriendshipDTO
Преобразует Friendship Entity в DTO
Конвертирует FriendshipStatus Enum в String

Пример:
FriendshipDTO dto = FriendshipMapper.toDto(friendship);

Преимущества использования Mappers:
- Разделение concerns (DTO для API, Entity для БД)
- Возможность добавить логику трансформации
- Скрытие внутренней структуры Entity
- Легкость изменения API без изменения БД

═══════════════════════════════════════════════════════════════════

📁 model/
═══════════════════════════════════════════════════════════════════

Назначение: JPA Entity классы (модели базы данных)

User.java
---------
Таблица: users

Поля:
@Id @GeneratedValue
- id: Long (PRIMARY KEY, AUTO_INCREMENT)

@Column(unique = true, nullable = false)
- username: String (уникальный логин)
- email: String (уникальный email)

@Column(nullable = false)
- password: String (BCrypt хеш пароля)
- createdAt: LocalDateTime (дата регистрации)

@Column
- fullName: String (полное имя, опционально)
- profileImageUrl: String (аватарка, опционально)

@ElementCollection(fetch = FetchType.EAGER)
@CollectionTable(name = "user_roles")
- roles: Set<String> (роли пользователя: USER, ADMIN)

Lifecycle hooks:
@PrePersist
- onCreate(): устанавливает createdAt и дефолтную роль USER

Индексы:
- UNIQUE INDEX на username
- UNIQUE INDEX на email

Связи:
- One-to-Many с Post (не маппится напрямую)
- Many-to-Many с User через Friendship

Post.java
---------
Таблица: posts

Поля:
@Id @GeneratedValue
- id: Long (PRIMARY KEY, AUTO_INCREMENT)

@Column(nullable = false)
- userId: Long (FOREIGN KEY к users.id)
- visibility: Visibility (ENUM: PUBLIC, FRIENDS_ONLY, PRIVATE)

@Column(nullable = false, updatable = false)
- postedAt: LocalDateTime (время создания, неизменяемое)

@Column
- primaryImageUrl: String (URL передней камеры)
- secondaryImageUrl: String (URL задней камеры)
- caption: String (подпись до 200 символов)
- isLate: boolean (опоздал ли пользователь)

Inner Enum:
Visibility {
    PUBLIC,        // Виден всем
    FRIENDS_ONLY,  // Только друзья
    PRIVATE        // Только владелец
}

Lifecycle hooks:
@PrePersist
- onCreate(): устанавливает postedAt = LocalDateTime.now()

Индексы:
- INDEX на userId (для быстрого поиска постов пользователя)
- INDEX на postedAt (для сортировки по времени)
- COMPOSITE INDEX на (userId, postedAt) для запросов "посты пользователя за день"

Валидация на уровне БД:
- NOT NULL: userId, postedAt, visibility
- CHECK: visibility IN ('PUBLIC', 'FRIENDS_ONLY', 'PRIVATE')

Friendship.java
---------------
Таблица: friendships

Поля:
@Id @GeneratedValue
- id: Long (PRIMARY KEY, AUTO_INCREMENT)

@Column(nullable = false)
- userId: Long (инициатор дружбы)
- friendId: Long (получатель запроса)
- status: FriendshipStatus (статус дружбы)
- createdAt: LocalDateTime (время создания запроса)

@Column
- acceptedAt: LocalDateTime (время принятия, nullable)

Inner Enum:
FriendshipStatus {
    PENDING,   // Запрос отправлен
    ACCEPTED,  // Запрос принят (друзья)
    REJECTED,  // Запрос отклонен
    BLOCKED    // Пользователь заблокирован
}

Lifecycle hooks:
@PrePersist
- onCreate(): устанавливает createdAt

Constraints:
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "friend_id"})
})
Гарантирует отсутствие дублирующих запросов

Индексы:
- UNIQUE INDEX на (userId, friendId)
- INDEX на friendId (для поиска входящих запросов)
- INDEX на status (для фильтрации по статусу)

Бизнес-правила:
- userId != friendId (нельзя добавить себя в друзья)
- Один запрос между двумя пользователями
- Двусторонняя проверка (A→B и B→A это разные записи)

BeRealTime.java
---------------
Таблица: bereal_times

Поля:
@Id @GeneratedValue
- id: Long (PRIMARY KEY, AUTO_INCREMENT)

@Column(nullable = false, unique = true)
- notificationTime: LocalDateTime (случайное время BeReal)

@Column(nullable = false)
- sent: boolean (отправлено ли уведомление, default: false)

Индексы:
- UNIQUE INDEX на notificationTime
- INDEX на (notificationTime, sent) для scheduler

Использование:
Хранит сгенерированное время BeReal для каждого дня
Scheduler ищет записи где sent = false и время наступило
После отправки уведомления устанавливает sent = true

Жизненный цикл:
1. В 00:01 создается запись на сегодня (sent = false)
2. Когда наступает notificationTime, отправляются уведомления
3. Устанавливается sent = true
4. Запись хранится для истории

═══════════════════════════════════════════════════════════════════

📁 repository/
═══════════════════════════════════════════════════════════════════

Назначение: Интерфейсы для работы с базой данных

Все репозитории наследуют JpaRepository<Entity, ID>

UserRepository.java
-------------------
Интерфейс: extends JpaRepository<User, Long>

Кастомные методы:

Optional<User> findByUsername(String username)
- Поиск пользователя по username
- Использование: аутентификация, проверка уникальности

Optional<User> findByEmail(String email)
- Поиск пользователя по email
- Использование: проверка уникальности при регистрации

boolean existsByUsername(String username)
- Проверка существования username
- Оптимизированный запрос (SELECT EXISTS)

boolean existsByEmail(String email)
- Проверка существования email
- Оптимизированный запрос

Генерируемые Spring Data JPA запросы:
SELECT * FROM users WHERE username = ?
SELECT * FROM users WHERE email = ?
SELECT EXISTS(SELECT 1 FROM users WHERE username = ?)

PostRepository.java
-------------------
Интерфейс: extends JpaRepository<Post, Long>

Кастомные методы:

List<Post> findByUserIdAndPostedAtBetween(
    Long userId, 
    LocalDateTime start, 
    LocalDateTime end
)
- Поиск постов пользователя за период
- Использование: проверка "один пост в день"
- SQL: WHERE user_id = ? AND posted_at BETWEEN ? AND ?

List<Post> findByPostedAtBetween(
    LocalDateTime start, 
    LocalDateTime end
)
- Все посты за период
- Использование: лента "сегодня"

@Query аннотации (кастомные запросы):

@Query("SELECT p FROM Post p WHERE p.visibility = 'PUBLIC' ORDER BY p.postedAt DESC")
Page<Post> findPublicPosts(Pageable pageable)
- Только публичные посты с пагинацией

@Query("SELECT p FROM Post p WHERE p.userId = :userId OR p.visibility = 'PUBLIC'")
Page<Post> findUserAndPublicPosts(@Param("userId") Long userId, Pageable pageable)
- Свои посты + публичные

@Query("SELECT p FROM Post p WHERE " +
       "p.userId = :userId OR " +
       "p.visibility = 'PUBLIC' OR " +
       "(p.visibility = 'FRIENDS_ONLY' AND p.userId IN :friendIds)")
Page<Post> findVisiblePosts(
    @Param("userId") Long userId, 
    @Param("friendIds") List<Long> friendIds, 
    Pageable pageable
)
- Комплексный запрос видимых постов
- Свои + публичные + посты друзей

FriendshipRepository.java
--------------------------
Интерфейс: extends JpaRepository<Friendship, Long>

Кастомные методы:

Optional<Friendship> findByUserIdAndFriendId(Long userId, Long friendId)
- Поиск конкретной связи дружбы
- Использование: проверка существующих запросов

List<Friendship> findByUserIdAndStatus(Long userId, FriendshipStatus status)
- Исходящие запросы пользователя с определенным статусом
- Использование: список отправленных запросов

List<Friendship> findByFriendIdAndStatus(Long friendId, FriendshipStatus status)
- Входящие запросы к пользователю
- Использование: список ожидающих подтверждения

@Query("SELECT f FROM Friendship f WHERE " +
       "(f.userId = :userId OR f.friendId = :userId) AND f.status = :status")
List<Friendship> findAllFriendships(
    @Param("userId") Long userId, 
    @Param("status") FriendshipStatus status
)
- Все дружеские связи пользователя (в обе стороны)
- Использование: получение списка друзей

@Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
       "FROM Friendship f WHERE " +
       "((f.userId = :userId AND f.friendId = :friendId) OR " +
       "(f.userId = :friendId AND f.friendId = :userId)) AND " +
       "f.status = 'ACCEPTED'")
boolean areFriends(
    @Param("userId") Long userId, 
    @Param("friendId") Long friendId
)
- Быстрая проверка: являются ли пользователи друзьями
- Оптимизированный запрос с COUNT
- Проверяет обе направления связи

BeRealTimeRepository.java
--------------------------
Интерфейс: extends JpaRepository<BeRealTime, Long>

Кастомные методы:

Optional<BeRealTime> findTopByNotificationTimeBetweenOrderByNotificationTimeDesc(
    LocalDateTime start, 
    LocalDateTime end
)
- Поиск последней записи BeReal времени за период
- Использование: получение времени BeReal на сегодня
- ORDER BY DESC + LIMIT 1

Примечание:
"Top" в Spring Data означает LIMIT 1
"Between" генерирует BETWEEN в SQL
"OrderBy...Desc" добавляет ORDER BY ... DESC

═══════════════════════════════════════════════════════════════════

📁 security/
═══════════════════════════════════════════════════════════════════

Назначение: Компоненты безопасности и JWT аутентификация

SecurityConfig.java
-------------------
Аннотация: @Configuration, @EnableWebSecurity, @EnableMethodSecurity

Главный Bean:
@Bean SecurityFilterChain securityFilterChain(HttpSecurity http)

Конфигурация:

1. CSRF Protection:
   .csrf(csrf -> csrf.disable())
   Отключен для stateless API (используем JWT)

2. Authorization Rules:
   .authorizeHttpRequests(auth -> auth
       .requestMatchers("/api/auth/**").permitAll()    // Регистрация/логин
       .requestMatchers("/images/**").permitAll()       // Статика
       .anyRequest().authenticated()                    // Все остальное
   )

3. Session Management:
   .sessionManagement(session -> 
       session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
   )
   STATELESS - не создаем HTTP сессии, только JWT

4. Filter Chain:
   .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
   JWT фильтр выполняется перед стандартным фильтром аутентификации

Дополнительные Beans:

@Bean PasswordEncoder passwordEncoder()
Возвращает: BCryptPasswordEncoder
Используется для хеширования паролей
Rounds: 10 (2^10 = 1024 итерации)

@Bean AuthenticationManager authenticationManager(AuthenticationConfiguration config)
Менеджер аутентификации для проверки credentials

CORS Configuration (если нужен):
@Bean CorsConfigurationSource corsConfigurationSource()
Настройка Cross-Origin Resource Sharing для frontend

JwtUtil.java
------------
Аннотация: @Component

Поля:
@Value("${jwt.secret}")
private String secret;  // Секретный ключ (минимум 256 бит)

@Value("${jwt.expiration:86400000}")
private Long expiration;  // Срок действия (24 часа по умолчанию)

Методы:

String generateToken(String username, Long userId)
Генерирует JWT токен
Claims:
- subject: username
- userId: кастомный claim с ID пользователя
- iat (issued at): время создания
- exp (expiration): время истечения

Алгоритм подписи: HS512 (HMAC-SHA512)
Структура токена: header.payload.signature

String extractUsername(String token)
Извлекает username из subject claim

Long extractUserId(String token)
Извлекает userId из кастомного claim

boolean isTokenValid(String token, String username)
Проверяет:
- Совпадает ли username
- Не истек ли срок действия
- Валидна ли подпись

private boolean isTokenExpired(String token)
Проверяет exp claim

private Claims extractAllClaims(String token)
Парсит токен и извлекает все claims
Проверяет подпись через signing key

private SecretKey getSigningKey()
Генерирует ключ подписи из secret строки
Использует HMAC-SHA512

Пример токена:
{
  "alg": "HS512",
  "typ": "JWT"
}
{
  "userId": 2,
  "sub": "john_doe",
  "iat": 1699564327,
  "exp": 1699650727
}
[signature]

JwtAuthenticationFilter.java
-----------------------------
Наследует: OncePerRequestFilter
Аннотация: @Component

Поля:
- JwtUtil jwtUtil
- UserDetailsService userDetailsService

Главный метод:
protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
)

Логика фильтра:

1. Извлечение токена из заголовка:
   String authHeader = request.getHeader("Authorization");
   Ожидаемый формат: "Bearer <token>"

2. Валидация формата:
   if (authHeader == null || !authHeader.startsWith("Bearer ")) {
       filterChain.doFilter(request, response);  // Пропускаем дальше
       return;
   }

3. Парсинг токена:
   String jwt = authHeader.substring(7);  // Убираем "Bearer "
   String username = jwtUtil.extractUsername(jwt);

4. Проверка аутентификации:
   if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)

5. Загрузка пользователя:
   UserDetails userDetails = userDetailsService.loadUserByUsername(username);

6. Валидация токена:
   if (jwtUtil.isTokenValid(jwt, username))

7. Установка аутентификации:
   UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
       userDetails,
       null,
       userDetails.getAuthorities()
   );
   authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
   SecurityContextHolder.getContext().setAuthentication(authToken);

8. Продолжение цепочки фильтров:
   filterChain.doFilter(request, response);

Порядок выполнения:
Request → JwtAuthenticationFilter → [другие фильтры] → Controller

CustomUserDetailsService.java
------------------------------
Имплементирует: UserDetailsService
Аннотация: @Service

Метод:
UserDetails loadUserByUsername(String username) throws UsernameNotFoundException

Логика:

1. Загрузка пользователя из БД:
   User user = userRepository.findByUsername(username)
       .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

2. Конвертация ролей:
   List<GrantedAuthority> authorities = user.getRoles().stream()
       .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
       .collect(Collectors.toList());

   Префикс "ROLE_" добавляется автоматически
   Пример: "USER" → "ROLE_USER"

3. Создание UserDetails:
   return new org.springframework.security.core.userdetails.User(
       user.getUsername(),
       user.getPassword(),  // BCrypt хеш
       authorities
   );

Использование:
Вызывается JwtAuthenticationFilter для загрузки деталей пользователя
Также используется AuthenticationManager при логине

Поток аутентификации:

POST /api/auth/login
→ AuthController
→ AuthenticationManager.authenticate()
→ CustomUserDetailsService.loadUserByUsername()
→ PasswordEncoder.matches() (проверка пароля)
→ JwtUtil.generateToken() (если успешно)
→ Возврат токена клиенту

Последующие запросы:

GET /api/posts
Header: Authorization: Bearer <token>
→ JwtAuthenticationFilter
→ JwtUtil.extractUsername()
→ CustomUserDetailsService.loadUserByUsername()
→ JwtUtil.isTokenValid()
→ SecurityContext.setAuthentication()
→ PostController (authenticated request)

═══════════════════════════════════════════════════════════════════

📁 service/
═══════════════════════════════════════════════════════════════════

Назначение: Бизнес-логика приложения

AuthService.java
----------------
Аннотация: @Service

Зависимости:
- UserRepository userRepository
- PasswordEncoder passwordEncoder
- JwtUtil jwtUtil
- AuthenticationManager authenticationManager

Методы:

AuthResponse register(RegisterRequest request)
Регистрация нового пользователя

Логика:
1. Проверка уникальности username:
   if (userRepository.existsByUsername(request.username())) {
       throw new IllegalArgumentException("Username already exists");
   }

2. Проверка уникальности email:
   if (userRepository.existsByEmail(request.email())) {
       throw new IllegalArgumentException("Email already exists");
   }

3. Создание пользователя:
   User user = new User();
   user.setUsername(request.username());
   user.setEmail(request.email());
   user.setPassword(passwordEncoder.encode(request.password()));  // Хеширование!
   user.setFullName(request.fullName());

4. Сохранение в БД:
   User saved = userRepository.save(user);

5. Генерация JWT токена:
   String token = jwtUtil.generateToken(saved.getUsername(), saved.getId());

6. Возврат ответа:
   return new AuthResponse(token, saved.getId(), saved.getUsername(), saved.getEmail());

AuthResponse login(AuthRequest request)
Авторизация существующего пользователя

Логика:
1. Аутентификация через Spring Security:
   authenticationManager.authenticate(
       new UsernamePasswordAuthenticationToken(
           request.username(), 
           request.password()
       )
   );
   
   Если credentials неверны → BadCredentialsException

2. Загрузка пользователя:
   User user = userRepository.findByUsername(request.username())
       .orElseThrow(() -> new IllegalArgumentException("User not found"));

3. Генерация токена:
   String token = jwtUtil.generateToken(user.getUsername(), user.getId());

4. Возврат ответа:
   return new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail());

Безопасность:
- Пароли никогда не хранятся в открытом виде
- BCrypt автоматически добавляет salt
- Проверка паролей через PasswordEncoder.matches()
- JWT токены подписаны и защищены от подделки

PostService.java
----------------
Аннотация: @Service

Зависимости:
- PostRepository postRepository
- BeRealTimeService beRealTimeService
- PostVisibilityChecker visibilityChecker

Методы:

Post createPost(Post post)
Создание нового поста

Бизнес-правила:
1. Валидация userId:
   if (post.getUserId() == null) {
       throw new IllegalArgumentException("User ID cannot be null");
   }

2. Проверка "один пост в день":
   if (hasUserPostedToday(post.getUserId())) {
       throw new UserAlreadyPostedException("You have already posted today");
   }

3. Определение опоздания:
   LocalDateTime todayBeRealTime = beRealTimeService.getTodayBeRealTime();
   LocalDateTime now = LocalDateTime.now();
   post.setLate(now.isAfter(todayBeRealTime.plusMinutes(2)));

4. Сохранение:
   return postRepository.save(post);

Логирование:
log.info("Creating post for user: {}", post.getUserId());
log.info("Post late status: {} (BeReal time: {}, posted at: {})", ...);

boolean hasUserPostedToday(Long userId)
Проверка наличия поста за сегодня

Логика:
LocalDate today = LocalDate.now();
LocalDateTime start = today.atStartOfDay();  // 00:00:00
LocalDateTime end = today.atTime(LocalTime.MAX);  // 23:59:59.999999999

List<Post> todayPosts = postRepository.findByUserIdAndPostedAtBetween(
    userId, start, end
);
return !todayPosts.isEmpty();

Optional<Post> getVisiblePostById(Long id, Long currentUserId)
Получение поста с проверкой видимости

Логика:
return postRepository.findById(id)
    .filter(post -> visibilityChecker.isPostVisibleToUser(post, currentUserId));

Возвращает empty Optional если пост не виден

Page<Post> getVisiblePostsPaginated(Long currentUserId, Pageable pageable)
Получение постов с пагинацией и фильтрацией

Логика:
Page<Post> allPosts = postRepository.findAll(pageable);
return visibilityChecker.filterVisiblePosts(allPosts, currentUserId);

Пагинация:
- pageNumber: номер страницы (0-based)
- pageSize: размер страницы
- sort: сортировка (обычно по postedAt DESC)

Post updatePost(Long id, Post newPost, Long currentUserId)
Обновление существующего поста

Проверки:
1. Существование поста:
   Post existing = postRepository.findById(id)
       .orElseThrow(() -> new PostNotFoundException(id));

2. Права доступа:
   if (!existing.getUserId().equals(currentUserId)) {
       throw new UnauthorizedException("You can only update your own posts");
   }

3. Обновление полей:
   if (newPost.getCaption() != null) {
       existing.setCaption(newPost.getCaption());
   }
   if (newPost.getVisibility() != null) {
       existing.setVisibility(newPost.getVisibility());
   }

Примечание: нельзя изменить изображения, userId, postedAt

void deletePost(Long id, Long currentUserId)
Удаление поста

Проверки:
1. Существование:
   Post post = postRepository.findById(id)
       .orElseThrow(() -> new PostNotFoundException(id));

2. Права доступа:
   if (!post.getUserId().equals(currentUserId)) {
       throw new UnauthorizedException("You can only delete your own posts");
   }

3. Удаление:
   postRepository.deleteById(id);

Примечание: CASCADE удаление не настроено, нужно вручную удалять связанные данные

List<Post> getFriendsPosts(Long currentUserId)
Получение постов друзей

Логика:
List<Long> friendIds = visibilityChecker.getFriendIds(currentUserId);
return postRepository.findAll().stream()
    .filter(post -> friendIds.contains(post.getUserId()))
    .toList();

Оптимизация: можно добавить кастомный query в repository

FriendshipService.java
----------------------
Аннотация: @Service

Зависимости:
- FriendshipRepository friendshipRepository
- UserRepository userRepository

Методы:

Friendship sendFriendRequest(Long userId, Long friendId)
Отправка запроса в друзья

Валидации:
1. Проверка самого себя:
   if (userId.equals(friendId)) {
       throw new IllegalArgumentException("Cannot send friend request to yourself");
   }

2. Существование пользователя:
   if (!userRepository.existsById(friendId)) {
       throw new IllegalArgumentException("User not found");
   }

3. Отсутствие существующего запроса:
   if (friendshipRepository.findByUserIdAndFriendId(userId, friendId).isPresent()) {
       throw new IllegalArgumentException("Friend request already exists");
   }

4. Проверка обратного запроса:
   if (friendshipRepository.findByUserIdAndFriendId(friendId, userId).isPresent()) {
       throw new IllegalArgumentException("This user has already sent you a request");
   }

Создание:
Friendship friendship = new Friendship();
friendship.setUserId(userId);
friendship.setFriendId(friendId);
friendship.setStatus(FriendshipStatus.PENDING);
return friendshipRepository.save(friendship);

Friendship acceptFriendRequest(Long userId, Long requesterId)
Принятие запроса в друзья

Логика:
1. Поиск запроса:
   Friendship friendship = friendshipRepository
       .findByUserIdAndFriendId(requesterId, userId)
       .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

2. Проверка статуса:
   if (friendship.getStatus() != FriendshipStatus.PENDING) {
       throw new IllegalArgumentException("Request is not pending");
   }

3. Обновление:
   friendship.setStatus(FriendshipStatus.ACCEPTED);
   friendship.setAcceptedAt(LocalDateTime.now());
   return friendshipRepository.save(friendship);

Примечание: userId - это получатель (кто принимает), requesterId - отправитель

void rejectFriendRequest(Long userId, Long requesterId)
Отклонение запроса

Логика аналогична accept, но:
friendship.setStatus(FriendshipStatus.REJECTED);
acceptedAt остается null

void removeFriend(Long userId, Long friendId)
Удаление из друзей

Логика:
1. Поиск в обе стороны:
   Friendship friendship = friendshipRepository
       .findByUserIdAndFriendId(userId, friendId)
       .or(() -> friendshipRepository.findByUserIdAndFriendId(friendId, userId))
       .orElseThrow(() -> new IllegalArgumentException("Friendship not found"));

2. Проверка прав:
   if (!friendship.getUserId().equals(userId) && 
       !friendship.getFriendId().equals(userId)) {
       throw new UnauthorizedException("You cannot remove this friendship");
   }

3. Удаление:
   friendshipRepository.delete(friendship);

List<Long> getFriendIds(Long userId)
Получение списка ID друзей

Логика:
List<Friendship> friendships = friendshipRepository
    .findAllFriendships(userId, FriendshipStatus.ACCEPTED);

return friendships.stream()
    .map(f -> f.getUserId().equals(userId) ? f.getFriendId() : f.getUserId())
    .collect(Collectors.toList());

Пояснение: дружба может быть записана как (A, B) или (B, A), поэтому проверяем обе стороны

List<Friendship> getPendingRequests(Long userId)
Входящие запросы в друзья

return friendshipRepository.findByFriendIdAndStatus(
    userId, 
    FriendshipStatus.PENDING
);

Возвращает запросы, где userId - получатель

List<Friendship> getSentRequests(Long userId)
Исходящие запросы в друзья

return friendshipRepository.findByUserIdAndStatus(
    userId, 
    FriendshipStatus.PENDING
);

Возвращает запросы, где userId - отправитель

boolean areFriends(Long userId, Long friendId)
Быстрая проверка дружбы

return friendshipRepository.areFriends(userId, friendId);

Использует оптимизированный SQL запрос с COUNT

BeRealTimeService.java
----------------------
Аннотация: @Service

Зависимости:
- BeRealTimeRepository beRealTimeRepository
- Random random (для генерации случайного времени)

Методы:@Scheduled(cron = "0 1 0 * * *")
public void generateDailyBeRealTime()
Генерация случайного времени BeReal каждый день

Cron выражение: "0 1 0 * * *"
Расшифровка:
- 0  : секунда 0
- 1  : минута 1
- 0  : час 0 (полночь)
- *  : любой день месяца
- *  : любой месяц
- *  : любой день недели

Итого: выполняется каждый день в 00:01:00

Логика:
1. Получение сегодняшней даты:
   LocalDate today = LocalDate.now();

2. Генерация случайного времени:
   int hour = 9 + random.nextInt(14);   // 9-22 (9 + 0..13)
   int minute = random.nextInt(60);      // 0-59

   Диапазон: от 09:00 до 22:59

3. Создание LocalDateTime:
   LocalDateTime beRealTime = LocalDateTime.of(
       today, 
       LocalTime.of(hour, minute)
   );

4. Сохранение в БД:
   BeRealTime entity = new BeRealTime(beRealTime);
   beRealTimeRepository.save(entity);

5. Логирование:
   log.info("Generated BeReal time for today: {}", beRealTime);

Пример сгенерированных времен:
- 2024-11-10 14:23:00
- 2024-11-10 19:47:00
- 2024-11-10 10:05:00

LocalDateTime getTodayBeRealTime()
Получение времени BeReal на сегодня

Логика:
1. Определение границ дня:
   LocalDate today = LocalDate.now();
   LocalDateTime startOfDay = today.atStartOfDay();           // 00:00:00
   LocalDateTime endOfDay = today.atTime(LocalTime.MAX);      // 23:59:59.999...

2. Поиск в БД:
   return beRealTimeRepository
       .findTopByNotificationTimeBetweenOrderByNotificationTimeDesc(
           startOfDay, 
           endOfDay
       )
       .map(BeRealTime::getNotificationTime)
       .orElseGet(() -> {
           log.warn("No BeReal time found for today, generating now");
           generateDailyBeRealTime();
           return getTodayBeRealTime();  // Рекурсивный вызов
       });

Fallback механизм:
Если запись не найдена (например, scheduler не сработал), генерируется на лету

Использование:
PostService вызывает этот метод для определения isLate

ImageService.java
-----------------
Аннотация: @Service

Зависимости:
- FileStorageConfig fileStorageConfig
- Path fileStorageLocation (директория uploads/images/)

@Value("${server.port:8080}")
private String serverPort;

Методы:

String uploadImage(MultipartFile file, Long userId, String type)
Загрузка изображения на сервер

Параметры:
- file: MultipartFile (загружаемый файл)
- userId: Long (ID пользователя-владельца)
- type: String ("primary" или "secondary")

Логика:

1. Валидация файла:
   validateImage(file);
   
   Проверяет:
   - Файл не пустой
   - Content-Type начинается с "image/"
   - Размер < 10 MB
   - Расширение допустимо (.jpg, .jpeg, .png, .gif, .webp)

2. Генерация уникального имени:
   String originalFilename = file.getOriginalFilename();
   String fileExtension = getFileExtension(originalFilename);
   String newFileName = String.format(
       "%d_%s_%s%s", 
       userId,                      // 2
       type,                        // "primary"
       UUID.randomUUID().toString(), // "550e8400-e29b-41d4-a716..."
       fileExtension                // ".jpg"
   );
   
   Результат: "2_primary_550e8400-e29b-41d4-a716-446655440000.jpg"

3. Определение пути сохранения:
   Path targetLocation = fileStorageLocation.resolve(newFileName);
   
   Полный путь: /path/to/project/uploads/images/2_primary_550e8400...jpg

4. Копирование файла:
   Files.copy(
       file.getInputStream(), 
       targetLocation, 
       StandardCopyOption.REPLACE_EXISTING
   );

5. Генерация URL:
   String fileUrl = ServletUriComponentsBuilder
       .fromCurrentContextPath()
       .path("/images/")
       .path(newFileName)
       .toUriString();
   
   Результат: "http://localhost:8080/images/2_primary_550e8400...jpg"

6. Возврат URL:
   return fileUrl;

Обработка ошибок:
try-catch IOException → throw ImageUploadException

void deleteImage(String imageUrl)
Удаление изображения с сервера

Параметры:
- imageUrl: String (полный URL изображения)

Логика:

1. Извлечение имени файла из URL:
   String fileName = extractFileNameFromUrl(imageUrl);
   
   "http://localhost:8080/images/2_primary_550e8400...jpg"
   → "2_primary_550e8400...jpg"

2. Построение пути:
   Path filePath = fileStorageLocation.resolve(fileName).normalize();

3. Проверка безопасности (Path Traversal защита):
   if (!filePath.startsWith(fileStorageLocation)) {
       log.error("Attempted to delete file outside storage directory");
       return;
   }
   
   Предотвращает удаление файлов через "../../../etc/passwd"

4. Удаление файла:
   Files.deleteIfExists(filePath);

5. Логирование:
   log.info("Image deleted: {}", fileName);

Обработка ошибок:
Логирует ошибку, но не бросает исключение (graceful degradation)

private void validateImage(MultipartFile file)
Валидация загружаемого изображения

Проверки:

1. Пустой файл:
   if (file.isEmpty()) {
       throw new IllegalArgumentException("File is empty");
   }

2. MIME тип:
   String contentType = file.getContentType();
   if (contentType == null || !contentType.startsWith("image/")) {
       throw new IllegalArgumentException("File must be an image");
   }
   
   Допустимые: image/jpeg, image/png, image/gif, image/webp

3. Размер файла:
   if (file.getSize() > 10 * 1024 * 1024) {  // 10 MB
       throw new IllegalArgumentException("File size must be less than 10MB");
   }

4. Расширение файла:
   String filename = file.getOriginalFilename();
   if (!isValidImageExtension(filename)) {
       throw new IllegalArgumentException("Invalid image file extension");
   }

private boolean isValidImageExtension(String filename)
Проверка допустимого расширения

String extension = getFileExtension(filename).toLowerCase();
return extension.equals(".jpg") || 
       extension.equals(".jpeg") || 
       extension.equals(".png") || 
       extension.equals(".gif") || 
       extension.equals(".webp");

Список разрешенных форматов:
- JPG/JPEG: самый распространенный
- PNG: с прозрачностью
- GIF: анимация (обычно не используется в BeReal)
- WEBP: современный формат от Google

private String getFileExtension(String filename)
Извлечение расширения файла

if (filename == null) return "";
int lastDotIndex = filename.lastIndexOf('.');
return (lastDotIndex == -1) ? "" : filename.substring(lastDotIndex);

Примеры:
"photo.jpg" → ".jpg"
"image.test.png" → ".png"
"noextension" → ""

private String extractFileNameFromUrl(String imageUrl)
Извлечение имени файла из URL

Пример:
"http://localhost:8080/images/2_primary_550e8400...jpg"
→ "2_primary_550e8400...jpg"

Логика:
int lastSlashIndex = imageUrl.lastIndexOf('/');
if (lastSlashIndex != -1 && lastSlashIndex < imageUrl.length() - 1) {
    return imageUrl.substring(lastSlashIndex + 1);
}
return null;

PostVisibilityChecker.java
--------------------------
Аннотация: @Component

Зависимости:
- FriendshipRepository friendshipRepository

Назначение:
Отдельный компонент для проверки видимости постов
Избегает циклических зависимостей между PostService и FriendshipService

Методы:

boolean isPostVisibleToUser(Post post, Long currentUserId)
Проверяет, виден ли пост конкретному пользователю

Логика:

1. Проверка владельца:
   if (post.getUserId().equals(currentUserId)) {
       return true;
   }
   
   Свои посты всегда видны

2. Проверка по visibility:
   return switch (post.getVisibility()) {
       case PUBLIC -> true;
       case FRIENDS_ONLY -> areFriends(currentUserId, post.getUserId());
       case PRIVATE -> false;
   };

Таблица видимости:
┌───────────────┬─────────┬──────────────┬─────────┐
│ Visibility    │ Owner   │ Friends      │ Others  │
├───────────────┼─────────┼──────────────┼─────────┤
│ PUBLIC        │ ✅      │ ✅           │ ✅      │
│ FRIENDS_ONLY  │ ✅      │ ✅           │ ❌      │
│ PRIVATE       │ ✅      │ ❌           │ ❌      │
└───────────────┴─────────┴──────────────┴─────────┘

List<Post> filterVisiblePosts(List<Post> posts, Long currentUserId)
Фильтрует список постов

return posts.stream()
    .filter(post -> isPostVisibleToUser(post, currentUserId))
    .collect(Collectors.toList());

Использование: для коллекций постов без пагинации

Page<Post> filterVisiblePosts(Page<Post> postsPage, Long currentUserId)
Фильтрует Page постов

List<Post> visiblePosts = postsPage.getContent().stream()
    .filter(post -> isPostVisibleToUser(post, currentUserId))
    .collect(Collectors.toList());

return new PageImpl<>(
    visiblePosts, 
    postsPage.getPageable(), 
    postsPage.getTotalElements()
);

Примечание: totalElements остается прежним (можно оптимизировать)

List<Long> getFriendIds(Long userId)
Получает список ID друзей

List<Friendship> friendships = friendshipRepository
    .findAllFriendships(userId, Friendship.FriendshipStatus.ACCEPTED);

return friendships.stream()
    .map(f -> f.getUserId().equals(userId) ? f.getFriendId() : f.getUserId())
    .collect(Collectors.toList());

Дублирует логику FriendshipService для избежания циклической зависимости

boolean areFriends(Long userId, Long friendId)
Проверяет дружбу между двумя пользователями

return friendshipRepository.areFriends(userId, friendId);

Использует оптимизированный query из repository

Преимущества отдельного компонента:
1. Нет циклических зависимостей
2. Единое место логики видимости
3. Легко тестировать
4. Можно переиспользовать в других сервисах

═══════════════════════════════════════════════════════════════════

📁 BeRealApplication.java
═══════════════════════════════════════════════════════════════════

Главный класс приложения

Аннотации:
@SpringBootApplication
Комбинированная аннотация включающая:
- @Configuration: класс содержит Spring beans
- @EnableAutoConfiguration: автоматическая конфигурация
- @ComponentScan: сканирование пакета для компонентов

@EnableScheduling
Включает поддержку @Scheduled методов
Необходимо для BeRealTimeService.generateDailyBeRealTime()

Код:
package org.example.bereal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BeRealApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(BeRealApplication.class, args);
    }
}

Что происходит при запуске:

1. SpringApplication.run() запускается
2. Сканирование всех @Component, @Service, @Repository, @Controller
3. Создание ApplicationContext
4. Инициализация Beans (DI)
5. Автоконфигурация (DataSource, JPA, Security)
6. Запуск встроенного Tomcat на порту 8080
7. Выполнение @PostConstruct методов
8. Запуск @Scheduled задач
9. Приложение готово к приему запросов

Логирование при старте:
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

... INFO  BeRealApplication : Starting BeRealApplication
... INFO  BeRealApplication : No active profile set, falling back to default
... INFO  o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat initialized with port(s): 8080
... INFO  BeRealApplication : Started BeRealApplication in 3.456 seconds


================================================================================
                        6. УСТАНОВКА И НАСТРОЙКА
================================================================================

6.1 СИСТЕМНЫЕ ТРЕБОВАНИЯ
-------------------------

Минимальные требования:
- CPU: 2 cores
- RAM: 2 GB
- Disk: 10 GB (с учетом загруженных изображений)
- OS: Windows 10+, macOS 10.14+, Linux (Ubuntu 18.04+)

Рекомендуемые требования:
- CPU: 4+ cores
- RAM: 4+ GB
- Disk: 50+ GB SSD
- OS: Ubuntu 22.04 LTS, macOS 13+, Windows 11

Необходимое ПО:

Java Development Kit (JDK) 17+
Установка:

Windows:
1. Скачать: https://adoptium.net/
2. Запустить установщик
3. Добавить в PATH: JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17...
4. Проверка: java -version

macOS:
brew install openjdk@17
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
java -version

Linux:
sudo apt update
sudo apt install openjdk-17-jdk
java -version

Maven 3.6+
Установка:

Windows:
1. Скачать: https://maven.apache.org/download.cgi
2. Распаковать в C:\Program Files\Apache\maven
3. Добавить в PATH: M2_HOME=C:\Program Files\Apache\maven
4. Проверка: mvn -version

macOS:
brew install maven
mvn -version

Linux:
sudo apt install maven
mvn -version

PostgreSQL 15+
Установка:

Windows:
1. Скачать: https://www.postgresql.org/download/windows/
2. Запустить установщик
3. Запомнить пароль для пользователя postgres
4. pgAdmin4 установится автоматически

macOS:
brew install postgresql@15
brew services start postgresql@15
createdb bereal_db

Linux:
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql
sudo systemctl enable postgresql
sudo -u postgres createdb bereal_db

Git
Установка:

Windows: https://git-scm.com/download/win
macOS: brew install git
Linux: sudo apt install git

Проверка: git --version

6.2 КЛОНИРОВАНИЕ РЕПОЗИТОРИЯ
-----------------------------

Шаг 1: Откройте терминал

Windows: Git Bash или PowerShell
macOS/Linux: Terminal

Шаг 2: Перейдите в рабочую директорию

cd ~/projects
или
cd C:\Users\YourName\projects

Шаг 3: Клонируйте репозиторий

git clone https://github.com/duwniy/BeRealApplication.git
cd BeRealApplication

Структура после клонирования:
BeRealApplication/
├── src/
├── pom.xml
├── README.md
└── .gitignore

6.3 НАСТРОЙКА БАЗЫ ДАННЫХ
--------------------------

Шаг 1: Запустите PostgreSQL

Windows: Службы → PostgreSQL → Запустить
macOS: brew services start postgresql
Linux: sudo systemctl start postgresql

Шаг 2: Подключитесь к PostgreSQL

psql -U postgres

Введите пароль, установленный при установке

Шаг 3: Создайте базу данных

CREATE DATABASE bereal_db;

Шаг 4: Создайте пользователя (опционально)

CREATE USER bereal_user WITH PASSWORD 'secure_password123';
GRANT ALL PRIVILEGES ON DATABASE bereal_db TO bereal_user;

Шаг 5: Проверьте подключение

\c bereal_db
\dt

Должно быть пусто (таблицы создадутся при первом запуске)

Шаг 6: Выход из psql

\q

6.4 КОНФИГУРАЦИЯ ПРИЛОЖЕНИЯ
----------------------------

Откройте файл: src/main/resources/application.properties

# ===================================
# DATABASE CONFIGURATION
# ===================================
spring.datasource.url=jdbc:postgresql://localhost:5432/bereal_db
spring.datasource.username=bereal_user
spring.datasource.password=secure_password123

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# ===================================
# SERVER CONFIGURATION
# ===================================
server.port=8080

# ===================================
# JWT CONFIGURATION
# ===================================
jwt.secret=myVeryLongSecretKeyThatIsAtLeast256BitsLongForHS512AlgorithmPleaseChangeThisInProduction
jwt.expiration=86400000

# ===================================
# FILE UPLOAD
# ===================================
file.upload-dir=uploads/images
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# ===================================
# LOGGING
# ===================================
logging.level.org.example.bereal=DEBUG
logging.level.org.springframework.security=DEBUG

ВАЖНО: Измените jwt.secret на свой уникальный ключ!

Генерация безопасного ключа:
openssl rand -base64 64

Или онлайн: https://randomkeygen.com/ (выберите 504-bit WPA Key)

6.5 СБОРКА ПРОЕКТА
------------------

Шаг 1: Перейдите в корень проекта

cd BeRealApplication

Шаг 2: Очистите предыдущие сборки

mvn clean

Шаг 3: Установите зависимости и соберите

mvn install

Или с пропуском тестов:
mvn install -DskipTests

Процесс:
[INFO] Scanning for projects...
[INFO] Building BeReal Application 1.0.0
[INFO] 
[INFO] --- maven-clean-plugin:3.2.0:clean ---
[INFO] --- maven-resources-plugin:3.3.0:resources ---
[INFO] --- maven-compiler-plugin:3.10.1:compile ---
[INFO] --- maven-surefire-plugin:2.22.2:test ---
[INFO] --- maven-jar-plugin:3.3.0:jar ---
[INFO] --- spring-boot-maven-plugin:3.2.0:repackage ---
[INFO] BUILD SUCCESS

Результат: target/bereal-1.0.0.jar

6.6 ЗАПУСК ПРИЛОЖЕНИЯ
---------------------

Способ 1: Через Maven

mvn spring-boot:run

Способ 2: Через JAR файл

java -jar target/bereal-1.0.0.jar

Способ 3: Через IDE (IntelliJ IDEA)

1. Откройте проект в IntelliJ IDEA
2. Подождите индексацию и загрузку зависимостей
3. Найдите BeRealApplication.java
4. Правый клик → Run 'BeRealApplication'

Успешный запуск:
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

2024-11-10 00:00:01 INFO  BeRealApplication : Starting BeRealApplication
2024-11-10 00:00:02 INFO  FileStorageConfig : ✅ Created upload directory
2024-11-10 00:00:03 INFO  TomcatWebServer : Tomcat started on port(s): 8080
2024-11-10 00:00:03 INFO  BeRealApplication : Started BeRealApplication in 3.456 seconds

Приложение доступно по адресу: http://localhost:8080

6.7 ПРОВЕРКА РАБОТЫ
-------------------

Шаг 1: Проверьте health endpoint

curl http://localhost:8080/actuator/health

Если actuator не включен, увидите 404 - это нормально

Шаг 2: Попробуйте открыть защищенный endpoint

curl http://localhost:8080/api/posts

Ожидаемый ответ: 403 Forbidden (нет токена - правильно!)

Шаг 3: Зарегистрируйте тестового пользователя

curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "fullName": "Test User"
  }'

Ожидаемый ответ:
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": 1,
  "username": "testuser",
  "email": "test@example.com"
}

Шаг 4: Используйте токен

TOKEN="<скопируйте token из ответа>"

curl -X GET http://localhost:8080/api/posts \
  -H "Authorization: Bearer $TOKEN"

Ожидаемый ответ:
{
  "content": [],
  "pageable": {...},
  "totalElements": 0
}

Если все 4 шага прошли успешно - установка завершена! ✅


================================================================================
                        7. КОНФИГУРАЦИЯ
================================================================================

7.1 APPLICATION.PROPERTIES
---------------------------

Полная конфигурация с комментариями:

# ============================================================================
# DATABASE CONFIGURATION
# ============================================================================

# URL подключения к PostgreSQL
# Формат: jdbc:postgresql://<host>:<port>/<database>
spring.datasource.url=jdbc:postgresql://localhost:5432/bereal_db

# Учетные данные для подключения
spring.datasource.username=bereal_user
spring.datasource.password=secure_password123

# Драйвер PostgreSQL (определяется автоматически)
spring.datasource.driver-class-name=org.postgresql.Driver

# Connection Pool (HikariCP - default)
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000

# ============================================================================
# JPA / HIBERNATE CONFIGURATION
# ============================================================================

# Стратегия обновления схемы БД
# - none: не делать ничего
# - validate: только проверка соответствия
# - update: обновление схемы (рекомендуется для разработки)
# - create: пересоздание схемы при каждом запуске
# - create-drop: создание при запуске, удаление при остановке
spring.jpa.hibernate.ddl-auto=update

# Показывать SQL запросы в консоли
spring.jpa.show-sql=true

# Форматировать SQL для читаемости
spring.jpa.properties.hibernate.format_sql=true

# Диалект для PostgreSQL
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Стратегия именования таблиц/колонок
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

# Логирование типов данных при bind
logging.level.org.hibernate.type.descriptor.sql=trace

# ============================================================================
# SERVER CONFIGURATION
# ============================================================================

# Порт сервера
server.port=8080

# Context path (если нужен префикс для всех endpoints)
# server.servlet.context-path=/api/v1

# Сжатие ответов
server.compression.enabled=true
server.compression.mime-types=application/json,application/xml,text/html,text/xml,text/plain

# Таймауты
server.tomcat.connection-timeout=20000

# ============================================================================
# JWT CONFIGURATION
# ============================================================================

# Секретный ключ для подписи JWT (минимум 256 бит для HS512)
# ОБЯЗАТЕЛЬНО измените в production!
jwt.secret=myVeryLongSecretKeyThatIsAtLeast256BitsLongForHS512AlgorithmPleaseChangeThisInProduction

# Время жизни токена в миллисекундах
# 86400000 = 24 часа
# 3600000 = 1 час
jwt.expiration=86400000

# ============================================================================
# FILE UPLOAD CONFIGURATION
# ============================================================================

# Директория для загрузки файлов (относительный путь от корня проекта)
file.upload-dir=uploads/images

# Максимальный размер одного файла
spring.servlet.multipart.max-file-size=10MB

# Максимальный размер всего запроса
spring.servlet.multipart.max-request-size=10MB

# Включить поддержку multipart
spring.servlet.multipart.enabled=true

# ============================================================================
# STATIC RESOURCES
# ============================================================================

# Путь для раздачи статических файлов
spring.web.resources.static-locations=file:uploads/

# ============================================================================
# LOGGING CONFIGURATION
# ============================================================================

# Уровни логирования:
# TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF

# Логирование приложения
logging.level.org.example.bereal=DEBUG

# Логирование Spring Security
logging.level.org.springframework.security=DEBUG

# Логирование SQL запросов
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Логирование в файл
logging.file.name=logs/bereal.log
logging.file.max-size=10MB
logging.file.max-history=30

# Паттерн логов
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n

# ============================================================================
# ACTUATOR (monitoring endpoints)
# ============================================================================

# Включить Actuator endpoints
management.endpoints.web.exposure.include=health,info,metrics

# Base path для actuator
management.endpoints.web.base-path=/actuator

# Показывать детали health
management.endpoint.health.show-details=always

# ============================================================================
# INTERNATIONALIZATION
# ============================================================================

# Кодировка
spring.messages.encoding=UTF-8

# Базовое имя файлов с сообщениями
spring.messages.basename=messages

# ============================================================================
# JSON CONFIGURATION
# ============================================================================

# Формат даты в JSON
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=UTC

# Включение pretty print в development
spring.jackson.serialization.indent_output=true

7.2 ПЕРЕМЕННЫЕ ОКРУЖЕНИЯ
-------------------------

Для Production рекомендуется использовать переменные окружения

Файл .env (не коммитить в Git!):

DB_HOST=localhost
DB_PORT=5432
DB_NAME=bereal_db
DB_USERNAME=bereal_user
DB_PASSWORD=secure_password123

JWT_SECRET=production_secret_key_change_me
JWT_EXPIRATION=86400000

SERVER_PORT=8080

FILE_UPLOAD_DIR=/var/www/bereal/uploads

application.properties с переменными окружения:

spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:bereal_db}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}

jwt.secret=${JWT_SECRET:default_secret_key_change_me}
jwt.expiration=${JWT_EXPIRATION:86400000}

server.port=${SERVER_PORT:8080}

file.upload-dir=${FILE_UPLOAD_DIR:uploads/images}

Синтаксис: ${VARIABLE_NAME:default_value}
- Сначала ищется переменная окружения VARIABLE_NAME
- Если не найдена, используется default_value

Установка переменных окружения:

Linux/macOS:
export DB_PASSWORD="secure_password"
export JWT_SECRET="production_key"

Или в ~/.bashrc / ~/.zshrc:
export DB_PASSWORD="secure_password"
export JWT_SECRET="production_key"

Windows (PowerShell):
$env:DB_PASSWORD="secure_password"
$env:JWT_SECRET="production_key"

Windows (CMD):
set DB_PASSWORD=secure_password
set JWT_SECRET=production_key

Постоянные переменные Windows:
Панель управления → Система → Дополнительные параметры → Переменные среды

Docker:
docker run -e DB_PASSWORD=secure_password -e JWT_SECRET=production_key ...

7.3 ПРОФИЛИ КОНФИГУРАЦИИ
-------------------------

Spring Boot поддерживает профили для разных окружений

Создайте файлы:

application-dev.properties (разработка):
spring.datasource.url=jdbc:postgresql://localhost:5432/bereal_dev
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=create-drop
logging.level.org.example.bereal=DEBUG

application-prod.properties (production):
spring.datasource.url=jdbc:postgresql://prod-db-server:5432/bereal_prod
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=validate
logging.level.org.example.bereal=INFO
logging.file.name=/var/log/bereal/application.log

application-test.properties (тестирование):
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop

Активация профиля:

application.properties:
spring.profiles.active=dev

Или через командную строку:
java -jar bereal.jar --spring.profiles.active=prod

Или через переменную окружения:
export SPRING_PROFILES_ACTIVE=prod

7.4 LOGBACK КОНФИГУРАЦИЯ
-------------------------

Создайте src/main/resources/logback.xml для продвинутого логирования:

<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    
    <!-- Console Appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <!-- File Appender -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/bereal.log</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
        
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/bereal-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>10MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
    </appender>
    
    <!-- Error File Appender -->
    <appender name="ERROR_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/bereal-error.log</file>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
        
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/bereal-error-%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>90</maxHistory>
        </rollingPolicy>
    </appender>
    
    <!-- Loggers -->
    <logger name="org.example.bereal" level="DEBUG"/>
    <logger name="org.springframework.web" level="INFO"/>
    <logger name="org.springframework.security" level="DEBUG"/>
    <logger name="org.hibernate.SQL" level="DEBUG"/>
    <logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="TRACE"/>
    
    <!-- Root Logger -->
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
        <appender-ref ref="ERROR_FILE"/>
    </root>
    
</configuration>

7.5 CUSTOM BANNER
-----------------

Создайте src/main/resources/banner.txt для кастомного банера:

  ____       ____            _ 
 |  _ \     |  _ \          | |
 | |_) | ___| |_) | ___  ___| |
 |  _ < / _ \  _ < / _ \/ __| |
 | |_) |  __/ |_) |  __/\__ \_|
 |____/ \___|____/ \___||___(_)
                                
 :: BeReal Application :: (v1.0.0)
 :: Powered by Spring Boot ${spring-boot.version} ::


================================================================================
                    8. БЕЗОПАСНОСТЬ И АУТЕНТИФИКАЦИЯ
================================================================================

8.1 JWT TOKEN STRUCTURE
------------------------

JSON Web Token состоит из трех частей, разделенных точками:

HEADER.PAYLOAD.SIGNATURE

Пример токена:
eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIsInN1YiI6ImR1d25peSIsImlhdCI6MTY5OTU2NDMyNywiZXhwIjoxNjk5NjUwNzI3fQ.Fn_x1vlkJ8lFW5bz-2o4u7XdfvRol2Q0KyJzGWjHOH27bB2dLDIHi4sLsyNl2qaN_51LcuKR7uMOrw2zGbYtmg

HEADER (Base64 декодированный):
{
  "alg": "HS512",
  "typ": "JWT"
}

Алгоритм: HMAC-SHA512 (симметричное шифрование)
Тип: JSON Web Token

PAYLOAD (Base64 декодированный):
{
  "userId": 2,
  "sub": "duwniy",
  "iat": 1699564327,
  "exp": 1699650727
}

Claims:
- userId: Кастомный claim с ID пользователя (Long)
- sub: Subject - username пользователя (стандартный claim)
- iat: Issued At - время создания токена (Unix timestamp)
- exp: Expiration - время истечения токена (Unix timestamp)

SIGNATURE:
HMACSHA512(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret_key
)

Подпись гарантирует:
1. Токен не был изменен (integrity)
2. Токен создан нашим сервером (authenticity)

8.2 ПРОЦЕСС АУТЕНТИФИКАЦИИ
---------------------------

РЕГИСТРАЦИЯ:

1. Клиент отправляет данные:
   POST /api/auth/register
   {
     "username": "john_doe",
     "email": "john@example.com",
     "password": "SecurePass123",
     "fullName": "John Doe"
   }

2. Сервер валидирует данные:
   - Username: 3-20 символов, уникален
   - Email: валидный формат, уникален
   - Password: 6-40 символов

3. Хеширование пароля:
   String hashedPassword = passwordEncoder.encode("SecurePass123");
   Результат: $2a$10$N9qo8uLOickgx2ZMRZoMye...
   
   BCrypt структура: $2a$rounds$salt$hash
   - $2a: версия BCrypt
   - $10: количество раундов (2^10 = 1024 итерации)
   - salt: случайная соль (автоматически)
   - hash: результат хеширования

4. Сохранение в БД:
   INSERT INTO users (username, email, password, full_name, created_at)
   VALUES ('john_doe', 'john@example.com', '$2a$10$...', 'John Doe', NOW());

5. Генерация JWT токена:
   String token = jwtUtil.generateToken("john_doe", 1);

6. Возврат ответа:
   {
     "token": "eyJhbGciOiJIUzUxMiJ9...",
     "userId": 1,
     "username": "john_doe",
     "email": "john@example.com"
   }

АВТОРИЗАЦИЯ (LOGIN):

1. Клиент отправляет credentials:
   POST /api/auth/login
   {
     "username": "john_doe",
     "password": "SecurePass123"
   }

2. Spring Security Authentication:
   authenticationManager.authenticate(
     new UsernamePasswordAuthenticationToken(username, password)
   )

3. Внутри AuthenticationManager:
   a) Вызывается CustomUserDetailsService.loadUserByUsername()
   b) Загружается User из БД
   c) Вызывается passwordEncoder.matches(rawPassword, encodedPassword)
   d) BCrypt сравнивает:
      - Извлекает salt из сохраненного хеша
      - Хеширует введенный пароль с той же солью
      - Сравнивает результаты
   e) Если совпадает → успешная аутентификация
   f) Если нет → BadCredentialsException

4. Генерация JWT токена:
   String token = jwtUtil.generateToken(user.getUsername(), user.getId());

5. Возврат токена клиенту

ИСПОЛЬЗОВАНИЕ ТОКЕНА:

1. Клиент сохраняет токен:
   localStorage.setItem('token', 'eyJhbGciOiJIUzUxMiJ9...');

2. Клиент отправляет запросы с токеном:
   GET /api/posts
   Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...

3. JwtAuthenticationFilter перехватывает:
   a) Извлекает токен из заголовка
   b) Парсит токен и извлекает username
   c) Загружает UserDetails из БД
   d) Валидирует токен:
      - Проверяет подпись
      - Проверяет срок действия (exp claim)
      - Проверяет username
   e) Если валиден → устанавливает Authentication в SecurityContext
   f) Если нет → запрос отклоняется (401/403)

4. Контроллер получает аутентифицированный запрос:
   - SecurityContext.getAuthentication() содержит UserDetails
   - Можно извлечь userId из токена через JwtUtil
   - Выполняется бизнес-логика

8.3 БЕЗОПАСНОСТЬ ПАРОЛЕЙ
-------------------------

BCRYPT АЛГОРИТМ:

Принцип работы:
1. Генерируется случайная соль (128 бит)
2. Пароль + соль проходят через Blowfish cipher
3. Результат хеширования (192 бита)
4. Финальная строка: $2a$10$salt$hash

Параметры:
- Work Factor (rounds): 10 (по умолчанию)
- Время хеширования: ~100-200ms (на современном CPU)
- Адаптивность: можно увеличивать rounds со временем

Преимущества:
- Каждый пароль хешируется с уникальной солью
- Защита от rainbow tables
- Медленное хеширование → защита от brute-force
- Не нужно хранить соль отдельно (содержится в хеше)

Проверка пароля:
boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

Внутри:
1. Извлекается соль из encodedPassword
2. rawPassword хешируется с той же солью и тем же work factor
3. Сравниваются результаты

РЕКОМЕНДАЦИИ ПО ПАРОЛЯМ:

Минимальные требования:
- Длина: минимум 8 символов
- Содержит: буквы, цифры, спецсимволы
- Не содержит: username, email, распространенные слова

Расширенная валидация (можно добавить):

@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
         message = "Password must contain at least: 1 uppercase, 1 lowercase, 1 digit, 1 special character")
private String password;

Регулярное выражение:
- (?=.*[a-z]): минимум одна строчная буква
- (?=.*[A-Z]): минимум одна заглавная буква
- (?=.*\\d): минимум одна цифра
- (?=.*[@$!%*?&]): минимум один спецсимвол
- {8,}: минимум 8 символов

8.4 ЗАЩИТА ОТ АТАК
------------------

CSRF (Cross-Site Request Forgery):
Статус: ОТКЛЮЧЕН
Причина: Stateless API с JWT не подвержен CSRF
JWT токены хранятся на клиенте, не в cookies

XSS (Cross-Site Scripting):
Защита:
- Не храните JWT в localStorage (уязвимо к XSS)
- Используйте HttpOnly cookies (рекомендуется для production)
- Санитизация пользовательского ввода
- Content-Security-Policy заголовки

SQL Injection:
Защита:
- JPA/Hibernate использует prepared statements
- Параметризованные запросы
- Никогда не конкатенируйте SQL с пользовательским вводом

Path Traversal:
Защита в ImageService:
if (!filePath.startsWith(fileStorageLocation)) {
    log.error("Attempted to delete file outside storage directory");
    return;
}

Предотвращает: ../../../etc/passwd

Brute Force:
Рекомендуется добавить:
- Rate limiting (Spring Security + Bucket4j)
- Account lockout после N неудачных попыток
- CAPTCHA после нескольких попыток

JWT Token Theft:
Защита:
- Короткий срок жизни токена (15 минут - 1 час)
- Refresh tokens (separate endpoint)
- Blacklist для отозванных токенов (Redis)
- Привязка к IP адресу (опционально)

8.5 НАСТРОЙКА HTTPS
-------------------

Для Production обязательно использовать HTTPS!

Самоподписанный сертификат (только для разработки):

keytool -genkeypair -alias bereal -keyalg RSA -keysize 2048 \
  -storetype PKCS12 -keystore bereal.p12 -validity 365

application.properties:
server.ssl.enabled=true
server.ssl.key-store=classpath:bereal.p12
server.ssl.key-store-password=changeit
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=bereal

Production (Let's Encrypt):
1. Получите сертификат через Certbot
2. Конвертируйте в PKCS12:
   openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem \
     -out bereal.p12 -name bereal

3. Настройте в application.properties

Или используйте Nginx/Apache как reverse proxy для SSL termination

8.6 CORS CONFIGURATION
----------------------

Если нужен доступ с frontend:

@Configuration
public class CorsConfig {
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Разрешенные origins
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",  // React dev server
            "https://yourdomain.com"   // Production frontend
        ));
        
        // Разрешенные методы
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));
        
        // Разрешенные заголовки
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Разрешить credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // Expose headers
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization", "Content-Type"
        ));
        
        // Max age для preflight requests
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}

Добавьте в SecurityConfig:
http.cors(cors -> cors.configurationSource(corsConfigurationSource()));


================================================================================
                        9. API ENDPOINTS
================================================================================

9.1 AUTHENTICATION ENDPOINTS
-----------------------------

═══════════════════════════════════════════════════════════════════
POST /api/auth/register
═══════════════════════════════════════════════════════════════════

Описание: Регистрация нового пользователя

Authentication: НЕ ТРЕБУЕТСЯ

Request Headers:
Content-Type: application/json

Request Body:
{
  "username": "john_doe",           // required, 3-20 chars, unique
  "email": "john@example.com",      // required, valid email, unique
  "password": "SecurePass123",      // required, 6-40 chars
  "fullName": "John Doe"            // optional
}

Success Response (201 Created):
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOjEsInN1YiI6ImpvaG5fZG9lIiwiaWF0IjoxNjk5NTY0MzI3LCJleHAiOjE2OTk2NTA3Mjd9.Fn_x1vlkJ8lFW5bz-2o4u7XdfvRol2Q0KyJzGWjHOH27bB2dLDIHi4sLsyNl2qaN_51LcuKR7uMOrw2zGbYtmg",
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com"
}

Error Responses:

400 Bad Request (валидация):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 400,
  "error": "Validation Failed",
  "validationErrors": {
    "username": "Username must be between 3 and 20 characters",
    "email": "Email must be valid",
    "password": "Password must be between 6 and 40 characters"
  }
}

409 Conflict (дубликат):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Username already exists"
}

Пример curl:
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "SecurePass123",
    "fullName": "John Doe"
  }'

═══════════════════════════════════════════════════════════════════
POST /api/auth/login
═══════════════════════════════════════════════════════════════════

Описание: Авторизация существующего пользователя

Authentication: НЕ ТРЕБУЕТСЯ

Request Headers:
Content-Type: application/json

Request Body:
{
  "username": "john_doe",           // required
  "password": "SecurePass123"       // required
}

Success Response (200 OK):
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com"
}

Error Responses:

401 Unauthorized (неверные credentials):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid username or password"
}

Пример curl:
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "SecurePass123"
  }'

9.2 POST ENDPOINTS
------------------

═══════════════════════════════════════════════════════════════════
POST /api/posts
═══════════════════════════════════════════════════════════════════

Описание: Создание нового поста

Authentication: ТРЕБУЕТСЯ (JWT token)

Request Headers:
Authorization: Bearer <token>
Content-Type: multipart/form-data

Request Body (form-data):
primaryImage: [file]              // required, image file
secondaryImage: [file]            // required, image file
caption: "Beautiful sunset!"      // optional, max 200 chars
visibility: "PUBLIC"              // optional, default: PUBLIC
                                  // values: PUBLIC, FRIENDS_ONLY, PRIVATE

Success Response (201 Created):
{
  "id": 1,
  "userId": 1,
  "primaryImageUrl": "http://localhost:8080/images/1_primary_550e8400-e29b-41d4-a716-446655440000.jpg",
  "secondaryImageUrl": "http://localhost:8080/images/1_secondary_6ba7b810-9dad-11d1-80b4-00c04fd430c8.jpg",
  "postedAt": "2024-11-10T14:23:00",
  "isLate": false,
  "caption": "Beautiful sunset!",
  "visibility": "PUBLIC"
}

Error Responses:

400 Bad Request (валидация файла):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "File must be an image"
}

409 Conflict (уже постил сегодня):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "You have already posted today. Come back tomorrow!"
}

413 Payload Too Large:
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 413,
  "error": "File Too Large",
  "message": "Maximum upload size exceeded"
}

Пример curl:
curl -X POST http://localhost:8080/api/posts \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -F "primaryImage=@front.jpg" \
  -F "secondaryImage=@back.jpg" \
  -F "caption=Beautiful sunset!" \
  -F "visibility=PUBLIC"

═══════════════════════════════════════════════════════════════════
GET /api/posts
═══════════════════════════════════════════════════════════════════

Описание: Получение списка видимых постов с пагинацией

Authentication: ТРЕБУЕТСЯ

Request Headers:
Authorization: Bearer <token>

Query Parameters:
page=0                  // optional, default: 0 (номер страницы, 0-based)
size=20                 // optional, default: 20 (размер страницы)
sortBy=postedAt         // optional, default: postedAt (поле сортировки)
sortDir=desc            // optional, default: desc (направление: asc/desc)

Success Response (200 OK):
{
  "content": [
    {
      "id": 1,
      "userId": 1,
      "primaryImageUrl": "http://localhost:8080/images/...",
      "secondaryImageUrl": "http://localhost:8080/images/...",
      "postedAt": "2024-11-10T14:23:00",
      "isLate": false,
      "caption": "Beautiful sunset!",
      "visibility": "PUBLIC"
    },
    ...
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 5,
  "totalElements": 100,
  "last": false,
  "first": true,
  "size": 20,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 20,
  "empty": false
}

Фильтрация видимости:
- Свои посты: всегда видны
- PUBLIC посты: видны всем
- FRIENDS_ONLY: только если вы друзья с автором
- PRIVATE: только владелец

Пример curl:
curl -X GET "http://localhost:8080/api/posts?page=0&size=10&sortBy=postedAt&sortDir=desc" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."

═══════════════════════════════════════════════════════════════════
GET /api/posts/{id}
═══════════════════════════════════════════════════════════════════

Описание: Получение конкретного поста по ID

Authentication: ТРЕБУЕТСЯ

Request Headers:
Authorization: Bearer <token>

Path Parameters:
id: Long (ID поста)

Success Response (200 OK):
{
  "id": 1,
  "userId": 1,
  "primaryImageUrl": "http://localhost:8080/images/...",
  "secondaryImageUrl": "http://localhost:8080/images/...",
  "postedAt": "2024-11-10T14:23:00",
  "isLate": false,
  "caption": "Beautiful sunset!",
  "visibility": "PUBLIC"
}

Error Responses:

404 Not Found:
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Post not found with id: 1"
}

Примечание: Возвращает 404 если пост не виден текущему пользователю

Пример curl:
curl -X GET http://localhost:8080/api/posts/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."

═══════════════════════════════════════════════════════════════════
GET /api/posts/today
═══════════════════════════════════════════════════════════════════

Описание: Получение всех постов, созданных сегодня

Authentication: ТРЕБУЕТСЯ

Request Headers:
Authorization: Bearer <token>

Success Response (200 OK):
[
  {
    "id": 1,
    "userId": 1,
    "primaryImageUrl": "http://localhost:8080/images/...",
    "secondaryImageUrl": "http://localhost:8080/images/...",
    "postedAt": "2024-11-10T14:23:00",
    "isLate": false,
    "caption": "Today's moment",
    "visibility": "PUBLIC"
  },
  ...
]

Фильтрация:
- Свои посты + PUBLIC посты других пользователей за сегодня
- Временной диапазон: 00:00:00 - 23:59:59 текущего дня

Пример curl:
curl -X GET http://localhost:8080/api/posts/today \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."

═══════════════════════════════════════════════════════════════════
GET /api/posts/friends
═══════════════════════════════════════════════════════════════════

Описание: Получение постов друзей

Authentication: ТРЕБУЕТСЯ

Request Headers:
Authorization: Bearer <token>

Success Response (200 OK):
[
  {
    "id": 2,
    "userId": 3,
    "primaryImageUrl": "http://localhost:8080/images/...",
    "secondaryImageUrl": "http://localhost:8080/images/...",
    "postedAt": "2024-11-10T15:30:00",
    "isLate": true,
    "caption": "Late but made it!",
    "visibility": "FRIENDS_ONLY"
  },
  ...
]

Логика:
- Возвращает только посты пользователей из списка друзей
- Включает все visibility типы (PUBLIC, FRIENDS_ONLY, PRIVATE)

Пример curl:
curl -X GET http://localhost:8080/api/posts/friends \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."

═══════════════════════════════════════════════════════════════════
GET /api/posts/user/{userId}
═══════════════════════════════════════════════════════════════════

Описание: Получение постов конкретного пользователя

Authentication: ТРЕБУЕТСЯ

Request Headers:
Authorization: Bearer <token>

Path Parameters:
userId: Long (ID пользователя)

Success Response (200 OK):
[
  {
    "id": 3,
    "userId": 2,
    "primaryImageUrl": "http://localhost:8080/images/...",
    "secondaryImageUrl": "http://localhost:8080/images/...",
    "postedAt": "2024-11-09T10:15:00",
    "isLate": false,
    "caption": "Yesterday's memory",
    "visibility": "PUBLIC"
  },
  ...
]

Фильтрация видимости:
- Свои посты: все (включая PRIVATE)
- Чужие посты: только PUBLIC

Пример curl:
curl -X GET http://localhost:8080/api/posts/user/2 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."

═══════════════════════════════════════════════════════════════════
PUT /api/posts/{id}
═══════════════════════════════════════════════════════════════════

Описание: Обновление поста (только caption и visibility)

Authentication: ТРЕБУЕТСЯ

Request Headers:
Authorization: Bearer <token>
Content-Type: application/x-www-form-urlencoded

Path Parameters:
id: Long (ID поста)

Request Body (form data):
caption=Updated caption          // optional
visibility=FRIENDS_ONLY          // optional

Success Response (200 OK):
{
  "id": 1,
  "userId": 1,
  "primaryImageUrl": "http://localhost:8080/images/...",
  "secondaryImageUrl": "http://localhost:8080/images/...",
  "postedAt": "2024-11-10T14:23:00",
  "isLate": false,
  "caption": "Updated caption",
  "visibility": "FRIENDS_ONLY"
}

Error Responses:

403 Forbidden (не владелец):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "You can only update your own posts"
}

404 Not Found:
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Post not found with id: 1"
}

Ограничения:
- Нельзя изменить изображения
- Нельзя изменить userId
- Нельзя изменить postedAt
- Нельзя изменить isLate

Пример curl:
curl -X PUT http://localhost:8080/api/posts/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -d "caption=Updated caption" \
  -d "visibility=FRIENDS_ONLY"

═══════════════════════════════════════════════════════════════════
DELETE /api/posts/{id}
═══════════════════════════════════════════════════════════════════

Описание: Удаление поста

Authentication: ТРЕБУЕТСЯ

Request Headers:
Authorization: Bearer <token>

Path Parameters:
id: Long (ID поста)

Success Response (204 No Content):
(пустое тело ответа)

Error Responses:

403 Forbidden (не владелец):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "You can only delete your own posts"
}

404 Not Found:
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Post not found with id: 1"
}

Примечание: Изображения с сервера НЕ удаляются автоматически
(можно добавить @PreRemove hook в Post entity)

Пример curl:
curl -X DELETE http://localhost:8080/api/posts/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."

9.3 FRIENDSHIP ENDPOINTS
------------------------

═══════════════════════════════════════════════════════════════════
POST /api/friends/request/{friendId}
═══════════════════════════════════════════════════════════════════

Описание: Отправка запроса в друзья

Authentication: ТРЕБУЕТСЯ

Request Headers:
Authorization: Bearer <token>

Path Parameters:
friendId: Long (ID пользователя, которому отправляется запрос)

Success Response (201 Created):
{
  "id": 1,
  "userId": 1,
  "friendId": 2,
  "status": "PENDING",
  "createdAt": "2024-11-10T15:00:00",
  "acceptedAt": null
}

Error Responses:

400 Bad Request (попытка добавить себя):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Cannot send friend request to yourself"
}

400 Bad Request (пользователь не найден):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "User not found"
}

400 Bad Request (запрос уже существует):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Friend request already exists"
}

400 Bad Request (обратный запрос):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "This user has already sent you a request"
}

Пример curl:
curl -X POST http://localhost:8080/api/friends/request/2 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."

═══════════════════════════════════════════════════════════════════
POST /api/friends/accept/{requesterId}
═══════════════════════════════════════════════════════════════════

Описание: Принятие запроса в друзья

Authentication: ТРЕБУЕТСЯ

Request Headers:
Authorization: Bearer <token>

Path Parameters:
requesterId: Long (ID пользователя, отправившего запрос)

Success Response (200 OK):
{
  "id": 1,
  "userId": 2,
  "friendId": 1,
  "status": "ACCEPTED",
  "createdAt": "2024-11-10T15:00:00",
  "acceptedAt": "2024-11-10T15:05:00"
}

Error Responses:

400 Bad Request (запрос не найден):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Friend request not found"
}

400 Bad Request (неверный статус):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Request is not pending"
}

Примечание: 
- userId - отправитель запроса
- friendId - получатель (тот кто принимает)

Пример curl:
curl -X POST http://localhost:8080/api/friends/accept/2 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."

═══════════════════════════════════════════════════════════════════
POST /api/friends/reject/{requesterId}
═══════════════════════════════════════════════════════════════════

Описание: Отклонение запроса в друзья

Authentication: ТРЕБУЕТСЯ

Request Headers:
Authorization: Bearer <token>

Path Parameters:
requesterId: Long (ID пользователя, отправившего запрос)

Success Response (204 No Content):
(пустое тело ответа)

Error Responses:

400 Bad Request (запрос не найден):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Friend request not found"
}

400 Bad Request (неверный статус):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Request is not pending"
}

Примечание: Запись в БД обновляется на REJECTED (не удаляется)

Пример curl:
curl -X POST http://localhost:8080/api/friends/reject/2 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."

═══════════════════════════════════════════════════════════════════
DELETE /api/friends/{friendId}
═══════════════════════════════════════════════════════════════════

Описание: Удаление из друзей

Authentication: ТРЕБУЕТСЯ

Request Headers:
Authorization: Bearer <token>

Path Parameters:
friendId: Long (ID друга для удаления)

Success Response (204 No Content):
(пустое тело ответа)

Error Responses:

400 Bad Request (дружба не найдена):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Friendship not found"
}

403 Forbidden (нет прав):
{
  "timestamp": "2024-11-10T00:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "You cannot remove this friendship"
}

Примечание: 
- Запись полностью удаляется из БД
- Ищет в обе стороны (A→B или B→A)

Пример curl:
curl -X DELETE http://localhost:8080/api/friends/2 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."

═══════════════════════════════════════════════════════════════════
GET /api/friends
═══════════════════════════════════════════════════════════════════

Описание: Получение списка ID друзей

Authentication: ТРЕБУЕТСЯ

Request Headers:
Authorization: Bearer <token>

Success Response (200 OK):
[2, 3, 5, 7, 11]

Примечание: Возвращает массив Long (ID пользователей-друзей)

Пример curl:
curl -X GET http://localhost:8080/api/friends \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."

═══════════════════════════════════════════════════════════════════
GET /api/friends/requests/pending
═══════════════════════════════════════════════════════════════════

Описание: Получение входящих запросов в друзья

Authentication: ТРЕБУЕТСЯ

Request Headers:
Authorization: Bearer <token>

Success Response (200 OK):
[
  {
    "id": 1,
    "userId": 5,
    "friendId": 1,
    "status": "PENDING",
    "createdAt": "2024-11-10T14:00:00",
    "acceptedAt": null
  },
  {
    "id": 2,
    "userId": 7,
    "friendId": 1,
    "status": "PENDING",
    "createdAt": "2024-11-10T15:30:00",
    "acceptedAt": null
  }
]

Примечание: 
- friendId - это ID текущего пользователя (получатель)
- userId - ID отправителя запроса

Пример curl:
curl -X GET http://localhost:8080/api/friends/requests/pending \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."

═══════════════════════════════════════════════════════════════════
GET /api/friends/requests/sent
═══════════════════════════════════════════════════════════════════

Описание: Получение исходящих запросов в друзья

Authentication: ТРЕБУЕТСЯ

Request Headers:
Authorization: Bearer <token>

Success Response (200 OK):
[
  {
    "id": 3,
    "userId": 1,
    "friendId": 8,
    "status": "PENDING",
    "createdAt": "2024-11-10T16:00:00",
    "acceptedAt": null
  }
]

Примечание:
- userId - это ID текущего пользователя (отправитель)
- friendId - ID получателя запроса

Пример curl:
curl -X GET http://localhost:8080/api/friends/requests/sent \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."

9.4 FILE ENDPOINTS
------------------

═══════════════════════════════════════════════════════════════════
GET /images/{filename}
═══════════════════════════════════════════════════════════════════

Описание: Получение изображения

Authentication: НЕ ТРЕБУЕТСЯ (публичный доступ)

Path Parameters:
filename: String (имя файла изображения)

Success Response (200 OK):
Content-Type: image/jpeg (или image/png, image/gif, image/webp)
Content-Disposition: inline; filename="2_primary_550e8400...jpg"
Body: [binary image data]

Error Responses:

404 Not Found:
(пустое тело, если файл не существует)

400 Bad Request:
(если filename содержит недопустимые символы)

Примечание:
- Файлы раздаются напрямую из директории uploads/images/
- Content-Type определяется по расширению файла
- Браузер отображает изображение inline (не скачивает)

Пример использования:
В браузере: http://localhost:8080/images/2_primary_550e8400-e29b-41d4-a716-446655440000.jpg

В HTML: <img src="http://localhost:8080/images/2_primary_550e8400...jpg" alt="Post image">

curl:
curl -X GET http://localhost:8080/images/2_primary_550e8400-e29b-41d4-a716-446655440000.jpg \
  --output image.jpg


================================================================================
                        10. МОДЕЛИ ДАННЫХ
================================================================================

10.1 DATABASE SCHEMA
--------------------

ТАБЛИЦА: users
═══════════════════════════════════════════════════════════════════

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    profile_image_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT uk_username UNIQUE (username),
    CONSTRAINT uk_email UNIQUE (email)
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

Связанная таблица: user_roles
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE (user_id, role)
);

Пример данных:
| id | username  | email              | password (BCrypt)           | full_name | created_at          |
|----|-----------|--------------------|-----------------------------|-----------|---------------------|
| 1  | john_doe  | john@example.com   | $2a$10$N9qo8uLOickgx...       | John Doe  | 2024-11-10 10:00:00 |
| 2  | jane_doe  | jane@example.com   | $2a$10$X8po7uMPjdlgy...       | Jane Doe  | 2024-11-10 11:00:00 |

user_roles:
| user_id | role  |
|---------|-------|
| 1       | USER  |
| 2       | USER  |

ТАБЛИЦА: posts
═══════════════════════════════════════════════════════════════════

CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    primary_image_url VARCHAR(500),
    secondary_image_url VARCHAR(500),
    posted_at TIMESTAMP NOT NULL,
    is_late BOOLEAN NOT NULL DEFAULT FALSE,
    caption VARCHAR(200),
    visibility VARCHAR(20) NOT NULL CHECK (visibility IN ('PUBLIC', 'FRIENDS_ONLY', 'PRIVATE')),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_posts_user_id ON posts(user_id);
CREATE INDEX idx_posts_posted_at ON posts(posted_at);
CREATE INDEX idx_posts_user_posted ON posts(user_id, posted_at);
CREATE INDEX idx_posts_visibility ON posts(visibility);

Пример данных:
| id | user_id | primary_image_url          | secondary_image_url        | posted_at           | is_late | caption           | visibility    |
|----|---------|----------------------------|----------------------------|---------------------|---------|-------------------|---------------|
| 1  | 1       | .../1_primary_550e8400.jpg | .../1_secondary_6ba7b8.jpg | 2024-11-10 14:23:00 | false   | Beautiful sunset! | PUBLIC        |
| 2  | 2       | .../2_primary_7c9e6679.jpg | .../2_secondary_8d3a57.jpg | 2024-11-10 14:25:00 | true    | Late but here!    | FRIENDS_ONLY  |
| 3  | 1       | .../1_primary_9f4b2a3c.jpg | .../1_secondary_1e5d7f.jpg | 2024-11-09 10:15:00 | false   | Yesterday         | PRIVATE       |

Важные constraintsы:
- posted_at NOT NULL, но updatable = false в Entity (неизменяемое)
- visibility CHECK constraint гарантирует допустимые значения
- ON DELETE CASCADE: при удалении пользователя удаляются его посты

ТАБЛИЦА: friendships
═══════════════════════════════════════════════════════════════════

CREATE TABLE friendships (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED', 'BLOCKED')),
    created_at TIMESTAMP NOT NULL,
    accepted_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_friend UNIQUE (user_id, friend_id),
    CONSTRAINT chk_not_self CHECK (user_id != friend_id)
);

CREATE INDEX idx_friendships_user_id ON friendships(user_id);
CREATE INDEX idx_friendships_friend_id ON friendships(friend_id);
CREATE INDEX idx_friendships_status ON friendships(status);
CREATE INDEX idx_friendships_user_status ON friendships(user_id, status);

Пример данных:
| id | user_id | friend_id | status   | created_at          | accepted_at         |
|----|---------|-----------|----------|---------------------|---------------------|
| 1  | 1       | 2         | ACCEPTED | 2024-11-10 10:00:00 | 2024-11-10 10:05:00 |
| 2  | 3       | 1         | PENDING  | 2024-11-10 11:00:00 | NULL                |
| 3  | 1       | 4         | REJECTED | 2024-11-10 12:00:00 | NULL                |

Важные constraints:
- UNIQUE (user_id, friend_id): нельзя дублировать запросы
- CHECK (user_id != friend_id): нельзя добавить себя
- accepted_at NULL для непринятых запросов

ТАБЛИЦА: bereal_times
═══════════════════════════════════════════════════════════════════

CREATE TABLE bereal_times (
    id BIGSERIAL PRIMARY KEY,
    notification_time TIMESTAMP UNIQUE NOT NULL,
    sent BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE UNIQUE INDEX idx_bereal_notification_time ON bereal_times(notification_time);
CREATE INDEX idx_bereal_time_sent ON bereal_times(notification_time, sent);

Пример данных:
| id | notification_time   | sent  |
|----|---------------------|-------|
| 1  | 2024-11-09 14:23:00 | true  |
| 2  | 2024-11-10 19:47:00 | false |

Использование:
- Одна запись на день
- sent = false до отправки уведомления
- sent = true после отправки
- UNIQUE constraint на notification_time предотвращает дубликаты

10.2 ENTITY RELATIONSHIPS
-------------------------

ДИАГРАММА ER (Entity-Relationship):

┌──────────────────┐
│      users       │
│──────────────────│
│ PK  id           │
│ UK  username     │
│ UK  email        │
│     password     │
│     full_name    │
│     created_at   │
└──────────────────┘
         │ 1
         │
         │ *
         ├─────────────────────────┐
         │                         │
         │                         │
         │ 1                       │ 1
         │                         │
┌────────▼──────────┐    ┌─────────▼──────────┐
│      posts        │    │   friendships      │
│───────────────────│    │────────────────────│
│ PK  id            │    │ PK  id             │
│ FK  user_id       │    │ FK  user_id        │
│     primary_img   │    │ FK  friend_id      │
│     secondary_img │    │     status         │
│     posted_at     │    │     created_at     │
│     is_late       │    │     accepted_at    │
│     caption       │    └────────────────────┘
│     visibility    │             │
└───────────────────┘             │ *
                                  │
                                  │ 1
                           ┌──────▼──────┐
                           │    users    │
                           │  (friend)   │
                           └─────────────┘

┌──────────────────┐
│  bereal_times    │
│──────────────────│
│ PK  id           │
│ UK  notification │
│     sent         │
└──────────────────┘

ТИПЫ СВЯЗЕЙ:

User → Posts: One-to-Many
- Один пользователь может иметь много постов
- Каждый пост принадлежит одному пользователю
- При удалении пользователя удаляются все его посты (CASCADE)

User → Friendships: Many-to-Many (через промежуточную таблицу)
- Один пользователь может иметь много дружеских связей
- Friendship содержит user_id (инициатор) и friend_id (получатель)
- Связь двусторонняя: A→B и B→A это разные записи
- При удалении пользователя удаляются все его friendships (CASCADE)

BeRealTime: Standalone
- Не имеет прямых FK связей с другими таблицами
- Используется глобально для всех пользователей

10.3 DATA VALIDATION
--------------------

УРОВЕНЬ 1: Database Constraints

users:
- username: NOT NULL, UNIQUE, VARCHAR(20)
- email: NOT NULL, UNIQUE, VARCHAR(255), CHECK (email ~ regex)
- password: NOT NULL, VARCHAR(255) (BCrypt хеш)

posts:
- user_id: NOT NULL, FOREIGN KEY
- posted_at: NOT NULL
- visibility: NOT NULL, CHECK (IN ('PUBLIC', 'FRIENDS_ONLY', 'PRIVATE'))
- caption: VARCHAR(200)

friendships:
- user_id: NOT NULL, FOREIGN KEY
- friend_id: NOT NULL, FOREIGN KEY
- status: NOT NULL, CHECK (IN ('PENDING', 'ACCEPTED', 'REJECTED', 'BLOCKED'))
- UNIQUE (user_id, friend_id)
- CHECK (user_id != friend_id)

УРОВЕНЬ 2: JPA Entity Validation

@Entity
@Table(name = "users")
public class User {
    @Column(unique = true, nullable = false, length = 20)
    private String username;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
}

УРОВЕНЬ 3: DTO Validation (Jakarta Bean Validation)

public record RegisterRequest(
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    String username,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    String password,
    
    String fullName
) {}

УРОВЕНЬ 4: Business Logic Validation

PostService:
- Проверка "один пост в день"
- Проверка времени BeReal для isLate
- Проверка прав доступа при update/delete

FriendshipService:
- Проверка существования пользователя
- Проверка дубликатов запросов
- Проверка обратных запросов
- Проверка статуса перед accept/reject

ImageService:
- Валидация MIME типа (image/*)
- Валидация размера файла (< 10MB)
- Валидация расширения (.jpg, .png, .gif, .webp)
- Path traversal защита

10.4 DATA INTEGRITY
-------------------

ТРАНЗАКЦИИ:

@Transactional аннотация обеспечивает:
- Atomicity: Либо все операции выполнятся, либо ни одна
- Consistency: БД всегда в консистентном состоянии
- Isolation: Транзакции изолированы друг от друга
- Durability: Зафиксированные изменения сохраняются

Примеры использования:

@Transactional
public Post createPost(Post post) {
    // Проверка hasUserPostedToday
    // Сохранение поста
    // Если exception - rollback
}

@Transactional
public Friendship acceptFriendRequest(Long userId, Long requesterId) {
    // Поиск friendship
    // Обновление status и acceptedAt
    // Сохранение
}

REFERENTIAL INTEGRITY:

Foreign Keys с ON DELETE CASCADE:
- Удаление user → автоматически удаляются его posts
- Удаление user → автоматически удаляются его friendships

Проверки на уровне БД:
- UNIQUE constraints предотвращают дубликаты
- CHECK constraints валидируют ENUM значения
- NOT NULL constraints гарантируют обязательные поля

SOFT DELETE (можно добавить):

Вместо физического удаления, добавить:
@Column
private LocalDateTime deletedAt;

@Where(clause = "deleted_at IS NULL")
public class User { ... }

Преимущества:
- Возможность восстановления
- Сохранение истории
- Целостность связанных данных

10.5 DATA MIGRATION
-------------------

При изменении схемы БД рекомендуется использовать:

FLYWAY или LIQUIBASE

Пример Flyway migration:

V1__initial_schema.sql:
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(20) UNIQUE NOT NULL,
    ...
);

V2__add_profile_image.sql:
ALTER TABLE users ADD COLUMN profile_image_url VARCHAR(500);

V3__create_friendships.sql:
CREATE TABLE friendships (
    ...
);

Конфигурация (pom.xml):
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>

application.properties:
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.jpa.hibernate.ddl-auto=validate

Преимущества:
- Версионирование схемы БД
- Repeatable migrations
- Откат изменений
- Консистентность между окружениями


================================================================================
                        11. БИЗНЕС-ЛОГИКА
================================================================================

11.1 ОДИН ПОСТ В ДЕНЬ
---------------------

ПРАВИЛО:
Каждый пользователь может создать только один пост за календарные сутки

РЕАЛИЗАЦИЯ:

private boolean hasUserPostedToday(Long userId) {
    LocalDate today = LocalDate.now();
    LocalDateTime start = today.atStartOfDay();           // 00:00:00.000
    LocalDateTime end = today.atTime(LocalTime.MAX);      // 23:59:59.999999999
    
    List<Post> todayPosts = postRepository.findByUserIdAndPostedAtBetween(
        userId, start, end
    );
    
    return !todayPosts.isEmpty();
}

ВРЕМЕННАЯ ЛОГИКА:

Календарные сутки:
- Начало: 00:00:00.000 по системному времени
- Конец: 23:59:59.999999999

Часовые пояса:
- Используется системный часовой пояс сервера
- Для multi-region: хранить timezone пользователя в User Entity
- Конвертация: ZonedDateTime.now(ZoneId.of(user.getTimezone()))

SQL ЗАПРОС:

SELECT * FROM posts 
WHERE user_id = ? 
  AND posted_at >= '2024-11-10 00:00:00' 
  AND posted_at <= '2024-11-10 23:59:59.999999999';

EDGE CASES:

1. Пользователь пытается создать второй пост:
   →", jsonData.token);
    pm.environment.set("userId", jsonData.userId);
}

REQUEST 3: Create Post
Method: POST
URL: {{baseUrl}}/api/posts
Authorization: Bearer Token
Token: {{token}}
Body (form-data):
- primaryImage: [file]
- secondaryImage: [file]
- caption: "Test post"
- visibility: "PUBLIC"

Tests:
if (pm.response.code === 201) {
    var jsonData = pm.response.json();
    pm.environment.set("postId", jsonData.id);
    console.log("Post created with ID: " + jsonData.id);
}

REQUEST 4: Get All Posts
Method: GET
URL: {{baseUrl}}/api/posts?page=0&size=20&sortBy=postedAt&sortDir=desc
Authorization: Bearer Token
Token: {{token}}

REQUEST 5: Get Post by ID
Method: GET
URL: {{baseUrl}}/api/posts/{{postId}}
Authorization: Bearer Token
Token: {{token}}

REQUEST 6: Update Post
Method: PUT
URL: {{baseUrl}}/api/posts/{{postId}}
Authorization: Bearer Token
Token: {{token}}
Body (x-www-form-urlencoded):
- caption: "Updated caption"
- visibility: "FRIENDS_ONLY"

REQUEST 7: Send Friend Request
Method: POST
URL: {{baseUrl}}/api/friends/request/2
Authorization: Bearer Token
Token: {{token}}

REQUEST 8: Get Friends
Method: GET
URL: {{baseUrl}}/api/friends
Authorization: Bearer Token
Token: {{token}}

REQUEST 9: Delete Post
Method: DELETE
URL: {{baseUrl}}/api/posts/{{postId}}
Authorization: Bearer Token
Token: {{token}}

12.4 JAVASCRIPT FETCH ПРИМЕРЫ
------------------------------

Регистрация:
async function register() {
    try {
        const response = await fetch('http://localhost:8080/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: 'alice',
                email: 'alice@example.com',
                password: 'SecurePass123',
                fullName: 'Alice Johnson'
            })
        });
        
        const data = await response.json();
        
        if (response.ok) {
            localStorage.setItem('token', data.token);
            localStorage.setItem('userId', data.userId);
            console.log('Registered successfully!');
        } else {
            console.error('Registration failed:', data.message);
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

Создание поста:
async function createPost(primaryImage, secondaryImage, caption) {
    const token = localStorage.getItem('token');
    
    const formData = new FormData();
    formData.append('primaryImage', primaryImage);
    formData.append('secondaryImage', secondaryImage);
    formData.append('caption', caption);
    formData.append('visibility', 'PUBLIC');
    
    try {
        const response = await fetch('http://localhost:8080/api/posts', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`
            },
            body: formData
        });
        
        const data = await response.json();
        
        if (response.ok) {
            console.log('Post created:', data);
            return data;
        } else {
            console.error('Failed to create post:', data.message);
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

Получение постов с пагинацией:
async function getPosts(page = 0, size = 20) {
    const token = localStorage.getItem('token');
    
    try {
        const response = await fetch(
            `http://localhost:8080/api/posts?page=${page}&size=${size}&sortBy=postedAt&sortDir=desc`,
            {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            }
        );
        
        const data = await response.json();
        
        if (response.ok) {
            return data;
        } else {
            console.error('Failed to fetch posts:', data.message);
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

Отправка запроса в друзья:
async function sendFriendRequest(friendId) {
    const token = localStorage.getItem('token');
    
    try {
        const response = await fetch(`http://localhost:8080/api/friends/request/${friendId}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        const data = await response.json();
        
        if (response.ok) {
            console.log('Friend request sent:', data);
        } else {
            console.error('Failed to send request:', data.message);
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

12.5 REACT EXAMPLE COMPONENT
-----------------------------

import React, { useState, useEffect } from 'react';

const PostFeed = () => {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    
    useEffect(() => {
        fetchPosts();
    }, [page]);
    
    const fetchPosts = async () => {
        const token = localStorage.getItem('token');
        
        try {
            const response = await fetch(
                `http://localhost:8080/api/posts?page=${page}&size=20`,
                {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                }
            );
            
            const data = await response.json();
            
            setPosts(prev => [...prev, ...data.content]);
            setHasMore(!data.last);
            setLoading(false);
        } catch (error) {
            console.error('Error fetching posts:', error);
            setLoading(false);
        }
    };
    
    const handleLoadMore = () => {
        setPage(prev => prev + 1);
    };
    
    if (loading && page === 0) {
        return <div>Loading...</div>;
    }
    
    return (
        <div className="post-feed">
            {posts.map(post => (
                <div key={post.id} className="post-card">
                    <div className="post-header">
                        <span>User #{post.userId}</span>
                        {post.isLate && <span className="late-badge">Late</span>}
                    </div>
                    
                    <div className="post-images">
                        <img src={post.primaryImageUrl} alt="Primary" />
                        <img src={post.secondaryImageUrl} alt="Secondary" className="secondary" />
                    </div>
                    
                    <div className="post-caption">
                        {post.caption}
                    </div>
                    
                    <div className="post-footer">
                        <span>{new Date(post.postedAt).toLocaleString()}</span>
                        <span className="visibility">{post.visibility}</span>
                    </div>
                </div>
            ))}
            
            {hasMore && (
                <button onClick={handleLoadMore} disabled={loading}>
                    {loading ? 'Loading...' : 'Load More'}
                </button>
            )}
        </div>
    );
};

export default PostFeed;


================================================================================
                        13. ТЕСТИРОВАНИЕ
================================================================================

13.1 UNIT ТЕСТЫ
---------------

Добавьте зависимость в pom.xml:
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

PostService Test:

package org.example.bereal.service;

import org.example.bereal.exception.UserAlreadyPostedException;
import org.example.bereal.model.Post;
import org.example.bereal.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    
    @Mock
    private PostRepository postRepository;
    
    @Mock
    private BeRealTimeService beRealTimeService;
    
    @Mock
    private PostVisibilityChecker visibilityChecker;
    
    @InjectMocks
    private PostService postService;
    
    private Post testPost;
    
    @BeforeEach
    void setUp() {
        testPost = new Post();
        testPost.setUserId(1L);
        testPost.setPrimaryImageUrl("http://localhost:8080/images/test1.jpg");
        testPost.setSecondaryImageUrl("http://localhost:8080/images/test2.jpg");
        testPost.setCaption("Test post");
        testPost.setVisibility(Post.Visibility.PUBLIC);
    }
    
    @Test
    void createPost_Success_WhenUserHasNotPostedToday() {
        // Given
        LocalDateTime beRealTime = LocalDateTime.now().minusMinutes(1);
        when(postRepository.findByUserIdAndPostedAtBetween(anyLong(), any(), any()))
            .thenReturn(Collections.emptyList());
        when(beRealTimeService.getTodayBeRealTime()).thenReturn(beRealTime);
        when(postRepository.save(any(Post.class))).thenReturn(testPost);
        
        // When
        Post result = postService.createPost(testPost);
        
        // Then
        assertNotNull(result);
        assertFalse(result.isLate());
        verify(postRepository, times(1)).save(testPost);
    }
    
    @Test
    void createPost_ThrowsException_WhenUserAlreadyPostedToday() {
        // Given
        when(postRepository.findByUserIdAndPostedAtBetween(anyLong(), any(), any()))
            .thenReturn(List.of(testPost));
        
        // When & Then
        assertThrows(UserAlreadyPostedException.class, () -> {
            postService.createPost(testPost);
        });
        
        verify(postRepository, never()).save(any());
    }
    
    @Test
    void createPost_SetsLateFlagCorrectly_WhenPostedAfterGracePeriod() {
        // Given
        LocalDateTime beRealTime = LocalDateTime.now().minusMinutes(5);
        when(postRepository.findByUserIdAndPostedAtBetween(anyLong(), any(), any()))
            .thenReturn(Collections.emptyList());
        when(beRealTimeService.getTodayBeRealTime()).thenReturn(beRealTime);
        when(postRepository.save(any(Post.class))).thenReturn(testPost);
        
        // When
        Post result = postService.createPost(testPost);
        
        // Then
        assertTrue(result.isLate());
    }
    
    @Test
    void createPost_ThrowsException_WhenUserIdIsNull() {
        // Given
        testPost.setUserId(null);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            postService.createPost(testPost);
        });
    }
}

FriendshipService Test:

package org.example.bereal.service;

import org.example.bereal.model.Friendship;
import org.example.bereal.repository.FriendshipRepository;
import org.example.bereal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendshipServiceTest {
    
    @Mock
    private FriendshipRepository friendshipRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private FriendshipService friendshipService;
    
    @Test
    void sendFriendRequest_Success_WhenValidRequest() {
        // Given
        Long userId = 1L;
        Long friendId = 2L;
        
        when(userRepository.existsById(friendId)).thenReturn(true);
        when(friendshipRepository.findByUserIdAndFriendId(userId, friendId))
            .thenReturn(Optional.empty());
        when(friendshipRepository.findByUserIdAndFriendId(friendId, userId))
            .thenReturn(Optional.empty());
        when(friendshipRepository.save(any(Friendship.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        Friendship result = friendshipService.sendFriendRequest(userId, friendId);
        
        // Then
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(friendId, result.getFriendId());
        assertEquals(Friendship.FriendshipStatus.PENDING, result.getStatus());
        verify(friendshipRepository, times(1)).save(any(Friendship.class));
    }
    
    @Test
    void sendFriendRequest_ThrowsException_WhenSendingToSelf() {
        // Given
        Long userId = 1L;
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            friendshipService.sendFriendRequest(userId, userId);
        });
    }
    
    @Test
    void sendFriendRequest_ThrowsException_WhenUserNotFound() {
        // Given
        Long userId = 1L;
        Long friendId = 999L;
        
        when(userRepository.existsById(friendId)).thenReturn(false);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            friendshipService.sendFriendRequest(userId, friendId);
        });
    }
    
    @Test
    void acceptFriendRequest_Success_WhenRequestExists() {
        // Given
        Long userId = 1L;
        Long requesterId = 2L;
        
        Friendship friendship = new Friendship();
        friendship.setUserId(requesterId);
        friendship.setFriendId(userId);
        friendship.setStatus(Friendship.FriendshipStatus.PENDING);
        
        when(friendshipRepository.findByUserIdAndFriendId(requesterId, userId))
            .thenReturn(Optional.of(friendship));
        when(friendshipRepository.save(any(Friendship.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        Friendship result = friendshipService.acceptFriendRequest(userId, requesterId);
        
        // Then
        assertEquals(Friendship.FriendshipStatus.ACCEPTED, result.getStatus());
        assertNotNull(result.getAcceptedAt());
    }
}

13.2 INTEGRATION ТЕСТЫ
----------------------

PostController Integration Test:

package org.example.bereal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bereal.dto.PostDTO;
import org.example.bereal.model.Post;
import org.example.bereal.model.User;
import org.example.bereal.repository.PostRepository;
import org.example.bereal.repository.UserRepository;
import org.example.bereal.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private String token;
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser = userRepository.save(testUser);
        
        token = jwtUtil.generateToken(testUser.getUsername(), testUser.getId());
    }
    
    @Test
    void createPost_ReturnsCreated_WhenValidRequest() throws Exception {
        // Given
        MockMultipartFile primaryImage = new MockMultipartFile(
            "primaryImage",
            "primary.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );
        
        MockMultipartFile secondaryImage = new MockMultipartFile(
            "secondaryImage",
            "secondary.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );
        
        // When & Then
        mockMvc.perform(multipart("/api/posts")
                .file(primaryImage)
                .file(secondaryImage)
                .param("caption", "Test post")
                .param("visibility", "PUBLIC")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.userId").value(testUser.getId()))
            .andExpect(jsonPath("$.caption").value("Test post"))
            .andExpect(jsonPath("$.visibility").value("PUBLIC"));
    }
    
    @Test
    void createPost_ReturnsConflict_WhenUserAlreadyPostedToday() throws Exception {
        // Given - создаём первый пост
        Post existingPost = new Post();
        existingPost.setUserId(testUser.getId());
        existingPost.setPrimaryImageUrl("http://test.com/1.jpg");
        existingPost.setSecondaryImageUrl("http://test.com/2.jpg");
        postRepository.save(existingPost);
        
        MockMultipartFile primaryImage = new MockMultipartFile(
            "primaryImage",
            "primary.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test".getBytes()
        );
        
        MockMultipartFile secondaryImage = new MockMultipartFile(
            "secondaryImage",
            "secondary.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test".getBytes()
        );
        
        // When & Then
        mockMvc.perform(multipart("/api/posts")
                .file(primaryImage)
                .file(secondaryImage)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message").value("You have already posted today. Come back tomorrow!"));
    }
    
    @Test
    void getPosts_ReturnsOk_WithPagination() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/posts")
                .param("page", "0")
                .param("size", "20")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.pageable.pageNumber").value(0))
            .andExpect(jsonPath("$.pageable.pageSize").value(20));
    }
    
    @Test
    void getPosts_ReturnsUnauthorized_WhenNoToken() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/posts"))
            .andExpect(status().isForbidden());
    }
}

13.3 REPOSITORY ТЕСТЫ
---------------------

PostRepository Test:

package org.example.bereal.repository;

import org.example.bereal.model.Post;
import org.example.bereal.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private PostRepository postRepository;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser = entityManager.persistAndFlush(testUser);
    }
    
    @Test
    void findByUserIdAndPostedAtBetween_ReturnsPostsInRange() {
        // Given
        Post todayPost = createPost(testUser.getId(), LocalDateTime.now());
        Post yesterdayPost = createPost(testUser.getId(), LocalDateTime.now().minusDays(1));
        
        entityManager.persistAndFlush(todayPost);
        entityManager.persistAndFlush(yesterdayPost);
        
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);
        
        // When
        List<Post> results = postRepository.findByUserIdAndPostedAtBetween(
            testUser.getId(), start, end
        );
        
        // Then
        assertEquals(1, results.size());
        assertEquals(todayPost.getId(), results.get(0).getId());
    }
    
    @Test
    void findByPostedAtBetween_ReturnsAllPostsInRange() {
        // Given
        User anotherUser = new User();
        anotherUser.setUsername("another");
        anotherUser.setEmail("another@example.com");
        anotherUser.setPassword("password");
        anotherUser = entityManager.persistAndFlush(anotherUser);
        
        Post post1 = createPost(testUser.getId(), LocalDateTime.now());
        Post post2 = createPost(anotherUser.getId(), LocalDateTime.now());
        
        entityManager.persistAndFlush(post1);
        entityManager.persistAndFlush(post2);
        
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);
        
        // When
        List<Post> results = postRepository.findByPostedAtBetween(start, end);
        
        // Then
        assertEquals(2, results.size());
    }
    
    private Post createPost(Long userId, LocalDateTime postedAt) {
        Post post = new Post();
        post.setUserId(userId);
        post.setPrimaryImageUrl("http://test.com/1.jpg");
        post.setSecondaryImageUrl("http://test.com/2.jpg");
        post.setVisibility(Post.Visibility.PUBLIC);
        return post;
    }
}

13.4 ЗАПУСК ТЕСТОВ
------------------

Через Maven:
mvn test

Запуск конкретного теста:
mvn test -Dtest=PostServiceTest

Запуск с покрытием кода (JaCoCo):
mvn clean test jacoco:report

Через IntelliJ IDEA:
1. Правый клик на test пакете
2. Run 'Tests in...'

Через командную строку с отчётом:
mvn clean verify site

Отчёт находится в: target/site/jacoco/index.html


================================================================================
                        14. PRODUCTION DEPLOYMENT
================================================================================

14.1 ПОДГОТОВКА К PRODUCTION
-----------------------------

CHECKLIST:

□ Изменить jwt.secret на надежный ключ
□ Установить spring.jpa.hibernate.ddl-auto=validate
□ Настроить production базу данных
□ Включить HTTPS
□ Настроить CORS для production frontend
□ Добавить rate limiting
□ Настроить логирование в файл
□ Настроить мониторинг (Prometheus + Grafana)
□ Создать backup стратегию для БД
□ Настроить CI/CD pipeline

PRODUCTION application.properties:

# Database
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration=3600000

# Server
server.port=${PORT:8080}
server.compression.enabled=true

# File Upload
file.upload-dir=${FILE_UPLOAD_DIR:/var/www/bereal/uploads}

# Logging
logging.level.org.example.bereal=INFO
logging.file.name=/var/log/bereal/application.log

# Actuator
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=when-authorized

14.2 DOCKER DEPLOYMENT
----------------------

Dockerfile:

FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/bereal-1.0.0.jar app.jar

# Create uploads directory
RUN mkdir -p /app/uploads/images

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

docker-compose.yml:

version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: bereal_db
      POSTGRES_USER: bereal_user
      POSTGRES_PASSWORD: secure_password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U bereal_user"]
      interval: 10s
      timeout: 5s
      retries: 5

  bereal-app:
    build: .
    ports:
      - "8080:8080"
    environment:
      DB_URL: jdbc:postgresql://postgres:5432/bereal_db
      DB_USERNAME: bereal_user
      DB_PASSWORD: secure_password
      JWT_SECRET: ${JWT_SECRET}
    volumes:
      - app_uploads:/app/uploads
    depends_on:
      postgres:
        condition: service_healthy
    restart: unless-stopped

volumes:
  postgres_data:
  app_uploads:

Сборка и запуск:

docker-compose up -d

Просмотр логов:
docker-compose logs -f bereal-app

Остановка:
docker-compose down

14.3 KUBERNETES DEPLOYMENT
--------------------------

deployment.yaml:

apiVersion: apps/v1
kind: Deployment
metadata:
  name: bereal-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: bereal
  template:
    metadata:
      labels:
        app: bereal
    spec:
      containers:
      - name: bereal
        image: your-registry/bereal:latest
        ports:
        - containerPort: 8080
        env:
        - name: DB_URL
          valueFrom:
            secretKeyRef:
              name: bereal-secrets
              key: db-url
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: bereal-secrets
              key: db-username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: bereal-secrets
              key: db-password
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: bereal-secrets
              key: jwt-secret
        resources:
          limits:
            cpu: "1"
            memory: "1Gi"
          requests:
            cpu: "500m"
            memory: "512Mi"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 20
          periodSeconds: 5
        volumeMounts:
        - name: uploads
          mountPath: /app/uploads
      volumes:
      - name: uploads
        persistentVolumeClaim:
          claimName: bereal-uploads-pvc

service.yaml:

apiVersion: v1
kind: Service
metadata:
  name: bereal-service
spec:
  selector:
    app: bereal
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
  type: LoadBalancer

secrets.yaml:

apiVersion: v1
kind: Secret
metadata:
  name: bereal-secrets
type: Opaque
data:
  db-url: <base64-encoded-value>
  db-username: <base64-encoded-value>
  db-password: <base64-encoded-value>
  jwt-secret: <base64-encoded-value>

Применить конфигурацию:
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
kubectl apply -f secrets.yaml

14.4 CI/CD PIPELINE (GitHub Actions)
-------------------------------------

.github/workflows/deploy.yml:

name: Build and Deploy

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs
