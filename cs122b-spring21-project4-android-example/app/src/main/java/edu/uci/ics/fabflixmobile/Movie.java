package edu.uci.ics.fabflixmobile;

public class Movie {
    private final String title;
    private final String year;
    private final String id;
    private final String director;

    public Movie(String id,String title, String year,String director) {
        this.title = title;
        this.year = year;
        this.id = id;
        this.director = director;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }
    public String getId() {
        return id;
    }
    public String getDirector() {
        return director;
    }

}