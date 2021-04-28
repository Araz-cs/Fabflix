package main.java;

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
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
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
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {



        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try  {

            Connection conn = dataSource.getConnection();

            //String query = "SELECT * FROM Customers c WHERE c.email="+username+" AND c.cPassword="+password ;
            String query = "SELECT * FROM Customers c WHERE c.email=? AND c.cPassword=?";

            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();
            JsonObject jsonObject = new JsonObject();
            if(rs.next())
            {
                String id = rs.getString("cid");
                request.getSession().setAttribute("user", new User(username));
                request.getSession().setAttribute("customerID", id);
//                System.out.println("User" + request.getSession().getAttribute("user"));
                jsonObject.addProperty("status","success");
                jsonObject.addProperty("message","success");
            }
            else
            {
                //JsonObject responseJsonObject = new JsonObject();
                jsonObject.addProperty("status", "fail");
                jsonObject.addProperty("message", "user" + username + "is not right");
            }
            response.getWriter().write(jsonObject.toString());

        }
        catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());


            // set response status to 500 (Internal Server Error)

        }

    }
}

