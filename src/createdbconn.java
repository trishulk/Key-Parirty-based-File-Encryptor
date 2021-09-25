import java.sql.*;
public class createdbconn {
 public static Connection createconn() {
  Connection con = null;
  try {

   //loading drivers for oracle
   Class.forName("oracle.jdbc.driver.OracleDriver");

   //creating connection with the database 
   con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "ssuser", "ss");

  } catch (Exception e) {
   e.printStackTrace();
  }
  return con;
 }



}