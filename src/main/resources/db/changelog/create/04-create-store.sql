
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists store (
    id uuid default gen_random_uuid() unique not null ,
    address_id uuid references address(id) on delete set null on update cascade
)