--liquibase formatted sql

--changeset du:006-add-category-icon-and-color
ALTER TABLE category ADD COLUMN icon VARCHAR(48);
ALTER TABLE category ADD COLUMN color VARCHAR(32);

ALTER TABLE subcategory ADD COLUMN icon VARCHAR(48);
ALTER TABLE subcategory ADD COLUMN color VARCHAR(32);
