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