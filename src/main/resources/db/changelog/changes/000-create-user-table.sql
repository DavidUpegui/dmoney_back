--liquibase formatted sql

--changeset amor:000-create-users-table
CREATE TABLE users (
    id            UUID PRIMARY KEY,
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    name          VARCHAR(255) NOT NULL,
    provider      VARCHAR(255) NOT NULL,
    CONSTRAINT uq_users_email UNIQUE (email)
);