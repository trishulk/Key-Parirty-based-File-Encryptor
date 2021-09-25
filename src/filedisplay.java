import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet that retrieves a file from MySQL database and lets the client
 * downloads the file.
 * @author www.codejava.net
 */
@WebServlet("/filedisplay")
public class filedisplay extends HttpServlet {

 // size of byte buffer to send file
 private static final int BUFFER_SIZE = 4096;


 protected void doGet(HttpServletRequest request,
  HttpServletResponse response) throws ServletException, IOException {
  // get upload id from URL's parameters
  int fileid = Integer.parseInt(request.getParameter("param"));

  Connection conn = null; // connection to the database

  try {
   conn = createdbconn.createconn();
   PreparedStatement ps = conn.prepareStatement("select file_id,file_name,file_content from file_upload_tbl where file_id=?");
   ps.setInt(1, fileid);

   ResultSet rs = ps.executeQuery();


   if (rs.next()) {
    // gets file name and file blob data
    String fileName = rs.getString("file_name");
    Blob blob = rs.getBlob("file_content");
    InputStream inputStream = blob.getBinaryStream();
    
     EncAndStore ens = new EncAndStore();
    
     ens.benc = new byte[(int)blob.length()];
 		  
    //byte[] b = new byte[(int)filePart.getSize()] ;
    inputStream.read(ens.benc);
    ps = conn.prepareStatement("select file_key from users where userid=?");
    ps.setInt(1,Validate.userid);
    
     rs = ps.executeQuery();
    rs.next();
    ens.keystr=rs.getString(1);
    long startTime = System.nanoTime();
    ens.Decrypt();
    long endTime  = System.nanoTime();
    
    double totalTime = endTime -startTime;
    totalTime = totalTime/Math.pow(10, 9);
    
    System.out.println("##########################");
    System.out.println("DECRYPTION TIME: "+totalTime);
    System.out.println("##########################");
    InputStream is2 ;
    
    is2 = new ByteArrayInputStream(ens.bplain);
    
    long fileLength = blob.length();

    System.out.println("fileLength = " + fileLength);

    ServletContext context = getServletContext();

    // sets MIME type for the file download
    String mimeType = context.getMimeType(fileName);
    if (mimeType == null) {
     mimeType = "application/octet-stream";
    }

    // set content properties and header attributes for the response
    response.setContentType(mimeType);
    response.setContentLength((int) fileLength);
    String headerKey = "Content-Disposition";
    String headerValue = String.format("attachment; filename=\"%s\"", fileName);
    response.setHeader(headerKey, headerValue);

    // writes the file to the client
    OutputStream outStream = response.getOutputStream();

    byte[] buffer = new byte[BUFFER_SIZE];
    int bytesRead = -1;

    while ((bytesRead = is2.read(buffer)) != -1) {
     outStream.write(buffer, 0, bytesRead);
    }

    is2.close();
    inputStream.close();
    outStream.close();
   } else {
    // no file found
    response.getWriter().print("File not found for the id: " + fileid);
   }
  } catch (SQLException ex) {
   ex.printStackTrace();
   response.getWriter().print("SQL Error: " + ex.getMessage());
  } catch (IOException ex) {
   ex.printStackTrace();
   response.getWriter().print("IO Error: " + ex.getMessage());
  } finally {
   if (conn != null) {
    // closes the database connection
    try {
     conn.close();
    } catch (SQLException ex) {
     ex.printStackTrace();
    }
   }
  }
 }
}