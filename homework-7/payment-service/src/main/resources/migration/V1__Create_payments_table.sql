CREATE TABLE payments (
                        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        product_id BIGINT NOT NULL,
                        amount DECIMAL(19,2) NOT NULL,
                        status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                        payment_method VARCHAR(50),
                        transaction_id VARCHAR(255),
                        payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Комментарии для документации
COMMENT ON TABLE payments IS 'Таблица для хранения платежей';
COMMENT ON COLUMN payments.user_id IS 'ID пользователя';
COMMENT ON COLUMN payments.product_id IS 'ID продукта';
COMMENT ON COLUMN payments.amount IS 'Сумма платежа';
COMMENT ON COLUMN payments.status IS 'Возможные значения статуса платежа: PENDING, SUCCESS, FAILED, CANCELLED';