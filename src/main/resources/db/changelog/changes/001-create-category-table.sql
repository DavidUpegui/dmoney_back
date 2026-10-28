--liquibase formatted sql

--changeset du:001-create-category-table
CREATE TABLE category(
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    type VARCHAR(50) NOT NULL
);