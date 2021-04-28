package main.java;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;import java.sql.PreparedStatement;

@WebServlet(name = "confirmationPage", urlPatterns = "/api/confirmationPage")
public class confirmationPage extends HttpServlet {
    private static final long serialVersionUID = 2L;

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        try {
            Connection conn = dataSource.getConnection();
            //Statement statement = conn.createStatement();

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dateOBJ = new Date();

            int prevSale = Integer.parseInt("" + session.getAttribute("lastSale"));
            String cID = "" + session.getAttribute("customerID");

            String salesQuery = "SELECT s.sID as id, s.cID, m.title as title, s.saleDate " +
                    "FROM sales s JOIN movies m " +
                    "WHERE s.movieId = m.movieID AND s.sID > ? " +
                    "AND s.cID = ? " +
                    "AND saleDate ='" + df.format(dateOBJ) + "'\n" +
                    "ORDER BY s.sID DESC;";

            PreparedStatement statement=conn.prepareStatement(salesQuery);
            statement.setInt(1,prevSale);
            statement.setString(2,cID);

            ResultSet rs = statement.executeQuery();
            JsonArray jsonArray = new JsonArray();

            while (rs.next()) {
                String title = rs.getString("title");
                String sID = rs.getString("id");

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("title", title);
                jsonObject.addProperty("sID", sID);

                jsonArray.add(jsonObject);
            }

            ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
            previousItems.clear();

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}