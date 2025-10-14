
--liquibase formatted sql

--changeset burgasvv:1
begin ;
insert into category(name, description) values ('Компьютеры', 'Описание категории Компьютеры');
insert into category(name, description) values ('Ноутбуки', 'Описание категории Ноутбуки');
insert into category(name, description) values ('Смартфоны', 'Описание категории Смартфоны');
commit ;