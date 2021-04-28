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
import java.io.PrintWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.ResultSet;

@WebServlet(name = "shoppingCart", urlPatterns = "/api/shoppingCart")
public class shoppingCart extends HttpServlet {
    private static final long serialVersionUID = 2L;

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");


        String movieID = request.getParameter("movieID");
        String title = request.getParameter("title");
        String cmd = request.getParameter("cmd");

        previousItems = calculating(previousItems, movieID, title, cmd, session);


        response.getWriter().write(String.join("-", previousItems));
    }
    public ArrayList<String> calculating(ArrayList<String> previousItems,String movieID,String title,String cmd, HttpSession session ){
        if(!movieID.isEmpty()) {
            if (previousItems == null) {
                previousItems = new ArrayList<>();
                previousItems.add(movieID); // 1
                previousItems.add(title); //2
                previousItems.add("1"); //3
                session.setAttribute("previousItems", previousItems);
            }
            else {
                synchronized (previousItems) {
                    int i = 0;
                    int checker=0;
                    int counter = 0;
                    for(i = 0; i < previousItems.size() - 2 ; i+=3)
                    {
                        if (checker == 1) {
                            break;
                        }
                        if (previousItems.get(i).equals(movieID)) {
                            checker = 1;
                        }
                        counter = i;
                    }
                    if(cmd.equals("add"))
                    {
                        if(checker==1)
                        {
                            int qty=Integer.parseInt(previousItems.get(counter + 2));
                            qty+=1;
                            previousItems.set(counter+2,String.valueOf(qty));
                        }
                        else
                        {
                            previousItems.add(movieID);
                            previousItems.add(title);
                            previousItems.add("1");
                        }
                    }
                    else if(cmd.equals("del"))
                    {
                        if(checker==1)
                        {
                            previousItems.remove(counter);
                            previousItems.remove(counter);
                            previousItems.remove(counter);
                        }
                    }

                    else if(cmd.equals("sub"))
                    {
                        if(checker==1)
                        {
                            int qty = 0;
                            qty = Integer.parseInt(previousItems.get(counter+2));
                            if(qty == 1)
                            {
                                previousItems.remove(counter);
                                previousItems.remove(counter);
                                previousItems.remove(counter);
                            }
                            else
                            {
                                qty-=1;
                                previousItems.set(counter + 2,String.valueOf(qty));
                            }
                        }

                    }

                }
            }
        }
        return previousItems;
    }
}