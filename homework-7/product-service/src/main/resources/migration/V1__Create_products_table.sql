CREATE TABLE products (
                        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        account_number VARCHAR(50) NOT NULL UNIQUE,
                        price DECIMAL(19,2) NOT NULL,
                        product_type VARCHAR(50) NOT NULL,
                        user_id BIGINT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Комментарии
COMMENT ON TABLE products IS 'Таблица для хранения продуктов';
COMMENT ON COLUMN products.account_number IS 'Номер счета продукта';
COMMENT ON COLUMN products.price IS 'Цена/баланс продукта';
COMMENT ON COLUMN products.product_type IS 'Тип продукта: CARD, ACCOUNT, LOAN';
COMMENT ON COLUMN products.user_id IS 'ID владельца продукта';