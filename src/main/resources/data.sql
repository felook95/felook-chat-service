insert into user(id, username)
values (1, 'felook');

insert into application_user(password, username, role, is_account_non_expired, is_account_non_locked,
                             is_credentials_non_expired, is_enabled)
values ('asssssssssssfffffffffff', 'felook', 'USER', 1, 1, 1, 1);

insert into conversation(id, public_id)
values (1, 'fd5ff013-69de-4666-bf5a-695d9cdcc016');

insert into sw_conversation_user(id, conversation_id, user_id)
values (1, 1, 1);

insert into message(id, text, conversation_id, user_id)
values (1, 'Helló', 1, 1);