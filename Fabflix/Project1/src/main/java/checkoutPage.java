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
import java.util.Date;

@WebServlet(name = "checkoutPage", urlPatterns = "/api/checkoutPage")
public class checkoutPage extends HttpServlet {
    private static final long serialVersionUID = 2L;

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");

        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String cardNum = request.getParameter("cardnum");
        String expDate = request.getParameter("expdate").replace("-", "/");

        PrintWriter out = response.getWriter();
        boolean fn = firstName.isEmpty();
        boolean ls = lastName.isEmpty();
        boolean cn = cardNum.isEmpty();
        boolean ed = expDate.isEmpty();
        if(fn == true && ls == true && cn == true && ed == true)
        {
            out.write("invalid_info");
        }
        else{

            try(Connection conn = dataSource.getConnection()) {

                String card = "";
                String validThru = "";


                Statement creditCardQ = conn.createStatement();
                Statement salesQ = conn.createStatement();

                String saleQ =
                        "SELECT *\n" +
                                "FROM sales\n" +
                                "ORDER BY sID DESC\n" +
                                "LIMIT 1;";

                ResultSet rsSale = salesQ.executeQuery(saleQ);
                System.out.println("sID");
                while(rsSale.next())
                {
                    request.getSession().setAttribute("lastSale", rsSale.getString("sID"));
                }



                String CCardQ = "SELECT * \n" +
                        "FROM creditcards \n" +
                        "WHERE ccID = '" + cardNum + "'\n" +
                        "AND expiration = '" + expDate + "';";

                ResultSet rsCreditCard = creditCardQ.executeQuery(CCardQ);


                while (rsCreditCard.next())
                {
                    card = rsCreditCard.getString("ccID");
                    validThru = rsCreditCard.getString("expiration");
                }

                boolean flag = previousItems.isEmpty();
                boolean cardFlag = card.isEmpty();
                boolean ExpirationFlag = validThru.isEmpty();
                if (flag == true) {
                    out.write("cart_is_empty");
                }
                else
                {
                    if(cardFlag == true && ExpirationFlag == true)
                    {
                        out.write("invalid_info");
                    }
                    else
                    {
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        Date ins = new Date();
                        String newSale = "";
                        String custId = "" + request.getSession().getAttribute("customerID");
                        int i=0;
                        while(i < previousItems.size()-2){
                            int count = Integer.parseInt(previousItems.get(i+2));
                            int j=0;
                            while(j<count){
                                String temp = previousItems.get(i);
                                int hold = Integer.parseInt(custId);
                                newSale = "insert into sales (cid, movieId, saleDate)"
                                        + "values ( '" + hold + "' , '" + temp + "' , '" + df.format(ins) + "')";
                                PreparedStatement insertRow = conn.prepareStatement(newSale);

                                insertRow.executeUpdate();
                                j++;
                            }
                            i+=3;
                        }
                        out.write("order_success");
                    }
                }
                response.setStatus(200);
            } catch (Exception e) {
                out.write(e.getMessage());
                response.setStatus(500);
            }
        }
        out.close();
    }
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    }
}