# Домашнее задание №5. Spring Data JPA

* [Части задания](#части-задания)
* [Пример результатов](#пример-результатов)

## Части задания
Требуется скорректировать сервис на базе предыдущего [домашнего задания](../homework-spring-1):

- [X] вместо имеющейся сущности user сделать Entity

- [X] подключить стартер Spring Data JPA

- [X] вынесите все настройки в файл application.yml/properties

- [X] описать repository для работы с сущностью, можно использовать Query, если будет сочтено нужным

- [X] исправить методы UserService для возможности работы с repository

- [X] описать класс CommandLineRunner и выполнить возможные операции

- [X] добавить в проект миграцию для создания таблиц базы, а также инициализировать тестовый набор данных в БД

## Пример результатов

```
=== Демонстрация работы с пользователями ===
1. Создание пользователей:
    insert 
    into
        users
        (username) 
    values
        (?)
Hibernate: 
    insert 
    into
        users
        (username) 
    values
        (?)
    insert 
    into
        users
        (username) 
    values
        (?)
Hibernate: 
    insert 
    into
        users
        (username) 
    values
        (?)
    insert 
    into
        users
        (username) 
    values
        (?)
Hibernate: 
    insert 
    into
        users
        (username) 
    values
        (?)
Создан: User{id=6, username='john_doe'}
Создан: User{id=7, username='jane_smith'}
Создан: User{id=8, username='admin'}

2. Все пользователи:
    select
        u1_0.id,
        u1_0.username 
    from
        users u1_0
Hibernate: 
    select
        u1_0.id,
        u1_0.username 
    from
        users u1_0
User{id=1, username='test_user_1'}
User{id=2, username='test_user_2'}
User{id=5, username='initial_admin'}
User{id=6, username='john_doe'}
User{id=7, username='jane_smith'}
User{id=8, username='admin'}

3. Получение пользователя по ID:
    select
        u1_0.id,
        u1_0.username 
    from
        users u1_0 
    where
        u1_0.id=?
Hibernate: 
    select
        u1_0.id,
        u1_0.username 
    from
        users u1_0 
    where
        u1_0.id=?
Найден пользователь: User{id=6, username='john_doe'}

4. Обновление пользователя:
    select
        u1_0.id,
        u1_0.username 
    from
        users u1_0 
    where
        u1_0.id=?
Hibernate: 
    select
        u1_0.id,
        u1_0.username 
    from
        users u1_0 
    where
        u1_0.id=?
    update
        users 
    set
        username=? 
    where
        id=?
Hibernate: 
    update
        users 
    set
        username=? 
    where
        id=?
Обновлен пользователь: User{id=7, username='jane_doe_updated'}

5. Все пользователи после обновления:
    select
        u1_0.id,
        u1_0.username 
    from
        users u1_0
Hibernate: 
    select
        u1_0.id,
        u1_0.username 
    from
        users u1_0
User{id=1, username='test_user_1'}
User{id=2, username='test_user_2'}
User{id=5, username='initial_admin'}
User{id=6, username='john_doe'}
User{id=8, username='admin'}
User{id=7, username='jane_doe_updated'}

6. Удаление пользователя:
    select
        count(*) 
    from
        users u1_0 
    where
        u1_0.id=?
Hibernate: 
    select
        count(*) 
    from
        users u1_0 
    where
        u1_0.id=?

    select
        u1_0.id,
        u1_0.username 
    from
        users u1_0 
    where
        u1_0.id=?
Hibernate: 
    select
        u1_0.id,
        u1_0.username 
    from
        users u1_0 
    where
        u1_0.id=?
    delete 
    from
        users 
    where
        id=?
Hibernate: 
    delete 
    from
        users 
    where
        id=?
Пользователь удален: true

7. Все пользователи после удаления:
    select
        u1_0.id,
        u1_0.username 
    from
        users u1_0
Hibernate: 
    select
        u1_0.id,
        u1_0.username 
    from
        users u1_0
User{id=1, username='test_user_1'}
User{id=2, username='test_user_2'}
User{id=5, username='initial_admin'}
User{id=6, username='john_doe'}
User{id=7, username='jane_doe_updated'}
=== Завершение демонстрация работы с пользователями ===
```