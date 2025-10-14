
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists category (
    id uuid default gen_random_uuid() unique not null ,
    name varchar unique not null ,
    description text unique not null ,
    image_id uuid unique references image(id) on delete set null on update cascade
)