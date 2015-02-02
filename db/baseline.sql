drop database if exists mydb;
create database mydb;
grant usage on *.* to mydb@localhost identified by 'mydb';
grant all privileges on mydb.* to mydb@localhost;
use mydb;

create table users (
    username varchar(50) not null primary key,
    password varchar(250) not null,
    enabled boolean not null,
    email varchar(150) not null,
    fullname varchar(150) not null
) engine = InnoDb;

create table authorities (
    username varchar(50) not null,
    authority varchar(50) not null,
    foreign key (username) references users (username),
    unique index authorities_idx_1 (username, authority)
) engine = InnoDb;

create table persistent_logins (
    username varchar(64) not null,
    series varchar(64) primary key,
    token varchar(64) not null,
    last_used timestamp not null
) engine = InnoDb;