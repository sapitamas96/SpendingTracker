--liquibase formatted sql

--changeset tamsap:1
CREATE TABLE users (
	id UUID PRIMARY KEY,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL
);

ALTER TABLE users ADD CONSTRAINT unique_email UNIQUE (email);

INSERT INTO users(id, first_name, last_name, email) VALUES('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'John', 'Doe', 'john.doe@fakemail.com');
