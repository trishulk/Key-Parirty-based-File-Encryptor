import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class viewservlet extends HttpServlet {

 public void doGet(HttpServletRequest request, HttpServletResponse response)
 throws ServletException, IOException {

  response.setContentType("text/html");
  PrintWriter out = response.getWriter();

  int userid = Validate.userid;
  Connection con = null;

  try {
   con = createdbconn.createconn();
   PreparedStatement ps = con.prepareStatement("select file_id,file_name from file_upload_tbl where userid=?");
   ps.setInt(1, userid);

   ResultSet rs = ps.executeQuery();

   out.print("<table width=20% border=2>");
   out.print("<caption>Filename:</caption>");


   /* Printing column names */



   while (rs.next()) {
    out.print("<tr>");
    out.print("<td> <a href=\"filedisplay?param=" + rs.getInt(1) + "\">");
    out.print(rs.getString(2));
    out.println("</td>");
    out.print("</tr>");
   }


   /* Printing result */


   out.print("</table>");

  } catch (Exception e2) {
   e2.printStackTrace();
  } finally {
   out.close();
  }

 }
}