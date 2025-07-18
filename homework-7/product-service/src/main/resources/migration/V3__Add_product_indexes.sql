-- Индексы для оптимизации
CREATE INDEX idx_products_user_id ON products(user_id);
CREATE INDEX idx_products_product_type ON products(product_type);
CREATE INDEX idx_products_account_number ON products(account_number);
CREATE INDEX idx_products_user_type ON products(user_id, product_type);