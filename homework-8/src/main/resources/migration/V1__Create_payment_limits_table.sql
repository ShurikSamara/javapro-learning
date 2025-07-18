CREATE TABLE payment_limits (
    user_id BIGINT PRIMARY KEY,
    current_limit DECIMAL(12, 2) NOT NULL,
    default_limit DECIMAL(12, 2) NOT NULL,
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_payment_limits_user_id ON payment_limits(user_id);

DO $$
DECLARE
    i INT;
BEGIN
    FOR i IN 1..100 LOOP
        INSERT INTO payment_limits (user_id, current_limit, default_limit)
        VALUES (i, 10000.00, 10000.00);
    END LOOP;
END $$;