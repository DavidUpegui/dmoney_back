--liquibase formatted sql

--changeset du:002-create-subcategory-table
CREATE TABLE subcategory (
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    category_id UUID NOT NULL REFERENCES category(id)
);