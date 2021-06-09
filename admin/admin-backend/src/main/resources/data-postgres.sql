insert into roles (id, name) values (1, 'ROLE_ADMIN');

insert into privileges (id, name) values (1, 'READ_PRIVILEGE');
insert into privileges (id, name) values (2, 'CREATE_PRIVILEGE');
insert into privileges (id, name) values (3, 'UPDATE_PRIVILEGE');
insert into privileges (id, name) values (4, 'DELETE_PRIVILEGE');

insert into roles_privileges (role_id, privilege_id) values (1, 1);
insert into roles_privileges (role_id, privilege_id) values (1, 2);
insert into roles_privileges (role_id, privilege_id) values (1, 3);
insert into roles_privileges (role_id, privilege_id) values (1, 4);


-- password == name
insert into users (name, surname, username, password, enabled, last_password_reset_date) values ('admin1', 'admin1', 'admin1@gmail.com', '$2y$12$LdA5w1xM5qBwt1l.Srv62etWXUpyfCbU/usi3EprOEdZ3ZbCiDx/K', true, '2020-12-07 16:00:00.508-07');

insert into user_roles (user_id, role_id) values (1, 1);

-- root self signed sertifikat od admin app
insert into certificates (common_name, email, revocation_status, root_authority, serial_number, valid_from, valid_to) values ('LotusClinic', 'lotusclinic505@gmail.com', 'false', 'true', '139095100165847.00', '2021-05-04 00:00:00', '2022-05-04 00:00:00');

insert into hospitals (id, common_name, public_key) values (1, 'Hospital1', '-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo4+Cvxm+C17Ds8h10iDU \
BETXVdtz7S9aoWynCiWKZ+19PhIR3/0jZWuQg2wHUiyc9JivnJPS70EAkan3PQ+/ \
+cztEz6d+tM8rCyS1TFuiZvLUc1HpXas59vLE5JNi2fOPMKZl6HnRq6s+YfDR3i5 \
b4V33PBjZKLmQckiy34s6oURaDoEQwVs77JRtt0D21WCUz7q2KJxo0zDP/VOn84G \
25FdZ18TKEBBDCbzlv640HVQZK7Doj35bNJPm0qS1MzYEPyT2woZRM6XRrjORaz6 \
aKo5+3OhVIjY83XQmDBNTWXrypEok1PQUNMBY+hk67Bq86fCyDEkfZhECUFatmjb \
KQIDAQAB \
-----END PUBLIC KEY-----');
