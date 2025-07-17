-- Изменяем тип колонки на VARCHAR
ALTER TABLE products ALTER COLUMN product_type TYPE VARCHAR(50);

-- Обновляем существующие значения, если они есть
UPDATE products SET product_type = 'ACCOUNT' WHERE product_type = 'account';
UPDATE products SET product_type = 'CARD' WHERE product_type = 'card';