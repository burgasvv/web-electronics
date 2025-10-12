
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists store_product (
    store_id uuid references store(id) on delete cascade on update cascade ,
    product_id uuid references product(id) on delete cascade on update cascade ,
    amount bigint not null default 0 check ( amount >= 0 ) ,
    primary key (store_id, product_id)
)