import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.io.IOException;
import java.sql.*;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;


public class XMLParse {
    Document moviesXML;
    Document actorXML;
    Document starMovieXML;

    private void parseXmlFile() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            moviesXML = documentBuilder.parse("mains243.xml");
            actorXML = documentBuilder.parse("actors63.xml");
            starMovieXML = documentBuilder.parse("casts124.xml");

        } catch (ParserConfigurationException | SAXException | IOException error) {
            error.printStackTrace();
        }
    }

    private void parseGenres(Connection connection) throws SQLException, IOException {
        connection.setAutoCommit(true);
        Element mDoc = moviesXML.getDocumentElement();

        NodeList gList = mDoc.getElementsByTagName("cats");

        for (int i = 0; i < gList.getLength(); i++) {
            try {
                String genre = genreCheck(getTextValue((Element) gList.item(i), "cat"));

                String genreLocation = "SELECT * FROM genres WHERE gNames = ?;";
                PreparedStatement statement = connection.prepareStatement(genreLocation);

                statement.setString(1, genre);
                ResultSet genreRS = statement.executeQuery();

                if (genreRS.next())
                    continue;
                else {
                    System.out.println("Inserting genre: " + genre);
                    String genreInsertion = "INSERT INTO genres(gNames) VALUES(?);";
                    PreparedStatement pstatement = connection.prepareStatement(genreInsertion);
                    pstatement.setString(1, genre);
                    pstatement.execute();
                }
                genreRS.close();
            } catch (Exception genreE) {
            }
        }


    }

    private String genreCheck(String genre) throws SQLException {
        String temp = "None";

        if (genre.equalsIgnoreCase("drama") || genre.equalsIgnoreCase("dram") || genre.equalsIgnoreCase("Dram Docu") || genre.equalsIgnoreCase("dramd") || genre.equalsIgnoreCase("dramn") || genre.equalsIgnoreCase("draam"))
            temp = "Drama";
        else if (genre.equalsIgnoreCase("bio") || genre.equalsIgnoreCase("biog") || genre.equalsIgnoreCase("biop") || genre.equalsIgnoreCase("BioPx") || genre.equalsIgnoreCase("biopp") || genre.equalsIgnoreCase("biob"))
            temp = "Biography";
        else if (genre.equalsIgnoreCase("docu") || genre.equalsIgnoreCase("Docu Dram") || genre.equalsIgnoreCase("duco") || genre.equalsIgnoreCase("ducu") || genre.equalsIgnoreCase("dicu"))
            temp = "Documentary";
        else if (genre.equalsIgnoreCase("stage musical") || genre.equalsIgnoreCase("muscl") || genre.equalsIgnoreCase("musc") || genre.equalsIgnoreCase("Muusc"))
            temp = "Musical";
        else if (genre.equalsIgnoreCase("TV") || genre.equalsIgnoreCase("TVmini") || genre.equalsIgnoreCase("col TV") || genre.equalsIgnoreCase("bnw TV"))
            temp = "TV-show";
        else if (genre.equalsIgnoreCase("s.f.") || genre.equalsIgnoreCase("scifi") || genre.equalsIgnoreCase("sxfi") || genre.equalsIgnoreCase("scif"))
            temp = "Sci-Fi";
        else if (genre.equalsIgnoreCase("Surreal") || genre.equalsIgnoreCase("Surl") || genre.equalsIgnoreCase("Surr"))
            temp = "Surreal";
        else if (genre.equalsIgnoreCase("comdx") || genre.equalsIgnoreCase("comd") || genre.equalsIgnoreCase("cond"))
            temp = "Comedy";
        else if (genre.equalsIgnoreCase("crim") || genre.equalsIgnoreCase("cnrb") || genre.equalsIgnoreCase("cnr"))
            temp = "Crime";
        else if (genre.equalsIgnoreCase("romt") || genre.equalsIgnoreCase("Romt Comd"))
            temp = "Romance";
        else if (genre.equalsIgnoreCase("sports") || genre.equalsIgnoreCase("sport"))
            temp = "Sport";
        else if (genre.equalsIgnoreCase("mystp") || genre.equalsIgnoreCase("myst"))
            temp = "Mystery";
        else if (genre.equalsIgnoreCase("porn") || genre.equalsIgnoreCase("kinky"))
            temp = "Pornography";
        else if (genre.equalsIgnoreCase("advt") || genre.equalsIgnoreCase("Adct"))
            temp = "Adventure";
        else if (genre.equalsIgnoreCase("actn") || genre.equalsIgnoreCase("act"))
            temp = "Action";
        else if (genre.equalsIgnoreCase("horr") || genre.equalsIgnoreCase("Hor"))
            temp = "Horror";
        else if (genre.equalsIgnoreCase("fam") || genre.equalsIgnoreCase("faml"))
            temp = "Family";
        else if (genre.equalsIgnoreCase("west"))
            temp = "Western";
        else if (genre.equalsIgnoreCase("susp"))
            temp = "Thriller";
        else if (genre.equalsIgnoreCase("tvs"))
            temp = "TV-series";
        else if (genre.equalsIgnoreCase("Expm"))
            temp = "Experimental";
        else if (genre.equalsIgnoreCase("epic"))
            temp = "Epic";
        else if (genre.equalsIgnoreCase("noir"))
            temp = "Noir";
        else if (genre.equalsIgnoreCase("hist"))
            temp = "History";
        else if (genre.equalsIgnoreCase("fant"))
            temp = "Fantasy";
        else if (genre.equalsIgnoreCase("cart"))
            temp = "Cartoon";
        else
            temp = "none";
        return temp;
    }


    private String parseActor(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        String retString = "Inconsistencies in actors\n";
        Element documentElement = actorXML.getDocumentElement();
        NodeList nodeList = documentElement.getElementsByTagName("actor");
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                String name = getTextValue((Element) nodeList.item(i), "stagename");
                int dob = getIntValue((Element) nodeList.item(i), "dob");
                String dupStarCheck = "SELECT * FROM stars WHERE name = ? AND birthYear = ?;";
                PreparedStatement dupcheck = connection.prepareStatement(dupStarCheck);
                dupcheck.setString(1, name);
                dupcheck.setInt(2, dob);
                ResultSet rs1 = dupcheck.executeQuery();
                if (rs1.next()) {
                    retString += "Duplicate in actors " + name + +dob + "\n";
                } else {
                    String getMax = "SELECT max(id) as 'maxid' FROM stars;";
                    PreparedStatement maxId = connection.prepareStatement(getMax);
                    ResultSet rs2 = maxId.executeQuery();
                    String maxstarid = "";
                    if (rs2.next()) {
                        maxstarid = rs2.getString("maxid");
                    }
                    int retMax = Integer.parseInt(maxstarid.substring(2)) + 1;
                    String maxret = "nm" + retMax;

                    String insStar = "INSERT INTO stars(id, name, birthYear) VALUES (?, ?, ?)";
                    PreparedStatement insActor = connection.prepareStatement(insStar);
                    insActor.setString(1, maxret);
                    insActor.setString(2, name);
                    if (dob == -99) {
                        System.out.println("Inserting star: (" + maxret + ", " + name + ", " + "NULL)");
                        insActor.setString(3, null);
                    } else {
                        System.out.println("Inserting star: (" + maxret + ", " + name + ", " + dob + ")");
                        insActor.setInt(3, dob);
                    }
                    insActor.execute();
                    retMax++;

                }

            }
            connection.commit();


        }
        return retString;
    }

    private String parseStarMovie(Connection connection) throws SQLException, IOException {
        connection.setAutoCommit(false);

        String retString = "Inconsistencies in casts: \n";

        Element StarinMovies = starMovieXML.getDocumentElement();

        NodeList nodelist = StarinMovies.getElementsByTagName("dirfilms");
        if (nodelist != null) {
            for (int i = 0; i < nodelist.getLength(); i++) {

                if (getTextValue((Element) nodelist.item(i), "is").equals(null)) {
                    continue;

                }
                String directorName = getTextValue((Element) nodelist.item(i), "is");
                Element ele = (Element) nodelist.item(i);
                NodeList filmIDS = ele.getElementsByTagName("m");

                if (filmIDS != null) {
                    for (int z = 0; z < filmIDS.getLength(); z++) {
                        try {
                            String movie = getTextValue((Element) filmIDS.item(z), "t");
                            String star = getTextValue((Element) filmIDS.item(z), "a");
                            if (getTextValue((Element) filmIDS.item(z), "t").equals(null) || getTextValue((Element) filmIDS.item(z), "a").equals(null)) {
                                continue;
                            } else {
                                String movietitle;
                                String starname;
                                String movieindata = "SELECT * FROM movies WHERE title = ? AND director = ?;";
                                PreparedStatement checkMovies = connection.prepareStatement(movieindata);
                                checkMovies.setString(1, movie);
                                checkMovies.setString(2, directorName);
                                ResultSet getMovies = checkMovies.executeQuery();
                                String starindata = "SELECT * FROM stars WHERE name = ?;";
                                PreparedStatement checkStars = connection.prepareStatement(starindata);
                                checkStars.setString(1, star);
                                ResultSet getStars = checkStars.executeQuery();
                                if (getMovies.next() && getStars.next()) {
                                    movietitle = getMovies.getString("movieID");
                                    starname = getStars.getString("id");
                                    ;
                                } else {

                                    continue;
                                }
                                if (!starname.equals(null) && !movietitle.equals(null)) {
                                    String dupchecker = "SELECT count(*) as 'countc' FROM stars_in_movies WHERE  id =? AND movieID =?;";
                                    PreparedStatement snmdup = connection.prepareStatement(dupchecker);
                                    snmdup.setString(1, starname);
                                    snmdup.setString(2, movietitle);
                                    ResultSet oof = snmdup.executeQuery();
                                    int pp = 0;
                                    String temp = "";
                                    if (oof.next()) {
                                        temp = oof.getString("countc");
                                        pp = Integer.parseInt(temp);

                                    }

                                    if (pp < 1) {
                                        String insertVal = "INSERT INTO stars_in_movies VALUES(?, ?);";
                                        PreparedStatement insStarMovie = connection.prepareStatement(insertVal);
                                        System.out.println("Inserting stars_in_movies: (" + starname + ", " + movietitle + ")");
                                        insStarMovie.setString(1, starname);
                                        insStarMovie.setString(2, movietitle);
                                        insStarMovie.executeUpdate();
                                    } else {
                                        continue;
                                    }
                                }
                            }

                        } catch (Exception excecpt) {
                            retString += "Could not parse " + getTextValue((Element) ele.getElementsByTagName("m").item(z), "f") +
                                    "\n";
                        }
                    }

                }
            }
            connection.commit();

        }


        return retString;
    }

    private String parseMovies(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        String retString = "Inconsistencies in mains\n";

        Element documentElement = moviesXML.getDocumentElement();
        NodeList nodeList = documentElement.getElementsByTagName("directorfilms");
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                String directorName = "";

                if (getTextValue((Element) nodeList.item(i), "dirname") != null) {
                    directorName = getTextValue((Element) nodeList.item(i), "dirname");

                } else {
                    retString += "Error with director: " + getTextValue((Element) nodeList.item(i), "dirid") + "\n";
                    continue;
                }

                Element ele = (Element) nodeList.item(i);

                NodeList films = ele.getElementsByTagName("film");
                for (int z = 0; z < films.getLength(); z++) {
                    try {
                        String getMax = "SELECT max(movieID) as 'max' FROM movies;";
                        PreparedStatement movie = connection.prepareStatement(getMax);
                        ResultSet highestmovie = movie.executeQuery();
                        String movieMaxId = "";
                        if (highestmovie.next()) {
                            movieMaxId = highestmovie.getString("max");
                        }
                        int maxIDNum = Integer.parseInt(movieMaxId.substring(2)) + 1;
                        String retmovie = "tt0" + maxIDNum;
                        String movieName = getTextValue((Element) films.item(z), "t");

                        int movieYear = getIntValue((Element) films.item(z), "year");
                        if (movieName == null) {
                            retString += "Invalid tag " + getTextValue((Element) films.item(z), "fid") + "\n";
                            continue;
                        }
                        if (movieYear == -99) {
                            retString += "Invalid tag " + getTextValue((Element) films.item(z), "fid") + "\n";
                        } else {
                            String insertMovie = "INSERT INTO movies(movieID, title, director, yearz) VALUES(?, ?, ?, ?);";
                            PreparedStatement movieinserted = connection.prepareStatement(insertMovie);
                            movieinserted.setString(1, retmovie);
                            movieinserted.setString(2, movieName);
                            movieinserted.setString(3, directorName);
                            movieinserted.setInt(4, movieYear);
                            String insrating = "INSERT INTO ratings(movieID, rating, numVotes) VALUES(?, ?, ?);";
                            PreparedStatement ratingins = connection.prepareStatement(insrating);
                            ratingins.setString(1, retmovie);
                            ratingins.setString(2, "0");
                            ratingins.setString(3, "0");
                            String getMovies = "SELECT * FROM movies WHERE title = ? AND director = ? AND yearz = ?;";
                            PreparedStatement movieChecker = connection.prepareStatement(getMovies);
                            movieChecker.setString(1, movieName);
                            movieChecker.setString(2, directorName);
                            movieChecker.setInt(3, movieYear);
                            Element genreElement = (Element) films.item(z);
                            NodeList genreList = genreElement.getElementsByTagName("cats");
                            String genreName = "";
                            for (int a = 0; a < genreList.getLength(); a++) {
                                if (!getTextValue((Element) genreList.item(a), "cat").equals("null")) {
                                    genreName += genreCheck(getTextValue((Element) genreList.item(a), "cat"));
                                }
                            }
                            ResultSet dupcheck = movieChecker.executeQuery();
                            if (dupcheck.next()) {
                                retString += "Duplicate entry found in mains: " + getTextValue((Element) films.item(z), "fid") + "\n";

                            } else {

                                System.out.println("Inserting movie: (" + retmovie + ", " + movieName + ", " + directorName + ", " + genreName + ", " + movieYear + ")");
                                movieinserted.execute();
                                ratingins.execute();
                                String getIds = "SELECT * FROM genres WHERE gNames = ?;";
                                PreparedStatement getGenID = connection.prepareStatement(getIds);
                                getGenID.setString(1, genreName);
                                ResultSet genreIDs = getGenID.executeQuery();
                                if (genreIDs.next()) {
                                    String curID = genreIDs.getString("gID");
                                    try {
                                        String insertGiM = "INSERT INTO genres_in_movies(gID, movieID) VALUES(?, ?);";
                                        PreparedStatement inserted = connection.prepareStatement(insertGiM);
                                        inserted.setString(1, curID);
                                        inserted.setString(2, retmovie);

                                        inserted.execute();
                                    } catch (Exception ex) {
                                    }
                                }
                                maxIDNum++;
                                genreIDs.close();


                            }
                        }
                    } catch (Exception emovie) {
                    }


                }
            }
            connection.commit();
        }
        return retString;
    }


    private String getTextValue(Element element, String tagName) {
        String textVal = null;
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() > 0) {
            // here we expect only one <Name> would present in the <Employee>
            textVal = nodeList.item(0).getFirstChild().getNodeValue();

        }
        return textVal;
    }

    private int getIntValue(Element element, String tagName) {
        int n = -99; //default value if parse returns nothing
        try {
            n = Integer.parseInt(getTextValue(element, tagName));
        } catch (Exception e) {
            return n;
        }
        return n;
    }

    public static void main(String[] args) throws Exception {

        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

        BufferedWriter writer = new BufferedWriter(new FileWriter("inconsistencies.txt"));

        XMLParse parse = new XMLParse();
        String Errors = "";
        parse.parseXmlFile();
        parse.parseGenres(connection);
        for (String s : Arrays.asList(parse.parseMovies(connection), parse.parseActor(connection), parse.parseStarMovie(connection))) {
            Errors += s;
        }

        writer.write(Errors);
        System.out.println("Finished");
        writer.close();
        connection.close();
    }
}