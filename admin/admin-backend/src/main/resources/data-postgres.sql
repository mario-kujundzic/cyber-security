insert into authorities (name) values ('ROLE_ADMIN');

-- password == name
insert into users (name, surname, username, password, enabled, last_password_reset_date) values ('admin1', 'admin1', 'admin1@gmail.com', '$2y$12$LdA5w1xM5qBwt1l.Srv62etWXUpyfCbU/usi3EprOEdZ3ZbCiDx/K', true, '2020-12-07 16:00:00.508-07');

insert into user_authority (user_id, authority_id) values (1, 1);