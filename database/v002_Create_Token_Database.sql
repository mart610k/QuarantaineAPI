USE `quarantaine`;

CREATE TABLE `oauth2token`(
	`username` VARCHAR(128) NOT NULL,
	`access_token` BINARY(16) NOT NULL,
	`refresh_token` BINARY(16) NOT NULL,
	`token_type` VARCHAR(10) NOT NULL,
	`valid_to` TIMESTAMP, 
	FOREIGN KEY(`username`) REFERENCES `user`(`username`),
    PRIMARY KEY(`access_token`)
);