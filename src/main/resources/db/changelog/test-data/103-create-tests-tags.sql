--liquibase formatted sql

--changeset du:103-create-tests-tags.sql context:dev
INSERT INTO tag (id, user_id, name, description)
VALUES
    ('40000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', 'Capricho', 'No tan necesario'),
    ('40000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000001', 'Indispensable', 'No negociables'),
    ('40000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000001', 'Pareja', 'Gastados con mi amore');