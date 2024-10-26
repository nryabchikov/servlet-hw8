create table if not exists users
(
    id            serial primary key,
    age           int,
    name          varchar(50),
    surname       varchar(50),
    patronymic    varchar(50),
    date_of_birth date
);