package main.java;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
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

@WebServlet(name ="mainPage", urlPatterns = "/api/mainPage")
public class mainPage extends HttpServlet {
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
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();


        try
        {
            Connection conn = dataSource.getConnection();

            String query = "SELECT * FROM genres;";

            Statement statement = conn.createStatement();

            ResultSet rs = statement.executeQuery(query);
            JsonArray jsonArray = new JsonArray();

            while (rs.next())
            {
                String gid = rs.getString("gID");
                String gname = rs.getString("gNames");

//                System.out.println(gid +" \ngenre name is "+ gname);
                JsonObject jsonObject = new JsonObject();

                jsonObject.addProperty("gid", gid);
                jsonObject.addProperty("gname", gname);

                jsonArray.add(jsonObject);
            }

            out.write(jsonArray.toString());
            conn.close();
            response.setStatus(200);


        } catch (Exception e) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            response.setStatus(500);
        }
        out.close();
    }
}
