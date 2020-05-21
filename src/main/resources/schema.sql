create table conversation
(
    id        int  not null auto_increment primary key,
    public_id uuid not null
);
create table application_user
(
    id                         int         not null auto_increment primary key,
    password                   varchar(60) not null,
    username                   varchar(20) not null unique,
    role                       varchar     not null,
    is_account_non_expired     boolean     not null,
    is_account_non_locked      boolean     not null,
    is_credentials_non_expired boolean     not null,
    is_enabled                 boolean     not null
);
create table user
(
    id       int         not null auto_increment primary key,
    username varchar(20) not null unique
);
create table message
(
    id              int          not null auto_increment primary key,
    text            varchar(100) not null,
    conversation_id int,
    user_id         int,
    constraint KF_userMessage foreign key (user_id)
        references user (id),
    constraint FK_conversationMessage foreign key (conversation_id)
        references conversation (id)
);
create table sw_conversation_user
(
    id              int not null auto_increment primary key,
    conversation_id int,
    user_id         int,
    constraint FK_user_sw_conversation_user foreign key (user_id)
        references user (id),
    constraint FK_conversation_sw_conversation_user foreign key (conversation_id)
        references conversation (id),
    constraint uc_conversation_user unique (conversation_id, user_id)
);