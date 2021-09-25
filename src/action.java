import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.nio.file.Paths;


@WebServlet("/uploadServlet")
@MultipartConfig(maxFileSize = 16177215)
public class action extends HttpServlet {

 protected void doPost(HttpServletRequest request, HttpServletResponse response)
 throws ServletException, IOException {
  response.setContentType("text/html;charset=UTF-8");
  PrintWriter out = response.getWriter();
  String message = null;
  InputStream inputStream = null;
  Connection con = null;
  InputStream is2=null;
  // String file = request.getParameter("fileany");
    
  try {
   con = createdbconn.createconn();
   Part filePart = request.getPart("fileany");
   if (filePart != null) {
    // prints out some information for debugging
    System.out.println(filePart.getName());
    System.out.println(filePart.getSize());
    System.out.println(filePart.getContentType());

    // obtains input stream of the upload file
    inputStream = filePart.getInputStream();
    
    EncAndStore ens = new EncAndStore();
    
    ens.bplain = new byte[(int)filePart.getSize()];
 		  
    //byte[] b = new byte[(int)filePart.getSize()] ;
    inputStream.read(ens.bplain);
    PreparedStatement ps = con.prepareStatement("select file_key from users where userid=?");
    ps.setInt(1,Validate.userid);
    ResultSet rs = ps.executeQuery();
    rs.next();
    ens.keystr=rs.getString(1);
    long startTime=System.nanoTime();
    ens.Encrypt();
    long endTime = System.nanoTime();
    
    long totalTime = endTime - startTime;
    double totalTime2 = totalTime/Math.pow(10, 9);
    System.out.println("#############################");
    System.out.println("Total Time: "+totalTime2);
    System.out.println("#############################");
     is2 = new ByteArrayInputStream(ens.benc);
   }
   //   String pass = request.getParameter("pass");
   
   
   


   PreparedStatement ps = con.prepareStatement("select max(file_id) from file_upload_tbl");
   ResultSet rs = ps.executeQuery();
   int fileid = 1;
   if (rs.next()) {
    fileid = rs.getInt(1) + 1;
   }

  
   
   String sql = "INSERT INTO file_upload_tbl(file_id,userid, file_name, file_content) values (?,?, ?, ?)";
   PreparedStatement statement = con.prepareStatement(sql);

   statement.setInt(1, fileid);
   statement.setInt(2, Validate.userid);
   statement.setString(3, Paths.get(filePart.getSubmittedFileName()).getFileName().toString());

   if (inputStream != null) {
    // fetches input stream of the upload file for the blob column
    //statement.setBlob(4, inputStream);
	  statement.setBlob(4, is2);
   }
   // sends the statement to the database server
   int row = statement.executeUpdate();
   if (row > 0) {
    message = "File uploaded and saved into database";
   }
   request.setAttribute("message", "New File uploaded");
  } catch (SQLException ex) {
   message = "ERROR: " + ex.getMessage();
   request.setAttribute("message", message);
   ex.printStackTrace();
  } finally {
   if (con != null) {
    // closes the database connection
    try {
     con.close();
    } catch (SQLException ex) {
     ex.printStackTrace();
    }
   }
  }


  // forwards to the message page
  getServletContext().getRequestDispatcher("/File.jsp").forward(request, response);
 }
}