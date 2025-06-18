# Домашнее задание №4. Spring Context

* [Части задания](#части-задания)
* [Пример результатов](#пример-результатов)

## Части задания
[X] Разверните локально postgresql БД, создайте таблицу users (id bigserial primary key, username varchar(255) unique);

[X]Создайте Maven проект и подключите к нему: драйвер postgresql, hickaricp, spring context.

[ ] Создайте пул соединений в виде Spring бина

[X] Создайте класс User (Long id, String username)

[X] Реализуйте в виде бина класс UserDao который позволяет выполнять CRUD операции над пользователями

[X] Реализуйте в виде бина UserService, который позволяет: 

    - создавать,
    - удалять,
    - получать одного,
    - получать всех пользователей из базы данных

[X] Создайте Spring Context, получите из него бин UserService и выполните все возможные операции

## Пример результатов

```
=== Демонстрация работы с пользователями ===

1. Создание пользователей:
Создан: User{id=1, username='john_doe'}
Создан: User{id=2, username='jane_smith'}
Создан: User{id=3, username='admin'}

2. Все пользователи:
User{id=1, username='john_doe'}
User{id=2, username='jane_smith'}
User{id=3, username='admin'}

3. Получение пользователя по ID:
Найден пользователь: User{id=1, username='john_doe'}

4. Обновление пользователя:
Обновлен пользователь: User{id=2, username='jane_doe_updated'}

5. Все пользователи после обновления:
User{id=1, username='john_doe'}
User{id=2, username='jane_doe_updated'}
User{id=3, username='admin'}

6. Удаление пользователя:
Пользователь удален: true

7. Все пользователи после удаления:
User{id=1, username='john_doe'}
User{id=2, username='jane_doe_updated'}
=== Завершение демонстрация работы с пользователями ===
```