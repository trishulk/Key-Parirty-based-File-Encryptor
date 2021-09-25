import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class Login extends HttpServlet {

 protected void doPost(HttpServletRequest request, HttpServletResponse response)
 throws ServletException, IOException {
  response.setContentType("text/html;charset=UTF-8");
  PrintWriter out = response.getWriter();

  String username = request.getParameter("user");
  String pass = request.getParameter("pass");
  // Connection con=createdbconn.createconn();

  if (Validate.checkUser(username, pass))

  {



   RequestDispatcher rs = request.getRequestDispatcher("File.jsp");
   rs.forward(request, response);
  } else {
   out.println("Username or Password incorrect");
   RequestDispatcher rs = request.getRequestDispatcher("index.html");
   rs.include(request, response);
  }
 }
}