--liquibase formatted sql

--changeset du:003-create-tag-table
CREATE TABLE tag (
    id          UUID PRIMARY KEY,
    user_id     UUID NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(500)
);