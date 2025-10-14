
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists bucket (
    id uuid default gen_random_uuid() unique not null ,
    identity_id uuid unique references identity(id) on delete cascade on update cascade ,
    balance decimal not null default 0 check ( balance >= 0 )
)