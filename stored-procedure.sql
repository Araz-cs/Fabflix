use moviedb;

DROP PROCEDURE IF EXISTS add_movie;

DELIMITER $$
CREATE PROCEDURE add_movie(IN addTitle varchar(100), IN addDirector varchar(100), IN addYear integer, IN addGenre varchar(32), IN addStar varchar(100), addMovieID varchar(10), addStarID varchar(10))
BEGIN
    DECLARE idMovie int;
    DECLARE idStar varchar(10);
    DECLARE idGenre int;
    SELECT max(gID) + 1 FROM genres INTO idGenre;
    
    IF((select count(*) from movies where title = addTitle AND director = addDirector AND yearz = addYear) = 0) THEN
        insert into movies(movieID, title, yearz, director) values (addMovieID, addTitle, addYear, addDirector);
		insert into ratings(movieID,rating,numVotes) values (addMovieID,0,0);

    IF((select count(*) from genres where gNames = addGenre) > 0) THEN
            select gID from genres where gNames = addGenre into idGenre;
            insert into genres_in_movies(gID, movieID) values(idGenre, addMovieID);
            
    ELSE
            insert into genres(gID, gNames) values(idGenre, addGenre);
            
            insert into genres_in_movies(gID, movieID) values(idGenre, addMovieID);
            
    END IF;
        
    IF((select count(*) from stars where name = addStar) > 0) THEN
            select id from stars where name = addStar INTO idStar;
            
            insert into stars_in_movies(id, movieId) values(idStar, addMovieID);
    ELSE
            insert into stars(id, name) values(addStarID, addStar);
            
            insert into stars_in_movies(id, movieID) values(addStarID, addMovieID);
    END IF;
    END IF;
    
END
$$
DELIMITER ;

