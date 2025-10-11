
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists identity (
    id uuid default gen_random_uuid() unique not null ,
    authority varchar not null ,
    email varchar unique not null ,
    pass varchar not null ,
    firstname varchar not null ,
    lastname varchar not null ,
    patronymic varchar not null ,
    enabled boolean not null ,
    image_id uuid references image(id) on delete set null on update cascade
)