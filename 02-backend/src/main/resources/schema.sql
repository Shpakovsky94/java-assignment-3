drop database if exists loginApp;
create database loginApp;
use loginApp;

create table if not exists `LOGINAPP`.`USERS`
(
    id         bigint auto_increment
        primary key,
    username   varchar(100) not null,
    password   varchar(100) not null,
    first_name varchar(100) null,
    last_name  varchar(100) null,
    email      varchar(100) null,
    bday date null,
    constraint UK_6dotkott2kjsp8vw4d0m25fb7
        unique (email),
    constraint UK_r43af9ap4edm43mmtq01oddj6
        unique (username)
);

create table if not exists `LOGINAPP`.`ROLES`
(
    id int auto_increment
        primary key,
    name varchar(20) null
);

create table if not exists `LOGINAPP`.`USER_ROLES`
(
    user_id bigint not null,
    role_id int not null,
    primary key (user_id, role_id),
    constraint FKh8ciramu9cc9q3qcqiv4ue8a6
        foreign key (role_id) references roles (id),
    constraint FKhfh9dx7w3ubf1co1vdev94g3f
        foreign key (user_id) references users (id)
);



