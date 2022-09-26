CREATE DATABASE `quarantaine`;

USE `quarantaine`;

CREATE TABLE `user`(
	`username` VARCHAR(128) NOT NULL,
	`password` VARCHAR(128),
	`name` VARCHAR(128),
	`phonenumber` VARCHAR(16), 
	PRIMARY KEY(`username`)
);

#Placeholder command for creating user.
#CREATE USER `quarantaine`@`localhost` IDENTIFIED BY "password";


GRANT INSERT ON `quarantaine`.* TO `quarantaine`@`localhost`;
GRANT SELECT ON `quarantaine`.* TO `quarantaine`@`localhost`;
GRANT UPDATE ON `quarantaine`.* TO `quarantaine`@`localhost`;

FLUSH PRIVILEGES;