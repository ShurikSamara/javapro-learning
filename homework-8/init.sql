-- Создание таблицы limits_db
CREATE DATABASE limits_db;
CREATE USER limits_user WITH PASSWORD 'limits_pass';
GRANT ALL PRIVILEGES ON DATABASE limits_db TO limits_user;