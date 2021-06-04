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
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "Dashboard", urlPatterns = "/api/Dashboard")
public class Dashboard extends HttpServlet {
    private static final long serialVersionUID = 2L;

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/masterdb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            Connection conn = dataSource.getConnection();
            HttpSession session = request.getSession();
            //Statement statement= conn.createStatement();
            String query = "show full tables where Table_Type = 'BASE TABLE';";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            JsonArray arr = new JsonArray();
            while (rs.next()) {
                String table = rs.getString("Tables_in_moviedb");
                JsonObject jobject = new JsonObject();
                jobject.addProperty("table_name", table);
                String query1 = "show columns from " + table;
                Statement columns = conn.createStatement();
                ResultSet rs1 = columns.executeQuery(query1);
                String result = "";
                while (rs1.next()) {
                    String data = rs1.getString("Field");
                    String datatype = rs1.getString(("Type"));
                    result += data + " - " + datatype + ", ";

                }
                jobject.addProperty("result", result);
                arr.add(jobject);

            }
            JsonObject object = new JsonObject();
            object.addProperty("userRole", "" + session.getAttribute("userRole"));
            arr.add(object);

            out.write(arr.toString());
            response.setStatus(200);

            rs.close();
            conn.close();
        } catch (Exception exception) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", exception.getMessage());
            out.write(jsonObject.toString());

            response.setStatus(500);
        }
        out.close();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        System.out.println("hello");
        String star = request.getParameter("star");
        int birthyear = 0;
        if(request.getParameter("birthyear") != null){
            birthyear = Integer.parseInt(request.getParameter("birthyear"));
        }
        String title = request.getParameter("title");
        int year = 0;
        if(request.getParameter("year") != null){
            year = Integer.parseInt(request.getParameter("year"));
        }
        String director = request.getParameter("director");
        String getAction = request.getParameter("action");

        String genre = request.getParameter("genre");
        try {
            Connection conn = dataSource.getConnection();
            String maxq = "select max(id) as 'max' from stars";
            PreparedStatement p1 = conn.prepareStatement(maxq);
            ResultSet rs1 = p1.executeQuery();
            JsonObject rObject = new JsonObject();
            System.out.println("action " + getAction);
            System.out.println(rs1);

            if (getAction.equals("addStar")) {
                System.out.println("Max");
                if (star != null) {
                    if (rs1.next()) {
                        int maxStarIdNum = Integer.parseInt(rs1.getString("max").substring(2)) + 1;
                        if (birthyear > 0) {
                            String insStar = "insert into stars (id, name, birthYear) values(?, ?, ?)";
                            PreparedStatement newS = conn.prepareStatement(insStar);
                            newS.setString(1, "nm" + maxStarIdNum);
                            newS.setString(2, star);
                            newS.setInt(3, birthyear);
                            newS.executeUpdate();
                        } else {
                            String insbStar = "insert into stars (id, name, birthYear) values(?, ?, NULL)";
                            PreparedStatement newS2 = conn.prepareStatement(insbStar);
                            newS2.setString(1, "nm" + maxStarIdNum);
                            newS2.setString(2, star);
                            newS2.executeUpdate();
                        }
                        rObject.addProperty("status", "success");
                        rObject.addProperty("message", "nm" + maxStarIdNum);
                    }

                }
            }
            else if (getAction.equals("addMovie"))
            {
                if ((!title.equals("") && title != null) && year > 0 && (!director.equals("") && director != null) && (!star.equals("") && star != null) && (!genre.equals("") && genre != null)) {
                    String dup = "select * from movies where title = ? and yearz = ? and director = ?";
                    PreparedStatement p2= conn.prepareStatement(dup);
                    p2.setString(1, title);
                    p2.setInt(2, year);
                    p2.setString(3, director);
                    ResultSet catchdup = p2.executeQuery();
                    if(catchdup.next()) {
                        rObject.addProperty("status", "fail");
                        rObject.addProperty("message", "duplicate movie");
                    }
                    else
                    {
                        String maxMovie = "SELECT max(movieID) as 'maxm' FROM movies;";
                        PreparedStatement maxMovieIdStatement = conn.prepareStatement(maxMovie);
                        ResultSet rs2 = maxMovieIdStatement.executeQuery();
                        String Starid = "nm";
                        if (rs1.next())
                        {
                            int maxStarIdNum = Integer.parseInt(rs1.getString("max").substring(2));
                            maxStarIdNum+=1;
                            Starid += "" + maxStarIdNum;
                        }
                        if(rs2.next())
                        {
                            int maxMovieId = Integer.parseInt(rs2.getString("maxm").substring(3));
                            maxMovieId+=1;
                            System.out.print(Starid);
                            System.out.print(maxMovieId);

                            String MovieId = "tt0" + maxMovieId;
                            String insertMovie = "CALL add_movie(?, ?, ? ,? ,? ,? ,?);";
                            PreparedStatement ins = conn.prepareStatement(insertMovie);
                            ins.setString(1, title);
                            ins.setString(2, director);
                            ins.setInt(3, year);
                            ins.setString(4, genre);
                            ins.setString(5, star);
                            ins.setString(6, MovieId);
                            ins.setString(7, Starid);
                            //ResultSet finale=ins.executeQuery();
                            ins.executeQuery();
                            System.out.println("before check");
                            String check = "SELECT m.movieID, g.gID, s.id \n" +
                                    "FROM movies as m, genres as g, stars as s,genres_in_movies as gm, stars_in_movies as sm \n" +
                                    "where m.title = ? and m.yearz = ? \n" +
                                    "and m.director = ? and m.movieID = gm.movieID \n" +
                                    "and g.gNames = ? and m.movieID = sm.movieID and s.name = ?";

                            PreparedStatement finale = conn.prepareStatement(check);
                            finale.setString(1, title);
                            finale.setInt(2, year);
                            finale.setString(3, director);

                            finale.setString(4, genre);
                            finale.setString(5, star);
                            ResultSet rs3 = finale.executeQuery();
                            System.out.println("after check");
                            if(rs3.next()){
                                String mid = rs3.getString("m.movieID");
                                int gid = rs3.getInt("g.GID");
                                String sid = rs3.getString("s.id");
                                rObject.addProperty("status", "success");
                                rObject.addProperty("message", "movie id = " + mid + " genre id = " + gid + " star id = " + sid);
                            }

                        }
                    }

                }
                else
                {
                    rObject.addProperty("status", "fail");
                    rObject.addProperty("message", "Must enter required fields");
                }
            }
            response.getWriter().write(rObject.toString());
            conn.close();

        }
        catch (Exception e) {
            out.write(e.getMessage());
            response.setStatus(500);
        }
        out.close();
    }
}