CREATE TABLE IF NOT EXISTS ACCOUNT_USER (
    id INT PRIMARY KEY,
    name VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

insert into account_user(id, name, created_at, updated_at)
values (1, 'Pororo' , now() , now());

insert into account_user(id, name, created_at, updated_at)
values (2, 'Lupi' , now() , now());

insert into account_user(id, name, created_at, updated_at)
values (3, 'Eddie' , now() , now());