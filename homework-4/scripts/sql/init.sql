-- Создание таблицы users
CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     username VARCHAR(255) UNIQUE NOT NULL
    );

-- Вставка тестовых данных (опционально)
INSERT INTO users (username) VALUES
                                 ('test_user1'),
                                 ('test_user2')
    ON CONFLICT (username) DO NOTHING;