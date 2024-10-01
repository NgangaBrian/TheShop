CREATE DATABASE theshop;

USE theshop;

GRANT ALL PRIVILEGES ON theshop.* TO 'root'@'localhost' IDENTIFIED BY 'Brian123';

CREATE TABLE users(
                        id INT NOT NULL AUTO_INCREMENT,
                        fullname VARCHAR(50) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NULL,
                        googleId VARCHAR(255) NULL,
                        authType VARCHAR (255) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY(id)
);