DROP DATABASE IF EXISTS somniazodiaca;
CREATE DATABASE somniazodiaca;
USE somniazodiaca;

-- password: admin
insert into utenti (created_at, updated_at, email, qi, username, password_hash, ruolo) values (NOW(), NOW(), 'admin', 0, 'admin', '{bcrypt}$2a$12$DpDfIVCJrVCSNUukA/.1OeWJldzXfVLLZt6M6NuJ7AFpC6p392K.G', 'ADMIN');