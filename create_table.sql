DROP DATABASE IF EXISTS moviedb;
CREATE DATABASE moviedb;
USE moviedb;

DROP TABLE IF EXISTS stars_in_movies;
DROP TABLE IF EXISTS genres_in_movies;
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS ratings;
DROP TABLE IF EXISTS movies;
DROP TABLE IF EXISTS stars;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS creditcards;
CREATE TABLE movies(
        movieID  varchar(10) NOT NULL DEFAULT '',
        title varchar(100) NOT NULL DEFAULT '',
        yearz int(4) NOT NULL ,
        director varchar(100) NOT NULL DEFAULT '',
        PRIMARY KEY(movieID)
);

CREATE TABLE stars(
        id  varchar(10) NOT NULL DEFAULT '',
        name varchar(100) NOT NULL DEFAULT '',
        birthYear int(4) DEFAULT NULL,
        PRIMARY KEY(id)
);

CREATE TABLE stars_in_movies(
        id  varchar(10) NOT NULL DEFAULT '',
        movieID varchar(10) NOT NULL DEFAULT '',
        PRIMARY KEY(id, movieID),
        FOREIGN KEY(id) REFERENCES stars(id),
        FOREIGN KEY(movieID) REFERENCES movies(movieID)
        
);

CREATE TABLE genres(
        gID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        gNames varchar(32) NOT NULL DEFAULT ''
);

CREATE TABLE genres_in_movies(
        gID INT NOT NULL,
        movieID varchar(10) NOT NULL DEFAULT '',
        PRIMARY KEY(gID, movieID),
        FOREIGN KEY(gID) REFERENCES genres(gID),
        FOREIGN KEY(movieID) REFERENCES movies(movieID)
        
);

CREATE TABLE creditcards(
	ccID varchar(20) NOT NULL DEFAULT '',
	firstName varchar(50) NOT NULL DEFAULT '',
	lastName varchar(50) NOT NULL DEFAULT '',
	expiration date NOT NULL,
	PRIMARY KEY(ccID)
);

CREATE TABLE customers(
        cid int NOT NULL AUTO_INCREMENT ,
        firstName varchar(50) NOT NULL DEFAULT '',
        lastName varchar(50) NOT NULL DEFAULT '',
        ccID varchar(20) NOT NULL DEFAULT '',
        address varchar(200) NOT NULL DEFAULT '',
        email varchar(50) NOT NULL DEFAULT '',
        cPassword varchar(20) NOT NULL DEFAULT '',
        PRIMARY KEY(cid, ccID),
        FOREIGN KEY(ccID) REFERENCES creditcards(ccID)
        
);

CREATE TABLE sales(
        sID int NOT NULL AUTO_INCREMENT,
        cid int NOT NULL,
        movieID varchar(10) NOT NULL DEFAULT '',
        saleDate date NOT NULL,
        PRIMARY KEY(sID, cid, movieID),
        FOREIGN KEY(cid) REFERENCES customers(cid),
        FOREIGN KEY(movieID) REFERENCES movies(movieID)
        
);

CREATE TABLE ratings(
	movieID varchar(10) NOT NULL DEFAULT '',
	rating float NOT NULL,
	numVotes int NOT NULL,
    PRIMARY KEY(movieID),
	FOREIGN KEY(movieID) REFERENCES movies(movieID)
);