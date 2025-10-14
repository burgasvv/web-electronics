
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists bucket_product (
    bucket_id uuid references bucket(id) on delete cascade on update cascade ,
    product_id uuid references product(id) on delete cascade on update cascade ,
    amount bigint not null default 0 check ( amount >= 0 ) ,
    price decimal not null default 0 check ( price >= 0 ) ,
    primary key (bucket_id, product_id)
)