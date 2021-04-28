package main.java;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "singleMovie", urlPatterns = "/api/singleMovie")
public class singleMovie extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        HttpSession session = request.getSession();
        // Retrieve parameter id from url request.
        String id = request.getParameter("id");
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        String curURL= session.getAttribute("curURL") + "";

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource

            // Construct a query with parameter represented by "?"
            String query = "SELECT a.movieID as mID, a.title as Title, a.yearz as Year, a.director as Director,\n" +
                    "GROUP_CONCAT(DISTINCT g.gNames ORDER BY g.gNames ASC SEPARATOR ', ') as Genres,\n" +
                    "GROUP_CONCAT(distinct ss.id SEPARATOR ', ') as smID,\n" +
                    "GROUP_CONCAT(distinct sc.name SEPARATOR ', ') as Stars ,\n" +
                    "FORMAT(r.rating, 1) as Rating, \n" +
                    "sc.count as count\n" +
                    "FROM movies a,genres g,ratings r,stars_in_movies ss, genres_in_movies gm\n" +
                    "join count_Stars_in_movies sc\n" +
                    "WHERE a.movieID = r.movieID AND a.movieID = ss.movieID AND a.movieID = gm.movieID\n" +
                    "AND ss.id = sc.id\n" +
                    "AND gm.gID=g.gID \n" +
                    "and a.movieID = ?\n" +
                    "group by ss.id\n" +
                    "ORDER BY count DESC, name ASC";

            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            statement.setString(1, id);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            JsonObject newObject = new JsonObject();
            newObject.addProperty("curURL", curURL);
            jsonArray.add(newObject);

            // Iterate through each row of rs
            while (rs.next()) {

                String mID = rs.getString("mID");

                String title = rs.getString("Title");
                String year = rs.getString("Year");
                String director = rs.getString("Director");
                String smID = rs.getString("smID");

                String genres = rs.getString("Genres");
                String stars = rs.getString("Stars");
                String rating = rs.getString("Rating");
                // Create a JsonObject based on the data we retrieve from rs

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("mID", mID);
                jsonObject.addProperty("title", title);
                jsonObject.addProperty("year", year);
                jsonObject.addProperty("director", director);
                jsonObject.addProperty("smID", smID);
                jsonObject.addProperty("genres", genres);
                jsonObject.addProperty("stars", stars);
                jsonObject.addProperty("rating", rating);

                String [] arrOfStr = smID.split(",");

                for(String s : arrOfStr)
                {
                    jsonObject.addProperty(smID , s);
                }

                jsonArray.add(jsonObject);
            }
            rs.close();
            statement.close();
            conn.close();
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);


        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }

        // always remember to close db connection after usage. Here it's done by try-with-resources


    }

}
