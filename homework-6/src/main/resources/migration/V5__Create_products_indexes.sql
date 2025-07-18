-- Создание индекса для поиска продуктов по пользователю
CREATE INDEX idx_products_user_id ON products(user_id);
CREATE INDEX idx_products_account_number ON products(account_number);
CREATE INDEX idx_products_type ON products(product_type);