DROP DATABASE IF EXISTS somniazodiaca;
CREATE DATABASE somniazodiaca;
USE somniazodiaca;

insert into utenti (created_at, updated_at, email, qi, username, password, ruolo) values (NOW(), NOW(), 'admin', 0, 'admin', 'ADMIN', 'ADMIN');

select * from Utenti u where u.username = 'admin' or u.password = 'ADMIN';