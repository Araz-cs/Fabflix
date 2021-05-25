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

@WebServlet(name = "SuggestionServlet", urlPatterns = "/api/suggestion")
public class suggestion extends HttpServlet {
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
        String title = request.getParameter("query");
        String tokenizer = "";
        if(!title.isEmpty() || title != null) {
            String[] word = title.split(" ");

            for (int i = 0; i < word.length; i++) {
                tokenizer += "+" + word[i] + "*";

                if (i < word.length - 1)
                    tokenizer += " ";
            }
        }
        title = tokenizer;
        try{
            Connection conn=dataSource.getConnection();
            String first10 = "SELECT movieID,title FROM movies WHERE MATCH(title) AGAINST(? IN BOOLEAN MODE) LIMIT 10;";
            PreparedStatement ps = conn.prepareStatement(first10);
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();
            JsonArray jsonArray = new JsonArray();
            while(rs.next())
            {
                JsonObject info = new JsonObject();
                String mid = rs.getString("movieID");
                info.addProperty("data" , mid);
                String mname = rs.getString("title");
                info.addProperty("value" , mname);
                jsonArray.add(info);
            }
            out.write(jsonArray.toString());
            ps.close();
            response.setStatus(200);
            rs.close();
            conn.close();

        }
        catch (Exception except) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", except.getMessage());
            out.write(jsonObject.toString());
            response.setStatus(500);
            out.close();
        }
    }
}