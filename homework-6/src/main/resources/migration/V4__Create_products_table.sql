-- Создание таблицы products
CREATE TABLE products
(
    id             BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(255)   NOT NULL,
    balance        DECIMAL(15, 2) NOT NULL DEFAULT 0,
    product_type   product_type   NOT NULL,
    user_id        BIGINT         NOT NULL,
    created_at     TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);