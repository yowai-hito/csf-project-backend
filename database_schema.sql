drop database if exists csf_project;

create database csf_project;

use csf_project;

create table users (
  account_id int AUTO_INCREMENT PRIMARY KEY,
  account_name varchar(64) UNIQUE,
  account_password varchar(64),
  account_role VARCHAR(8),
  account_handle varchar(64) UNIQUE,
  account_email VARCHAR(64) UNIQUE
);

create table chatrooms (
  chatroom_id VARCHAR(32) PRIMARY KEY,
  chatroom_name varchar(64)
);

create table chatroom_users (
  chatroom_id VARCHAR(32),
  account_id int NOT NULL,

  CONSTRAINT fk_account_handle
    FOREIGN KEY (account_id) REFERENCES users(account_id),
  CONSTRAINT fk_chatroom_id
    FOREIGN KEY (chatroom_id) REFERENCES chatrooms(chatroom_id)
);

create table chatroom_chats (
  chatroom_id VARCHAR(32),
  account_id int NOT NULL,
  post varchar(500) NOT NULL,
  content_rating float,

  CONSTRAINT fk_chatroom_post_id
    FOREIGN KEY (chatroom_id) REFERENCES chatrooms(chatroom_id),
  CONSTRAINT fk_account_post_id
    FOREIGN KEY (account_id) REFERENCES users(account_id)
);

drop table chatroom_users;