--liquibase formatted sql

--changeset du:004-create-movement-table
CREATE TABLE movement (
    id          UUID PRIMARY KEY,
    user_id     UUID NOT NULL,
    cat_id      UUID NOT NULL REFERENCES category(id),
    subcat_id   UUID NOT NULL,
    amount      NUMERIC(19,4) NOT NULL,
    type        VARCHAR(20) NOT NULL,
    description VARCHAR(500),
    date        DATE NOT NULL
);