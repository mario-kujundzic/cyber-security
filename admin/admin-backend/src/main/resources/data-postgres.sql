insert into roles (id, name) values (1, 'ROLE_ADMIN');
insert into roles (id, name) values (2, 'ROLE_DOCTOR');

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
-- generated cert for hospital1
insert into certificates (common_name, email, revocation_status, root_authority, serial_number, valid_from, valid_to) values ('Hospital1', 'admin1@gmail.com', 'false', 'false', '672496789128620.00', '2021-06-11 02:00:00', '2022-07-11 02:00:00');

insert into hospitals (common_name, hospital_url, public_key) values ('Hospital1', 'https://localhost:9002',
'-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8ujeqk0wBwrZrT/DTG4S
/nhs9qnqTjuwavcrf3scARCD6yB+k+4DsiPjtvt0eBWwCjcIGcEhJSuXP9Dapek4
cNP3F7Y0MJ5R+j5U6mpaxpS2y0FCz3fEAwiaLfp/F1ZDV50oxnGCmLwonrv2Rthb
Toj8fx0v7gOds7lxzmCo8Fe4IKWAAdODrL4bSKdjH8zMKVhadm3Zz/HZXCnFaf3s
OqA+OomtD9NO0PFgkMWT11RRK/H2fypZ6NqZJyrw8H4K4cilXVf9sp3vefSd9AWv
CZbUxz3U2naGbbYBFlbr8Ua6+YfxkQqTt2nWuCOT9GU5AkV6ftLSxBnxLtFpa9uI
UwIDAQAB-----END PUBLIC KEY-----');


insert into requests_csr (common_name, country, email, locality, organization, organization_unit, public_key, state, hospital_name, status, certificate_user) values ('Hospital1', 'RS', 'admin1@gmail.com', 'Novi Sad', 'Lotus Clinic Organization', 'Cyber Security Administrative Center', 
'-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8ujeqk0wBwrZrT/DTG4S
/nhs9qnqTjuwavcrf3scARCD6yB+k+4DsiPjtvt0eBWwCjcIGcEhJSuXP9Dapek4
cNP3F7Y0MJ5R+j5U6mpaxpS2y0FCz3fEAwiaLfp/F1ZDV50oxnGCmLwonrv2Rthb
Toj8fx0v7gOds7lxzmCo8Fe4IKWAAdODrL4bSKdjH8zMKVhadm3Zz/HZXCnFaf3s
OqA+OomtD9NO0PFgkMWT11RRK/H2fypZ6NqZJyrw8H4K4cilXVf9sp3vefSd9AWv
CZbUxz3U2naGbbYBFlbr8Ua6+YfxkQqTt2nWuCOT9GU5AkV6ftLSxBnxLtFpa9uI
UwIDAQAB-----END PUBLIC KEY-----', 
'Vojvodina', 'Hospital1', 1, 0);
