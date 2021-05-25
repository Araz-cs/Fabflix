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
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        String title = request.getParameter("title");
        String year = request.getParameter("year");
        String page = request.getParameter("page");
        String director = request.getParameter("director");
        String star = request.getParameter("star");
        String genre = request.getParameter("genre");
        String countStr = request.getParameter("count");
        String s1 = sortOrder(request.getParameter("s1"));
        String s2 = sortOrder(request.getParameter("s2"));

        String curURL = "movieList.html?title=" + title + "&director=" +
                director + "&star" + star + "&genre=" +
                genre + "&year=" + year + "&page="  + page + "&count=" +
                countStr + "&s1=" + request.getParameter("s1") +
                "&s2=" + request.getParameter("s2");

        session.setAttribute("curURL", curURL);

        try (Connection conn = dataSource.getConnection())
        {
            String TQ = "";
            String tokenizer = "";
            if(year.isEmpty())
            {
                year = "%%";
            }

            if(title.equals("") || title.isEmpty()) {
                title = "%%";
                TQ = "`title` LIKE ?";
            }
            else if(title.indexOf('<') != -1) {
                title = title.replace('<', '%');
                TQ = "`title` LIKE ?";
            }
            else if (title.equals("@")) {
                TQ = "`title` regexp '^[^a-z0-9A-Z]'";
            }
            else {


                String[] word = title.split(" ");
                int i = 0;
                while ( i < word.length) {
                    tokenizer += "+" + word[i] + "*";

                    if (i < word.length - 1)
                        tokenizer += " ";
                    i++;
                }

                title = tokenizer;
                TQ = "MATCH(`title`) AGAINST( ? IN BOOLEAN MODE)";
            }

            int offset = (Integer.parseInt(page) - 1) * Integer.parseInt(countStr);
            String query = "SELECT *, found_rows() AS 'count' " +
                    "FROM `movielist`" +
                    "WHERE `genre` LIKE ? " +
                    "AND " + TQ +" " +
                    "AND `director` LIKE ? " +
                    "AND `star` LIKE ? " +
                    "AND `yearz` LIKE ? " +
                    "ORDER BY " + s1 +  ", " + s2 + " " +
                    "LIMIT " + Integer.parseInt(countStr) +" " +
                    "OFFSET " + offset;
//            System.out.println("offset:"+offset);
            PreparedStatement statement = conn.prepareStatement(query);


            if(title.equals("@"))
            {
                statement.setString(1, "%" + genre + "%");
                statement.setString(2, "%" + director + "%");
                statement.setString(3, "%" + star + "%");
                statement.setString(4,  year );
            }
            else
            {
                statement.setString(1, "%" + genre + "%");
                statement.setString( 2, title);
                statement.setString(3 , "%" + director + "%");
                statement.setString(4 , "%" + star + "%");
                statement.setString(5 ,  year );
            }

            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            int flag = 1;

            // Iterate through each row of rs
            while (rs.next())
            {

                String CountQ = "";
                String mid = rs.getString("movieID");
                String mtitle = rs.getString("title");
                int myear = rs.getInt("yearz");
                String mdirector = rs.getString("director");
                String gname = rs.getString("genre");
                String sid = rs.getString("sid");
                String sname = rs.getString("star");
                float rating = rs.getFloat("rating");

                JsonObject jsonObject = new JsonObject();

                jsonObject.addProperty("mid", mid);
                jsonObject.addProperty("mtitle", mtitle);
                jsonObject.addProperty("myear", myear);
                jsonObject.addProperty("mdirector", mdirector);
                jsonObject.addProperty("gname", gname);
                jsonObject.addProperty("sid", sid);
                jsonObject.addProperty("sname", sname);
                jsonObject.addProperty("rating", rating);

                if (flag == 1)
                {

                    CountQ = rs.getString("count");
                    JsonObject countObj = new JsonObject();
                    countObj.addProperty("CountQ", CountQ);
                    jsonArray.add(countObj);
                    flag = 0;
                }

                jsonArray.add(jsonObject);
            }

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

            // set reponse status to 500 (Internal Server Error)
            response.setStatus(500);
        }
        out.close();
    }
    public String sortOrder(String sortOrder)
    {
        if(sortOrder.equals("rASC"))
            sortOrder = "rating ASC";
        else if(sortOrder.equals("tASC"))
            sortOrder = "title ASC";
        else if(sortOrder.equals("rDESC"))
            sortOrder = "rating DESC";
        else if(sortOrder.equals("tDESC"))
            sortOrder = "title DESC";
        return sortOrder;
    }

}

