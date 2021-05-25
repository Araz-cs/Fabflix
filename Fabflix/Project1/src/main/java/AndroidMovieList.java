
package main.java;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(name = "AndroidMovieList", urlPatterns = "/api/AndroidMovieList")
public class AndroidMovieList extends HttpServlet {
    private static final long serialVersionUID = 2L;

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        String titles = request.getParameter("query");
        int page = Integer.parseInt(request.getParameter("page"));

        String tokenizer ="";

        if(!titles.isEmpty() || titles != null) {
            String[] word = titles.split(" ");
            int i =0;
            while (i < word.length) {
                tokenizer += "+" + word[i] + "*";

                if (i < word.length - 1)
                    tokenizer += " ";
                i++;
            }
        }
        titles = tokenizer;

        try {
            Connection conn = dataSource.getConnection();

            String query = "SELECT * FROM movies WHERE MATCH(title) AGAINST(? IN BOOLEAN MODE)"+
                    " ORDER BY movieID DESC "+
                    "LIMIT 20 OFFSET " + page * 20 + ";";

            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, titles);
//            statement.setInt(2, page * 20);


            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();
            while(rs.next())
            {
                JsonObject jsonObject = new JsonObject();

                String mid = rs.getString("movieID");
                jsonObject.addProperty("mid", mid);

                String title = rs.getString("title");
                jsonObject.addProperty("title", title);

                String year = rs.getString("yearz");
                jsonObject.addProperty("year", year);

                String director = rs.getString("director");
                jsonObject.addProperty("director", director);

                jsonArray.add(jsonObject);
            }

            out.write(jsonArray.toString());
            conn.close();
            rs.close();
            statement.close();

            out.close();
        } catch (Exception e) {
            out.write(e.getMessage());
            out.close();
        }
    }

}