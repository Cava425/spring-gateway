delete from oauth_client_details;
delete from oauth_client_token;
delete from oauth_access_token;
delete from oauth_refresh_token;
delete from oauth_code;
delete from oauth_approvals;


delete from oauth_client_details;
insert into oauth_client_details (client_id, client_secret, scope, authorized_grant_types, authorities, access_token_validity, refresh_token_validity)
                values ('client', '$2a$10$//J8rEY671f0ktW4PvsLxuoLIcLzluKmQTmAv5WQ2RhSwZp9v4OI.', 'all', 'password,refresh_token', 'ROLE_ADMIN,ROLE_USER', 7200, 7200);
