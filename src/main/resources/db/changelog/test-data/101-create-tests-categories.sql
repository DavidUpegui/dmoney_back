--liquibase formatted sql

--changeset du:101-tests-categories.sql context:dev
INSERT INTO category (id, user_id, name, description, type)
VALUES
    ('20000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', 'House', 'House things', 'OUTCOME'),
    ('20000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000001', 'Salary', 'Monthly income', 'INCOME'),
    ('20000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000001', 'Personal', 'Personal things', 'OUTCOME');


