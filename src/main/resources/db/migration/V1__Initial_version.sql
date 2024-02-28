create sequence app_user_seq start with 1 increment by 1;

create table app_user
(
    id bigint not null,
    email varchar(255) not null unique,
    password varchar(255) not null,
    primary key (id)
);

create table role
(
    id bigserial not null,
    name varchar(255) not null unique check (name in ('USER','ADMIN')),
    primary key (id)
);

create table user_role
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id)
);

alter table if exists user_role
    add constraint FKg7fr1r7o0fkk41nfhnjdyqn7b
        foreign key (user_id)
            references app_user;

alter table if exists user_role
    add constraint FKa68196081fvovjhkek5m97n3y
        foreign key (role_id)
            references role;
