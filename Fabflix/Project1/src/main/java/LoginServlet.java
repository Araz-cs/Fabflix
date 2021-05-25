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
import org.jasypt.util.password.StrongPasswordEncryptor;


import java.io.*;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public String getServletInfo() {
        return "Servlet connects to MySQL database and displays result of a SELECT";
    }
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {


        PrintWriter out = response.getWriter();

        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);



        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try  {
            // Verify reCAPTCHA
            // check if we in android or web to do recaptcha
            if(!gRecaptchaResponse.equals("android")) {
                try {
                    recaptchaVerifyUtils.verify(gRecaptchaResponse);
                } catch (Exception e) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("recaptchaStatus", "fail");
                    out.write(jsonObject.toString());
                    out.close();
                    return;
                }
            }
            Connection conn = dataSource.getConnection();

            //Employees
            String Equery = "SELECT * FROM employees e WHERE e.email=?";
            PreparedStatement Estatement = conn.prepareStatement(Equery);
            Estatement.setString(1, username);
            ResultSet rse = Estatement.executeQuery();

            //Customers
            String query = "SELECT * FROM customers c WHERE c.email=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();



            JsonObject jsonObject = new JsonObject();
//            System.out.println("00000000000000000000000  "+rs.next() + "  00000000000000000000000000\n");
//            System.out.println("00000000000000000000000  "+rse.next() + "  00000000000000000000000000");
            boolean flag = false;
            if(rs.next())
            {
                String email = rs.getString("email");
                String pass = rs.getString("cPassword");
                jsonObject.addProperty("recaptchaStatus", "success");
                flag = new StrongPasswordEncryptor().checkPassword(password, pass);

                if (username.equals(email) && flag) {
                    String id = rs.getString("cid");
                    request.getSession().setAttribute("user", new User(username));
                    request.getSession().setAttribute("customerID", id);
                    request.getSession().setAttribute("userRole", "customer");

                  System.out.println("User" + request.getSession().getAttribute("user"));
                    jsonObject.addProperty("status", "success");
                    jsonObject.addProperty("message", "success");
                }
                else{
                    jsonObject.addProperty("status", "fail");

                    if (username.equals(email))
                        jsonObject.addProperty("message", "Wrong password, please try again.");

                    else
                        jsonObject.addProperty("message", "User " + username + " does not exist.");
                }
            }
            else if(rse.next())
            {
                String email = rse.getString("email");
                String pass = rse.getString("password");
                jsonObject.addProperty("recaptchaStatus", "success");
                flag = new StrongPasswordEncryptor().checkPassword(password, pass);

                if (username.equals(email) && flag) {
                    String id = null;
                    request.getSession().setAttribute("user", new User(username));
                    request.getSession().setAttribute("customerID", id);
                    request.getSession().setAttribute("userRole", "employee");

                    System.out.println("User" + request.getSession().getAttribute("user"));
                    jsonObject.addProperty("status", "success");
                    jsonObject.addProperty("message", "success");
                }
                else{
                    jsonObject.addProperty("status", "fail");

                    if (username.equals(email))
                        jsonObject.addProperty("message", "Wrong password, please try again.");

                    else
                        jsonObject.addProperty("message", "User " + username + " does not exist.");
                }
            }
            else
            {
                //JsonObject responseJsonObject = new JsonObject();
                jsonObject.addProperty("status", "fail");
                jsonObject.addProperty("message", "User " + username + " is not right");
            }
            response.getWriter().write(jsonObject.toString());

        }
//        catch (SQLException e) {
//            // write error message JSON object to output
////            JsonObject jsonObject = new JsonObject();
////            jsonObject.addProperty("errorMessage", e.getMessage());
//            e.printStackTrace();
//
//            // set response status to 500 (Internal Server Error)
//
//        }
        catch (Exception e){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
        }

    }
}