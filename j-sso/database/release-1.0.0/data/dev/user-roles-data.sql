--liquibase formatted sql

--changeSet daivanov:users-role-data-01
INSERT INTO sso.user_roles(user_id, role_id)
VALUES ((SELECT user_id FROM sso.users WHERE email = 'admin@example.com'),
        (SELECT role_id FROM sso.roles WHERE role_code = 'ADMIN_SSO'))
ON CONFLICT DO NOTHING;