-- Добавляем новую колонку для метода платежа
ALTER TABLE payments ADD COLUMN payment_provider VARCHAR(100);

-- Обновляем существующие записи
UPDATE payments SET payment_provider = 'DEFAULT_PROVIDER' WHERE payment_provider IS NULL;

-- Добавляем ограничение
ALTER TABLE payments ALTER COLUMN payment_provider SET NOT NULL;