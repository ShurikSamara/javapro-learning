-- Вставка тестовых данных
INSERT INTO users (username)
VALUES ('test_user_1'),
       ('test_user_2'),
       ('initial_admin') ON CONFLICT (username) DO NOTHING;