use mydb;

insert into users (username, password, enabled, email, fullname) values ('admin', '$2a$10$wx0uVRyLoCLMouSNL.mFmOh4gbOWfntMTEBwnKNwKivE4jNHKU7wa', true, 'admin@mail.com', 'Administrator');
insert into authorities (username, authority) values ('admin', 'ADMIN');