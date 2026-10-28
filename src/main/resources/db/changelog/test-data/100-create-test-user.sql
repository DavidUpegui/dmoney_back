--liquibase formatted sql

--changeset du:100-create-test-user-table context:dev
INSERT INTO users (id, email, password_hash,name,provider)
    VALUES (
        '10000000-0000-0000-0000-000000000001',
        'emailprueba@gmail.com',
        '$2a$10$qBf9rB4Pr/cMK1qXvRsPIOTKo51F.XE05oejJu15rvAgNNDTl2TSq',
        'John Doe',
        'LOCAL'
        );