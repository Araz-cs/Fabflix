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
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "movieList", urlPatterns = "/api/movieList")
public class movieList extends HttpServlet {
    private static final long serialVersionUID = 2L;

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
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json"); // Response mime type

        // Retrieve parameter id from url request.

//        String id = request.getParameter("id");

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource
            Statement statement = conn.createStatement();
            // Construct a query with parameter represented by "?"
            String query = "SELECT m.movieID as mid, m.title as title, m.yearz as year,\n" +
                    "m.director as director,\n" +
                    "substring_index(group_concat(distinct g.gNames SEPARATOR ', '), ', ', 3) as genres,\n" +
                    "substring_index(group_concat(s.id SEPARATOR ','), ',', 3) as sid,\n" +
                    "substring_index(group_concat(s.name SEPARATOR ', '), ', ', 3) as stars ,\n" +
                    "r.rating\n" +
                    "FROM movies m\n" +
                    "LEFT JOIN genres_in_movies gm\n" +
                    "ON gm.movieID = m.movieID \n" +
                    "LEFT JOIN genres g\n" +
                    "ON g.gID = gm.gID\n" +
                    "LEFT JOIN stars_in_movies sm\n" +
                    "ON sm.movieID= m.movieID\n" +
                    "LEFT JOIN stars s\n" +
                    "ON sm.id = s.id \n" +
                    "LEFT JOIN ratings r\n" +
                    "ON r.movieID = m.movieID \n" +
                    "group by m.title\n" +
                    "ORDER BY rating DESC\n" +
                    "limit 0,20 ";

            // Declare our statement
            ResultSet rs = statement.executeQuery(query);

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {

                String mid = rs.getString("mid");
                String mtitle = rs.getString("title");
                int myear = rs.getInt("year");

                String mdirector = rs.getString("director");
                String gname = rs.getString("genres");
                String sid = rs.getString("sid");
                String sname = rs.getString("stars");
                float rating = rs.getFloat("rating");
                // Create a JsonObject based on the data we retrieve from rs

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("mid", mid);
                jsonObject.addProperty("mtitle", mtitle);
                jsonObject.addProperty("myear", myear);
                jsonObject.addProperty("mdirector", mdirector);
                jsonObject.addProperty("gname", gname);
                jsonObject.addProperty("sid", sid);
                jsonObject.addProperty("sname", sname);
                jsonObject.addProperty("rating", rating);

                ArrayList<String> arrSID = new ArrayList<>(Arrays.asList(sid.split(",")));
                ArrayList<String> arrSNary = new ArrayList<>(Arrays.asList(sname.split(",")));

                try {
                    jsonObject.addProperty("0sid", arrSID.get(0));
                    jsonObject.addProperty("1sid", arrSID.get(1));
                    jsonObject.addProperty("2sid", arrSID.get(2));
                }
                catch (Exception ignored)
                {

                }
                try {
                    jsonObject.addProperty("0n", arrSNary.get(0));
                    jsonObject.addProperty("1n", arrSNary.get(1));
                    jsonObject.addProperty("2n", arrSNary.get(2));
                }
                catch (Exception ignored)
                {

                }
                jsonArray.add(jsonObject);
            }

            rs.close();
            statement.close();

            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set reponse status to 500 (Internal Server Error)
            response.setStatus(500);
        }
        out.close();

    }

}

