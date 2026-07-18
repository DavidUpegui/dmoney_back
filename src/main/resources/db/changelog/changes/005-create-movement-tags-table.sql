--liquibase formatted sql

--changeset du:005-create-movement-tags-table
CREATE TABLE movement_tags (
    movement_id UUID NOT NULL REFERENCES movement(id) ON DELETE CASCADE,
    tag_id      UUID NOT NULL,
    PRIMARY KEY (movement_id, tag_id)
);