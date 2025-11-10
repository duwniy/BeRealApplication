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
spring.datasource.password=${DB_
