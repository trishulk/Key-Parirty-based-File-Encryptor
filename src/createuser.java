import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

/**
 * Servlet implementation class createuser
 */
@WebServlet("/createuser")
public class createuser extends HttpServlet {
 private static final long serialVersionUID = 1L;

 /**
  * @see HttpServlet#HttpServlet()
  */
 public createuser() {
  super();
  // TODO Auto-generated constructor stub
 }



 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  response.setContentType("text/html;charset=UTF-8");
  PrintWriter out = response.getWriter();

  String username = request.getParameter("username");
  String email = request.getParameter("email");
  String pass = request.getParameter("psw");
  String pswrpt = request.getParameter("psw-repeat");
  System.out.println(pass);
  System.out.println(pswrpt);
  if (!pass.equals(pswrpt)) {
   request.setAttribute("message", pass + pswrpt + "Password and Reset Password not working");
   RequestDispatcher rs = request.getRequestDispatcher("createuser.jsp");
   rs.forward(request, response);
  }

  if (Validate.createUser(username, email, pass)) {

    String insert=request.getParameter("userval"); 
   request.setAttribute("message", "New User Created");
   if(insert=="1") {
   RequestDispatcher rs = request.getRequestDispatcher("File.jsp");
   rs.forward(request, response);}
   
   else {
	   RequestDispatcher rs = request.getRequestDispatcher("index.html");
	   rs.forward(request, response);
   }
   
  } else {
   request.setAttribute("message", "New User not created");
   RequestDispatcher rs = request.getRequestDispatcher("File.jsp");
   rs.include(request, response);
  }

 }


}