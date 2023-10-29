--liquibase formatted sql

--changeset tamsap:2
CREATE TABLE transactions (
	id UUID PRIMARY KEY,
	summary VARCHAR(255),
	category VARCHAR(255) NOT NULL,
	sum NUMERIC NOT NULL,
	currency VARCHAR(3) NOT NULL,
	paid_at TIMESTAMP NOT NULL,
	user_id UUID NOT NULL,
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO transactions(id, summary, category, sum, currency, paid_at, user_id) VALUES(gen_random_uuid(), 'Lunch', 'Food', 10.00, 'EUR', '2023-08-01 12:00:00', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11');
INSERT INTO transactions(id, summary, category, sum, currency, paid_at, user_id) VALUES(gen_random_uuid(), 'Dinner', 'Food', 20.00, 'EUR', '2023-08-01 19:00:00', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11');
INSERT INTO transactions(id, summary, category, sum, currency, paid_at, user_id) VALUES(gen_random_uuid(), 'Taxi', 'Transport', 15.00, 'EUR', '2023-08-01 20:00:00', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11');
INSERT INTO transactions(id, summary, category, sum, currency, paid_at, user_id) VALUES(gen_random_uuid(), 'Hotel', 'Accommodation', 100.00, 'EUR', '2023-08-01 21:00:00', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11');
INSERT INTO transactions(id, summary, category, sum, currency, paid_at, user_id) VALUES(gen_random_uuid(), 'Breakfast', 'Food', 10.00, 'EUR', '2023-08-02 08:00:00', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11');
INSERT INTO transactions(id, summary, category, sum, currency, paid_at, user_id) VALUES(gen_random_uuid(), 'Lunch', 'Food', 10.00, 'EUR', '2023-09-02 12:00:00', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11');
INSERT INTO transactions(id, summary, category, sum, currency, paid_at, user_id) VALUES(gen_random_uuid(), 'Dinner', 'Food', 20.00, 'EUR', '2023-09-02 19:00:00', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11');
INSERT INTO transactions(id, summary, category, sum, currency, paid_at, user_id) VALUES(gen_random_uuid(), 'Taxi', 'Transport', 15.00, 'EUR', '2023-09-02 20:00:00', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11');
INSERT INTO transactions(id, summary, category, sum, currency, paid_at, user_id) VALUES(gen_random_uuid(), 'Hotel', 'Accommodation', 100.00, 'EUR', '2023-09-02 21:00:00', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11');
