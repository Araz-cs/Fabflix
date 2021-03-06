DROP DATABASE IF EXISTS moviedb;
CREATE DATABASE moviedb;
USE moviedb;

DROP TABLE IF EXISTS stars_in_movies;
DROP TABLE IF EXISTS genres_in_movies;
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS ratings;

DROP TABLE IF EXISTS movies;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS stars;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS creditcards;


CREATE TABLE employees
(
	email varchar(50) PRIMARY KEY,
    password varchar(20) NOT NULL,
    fullname varchar(100)
);

CREATE TABLE movies(
        movieID  varchar(10) NOT NULL DEFAULT '',
        title varchar(100) NOT NULL DEFAULT '',
        yearz int(4) NOT NULL ,
        director varchar(100) NOT NULL DEFAULT '',
        PRIMARY KEY(movieID),
        FULLTEXT(title)
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



DROP VIEW IF EXISTS moviegenre;
CREATE VIEW moviegenre AS
    select gm.movieID, GROUP_CONCAT(distinct g.gNames ORDER BY g.gNames SEPARATOR ', ' ) as genre
    from genres_in_movies as gm LEFT JOIN genres as g
    on gm.gID = g.gID
    GROUP BY gm.movieID;

DROP VIEW IF EXISTS moviestar;
CREATE VIEW moviestar AS
    SELECT sm.movieID, GROUP_CONCAT(sm.id SEPARATOR ", " ) as sid, GROUP_CONCAT(s.name SEPARATOR ", " ) as star
    FROM stars_in_movies as sm LEFT JOIN stars as s
    on sm.id = s.id
    GROUP BY sm.movieID;

DROP VIEW IF EXISTS movielist;
CREATE VIEW movielist AS
    SELECT m.movieID, m.title, m.director, g.genre, m.yearz,  FORMAT(IFNULL(r.rating, 0),1) as rating, s.sid, s.star
    FROM movies as m 
    LEFT JOIN ratings as r
    ON m.movieID = r.movieID
    LEFT JOIN moviegenre as g 
    ON m.movieID = g.movieID
    LEFT JOIN moviestar as s
    ON s.movieID = m.movieID;
    
-- DROP VIEW IF EXISTS movielist;
-- CREATE VIEW movielist AS
-- SELECT m.movieID , m.title, m.director, GROUP_CONCAT(distinct g.gNames ORDER BY g.gNames SEPARATOR ', ' ) as genre , m.yearz, FORMAT(IFNULL(rating, 0), 1) as rating,
--     GROUP_CONCAT( sm.id SEPARATOR ", " ) as sid, GROUP_CONCAT( s.name SEPARATOR ", " ) as star
--     FROM movies as m, genres_in_movies as gm, stars_in_movies as sm 
--     INNER JOIN ratings as r
-- 	INNER JOIN genres as g
--     INNER JOIN stars as s
-- 	WHERE r.movieID = gm.movieID AND sm.movieID = r.movieID
--     AND gm.gID = g.gID AND sm.id = s.id AND  m.movieID = r.movieId
--     group by movieID;
--     
    
DROP VIEW IF EXISTS count_Stars_in_movies;
CREATE VIEW count_Stars_in_movies AS
SELECT s.name as name, s.id as id, COUNT(sim.id) as count
FROM stars AS s JOIN stars_in_movies AS sim
WHERE s.id = sim.id
GROUP BY s.id
ORDER BY COUNT(sim.id) DESC, s.name ASC;



CREATE INDEX starN ON stars(name);
CREATE INDEX moviesN ON movies(title, director);