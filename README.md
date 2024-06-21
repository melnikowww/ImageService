### Бейджи CodeClimate:
[![Maintainability](https://api.codeclimate.com/v1/badges/56ad8a0c2aa86a03f993/maintainability)](https://codeclimate.com/github/melnikowww/testTask/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/56ad8a0c2aa86a03f993/test_coverage)](https://codeclimate.com/github/melnikowww/testTask/test_coverage)
## Описание:
Проект позволяющий создать пользователя с двумя возможными ролями (USER и MODERATOR), загружать и скачивать изображения 
в формате jpg/png. Предусмотрена авторизация и аутентификация. При регистрации, загрузке и скачивании пользователь 
получает на email, указанный при регистрации, письмо с подтверждением операции и некоторыми данными о ней. Связь между микросервисами реализована посредством брокера сообщений RabbitMQ.
### Использованные технологии:
* Spring Boot 
* Spring Data JPA
* Spring Security
* Spring EMail
* RabbitMQ
* JUnit/AssertJ
* JWT
* H2 
* Project Lombok
* QueryDSL
### Сборка и запуск
```
# Запуск RabbitMQ
docker run -it --rm --name rabbitmq -p 5600:5672 -p 15670:15672 rabbitmq:3.9-management

# Сборка проекта
gradle build

# Локальный запуск
gradle run
```
