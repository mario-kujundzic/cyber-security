insert into authorities (name) values ('ROLE_DOCTOR');
insert into authorities (name) values ('ROLE_ADMIN');

-- password == name
insert into users (name, surname, username, password, enabled, role, last_password_reset_date) values ('admin1', 'admin1', 'admin1@gmail.com', '$2y$12$LdA5w1xM5qBwt1l.Srv62etWXUpyfCbU/usi3EprOEdZ3ZbCiDx/K', true, 'ADMIN', '2020-12-07 16:00:00.508-07');
insert into users (name, surname, username, password, enabled, role, last_password_reset_date) values ('admin2', 'admin2', 'admin2@gmail.com', '$2y$12$fcgxX0RiMeL1jbEI/wKt/uD3b1nCrCY22O6Y5ecR28btz4miOnI5q', true, 'ADMIN', '2020-12-07 16:00:00.508-07');

insert into user_authority (user_id, authority_id) values (1, 2);
insert into user_authority (user_id, authority_id) values (2, 2);