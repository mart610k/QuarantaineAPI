CREATE DATABASE `quarantaine`;

USE `quarantaine`;

CREATE TABLE `user`(
	`username` VARCHAR(128) NOT NULL,
	`password` VARCHAR(128),
	`name` VARCHAR(128),
	`phonenumber` VARCHAR(16), 
	PRIMARY KEY(`username`)
);