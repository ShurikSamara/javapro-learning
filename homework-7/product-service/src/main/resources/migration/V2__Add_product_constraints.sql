-- Добавляем проверочные ограничения
ALTER TABLE products ADD CONSTRAINT chk_price_positive CHECK (price >= 0);
ALTER TABLE products ADD CONSTRAINT chk_product_type CHECK (product_type IN ('CARD', 'ACCOUNT', 'LOAN'));
ALTER TABLE products ADD CONSTRAINT chk_user_id_positive CHECK (user_id > 0);

-- Добавляем новые поля
ALTER TABLE products ADD COLUMN is_active BOOLEAN DEFAULT TRUE;
ALTER TABLE products ADD COLUMN description TEXT;