import java.util.Date;
import java.util.Random;
import java.sql.*;
public class Validate {


 static int userid;


 public static boolean checkUser(String username, String pass) {
  boolean st = false;
  Connection con = null;
  try {

   con = createdbconn.createconn();

   PreparedStatement ps = con.prepareStatement("select userid,email from users where username=?");
   ps.setString(1, username);
   ResultSet rs = ps.executeQuery();
   st = rs.next();
   userid = rs.getInt(1);
   String email = rs.getString(2);
   st = false;
   ps = con.prepareStatement("select salt from pwd_file where userid=?");
   ps.setInt(1, userid);
   rs = ps.executeQuery();
   st = rs.next();
   pseudorandom rand = new pseudorandom();
   String saltval = rs.getString(1);

   st = false;
   String hashpwd = rand.hash(pass, saltval);

   ps = con.prepareStatement("select * from pwd_file where userid=? and pwd=?");
   ps.setInt(1, userid);
   ps.setString(2, hashpwd);
   rs = ps.executeQuery();
   st = rs.next();

  } catch (Exception e) {
   e.printStackTrace();
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
  return st;
 }

 public static boolean createUser(String username, String email, String pass) {
  boolean st = false;
  int id = 0;
  Connection con = null;
  try {

   con = createdbconn.createconn();

   PreparedStatement ps = con.prepareStatement("select max(userid) from users");
   ResultSet rs = ps.executeQuery();
   if (rs.next()) {
    id = rs.getInt(1) + 1;
   }
   
   //Tri code-------------------------
   Random rnd = new Random();
   String bins="01";
   String keystr="";
   for(int j=0; j<64;j++)
   {
   	keystr = keystr+bins.charAt(rnd.nextInt(2));
   }
   //---------------------------------
   PreparedStatement qs = con.prepareStatement("insert into users(userid,username,email,file_key) values(?,?,?,?)");
   qs.setInt(1, id);
   qs.setString(2, username);
   qs.setString(3, email);
   qs.setString(4, keystr);
   ResultSet res = qs.executeQuery();
   st = res.next();
   Date d = new Date();
   pseudorandom rand = new pseudorandom();
   String rand1 = rand.randomnum(email);
   String hashpwd = rand.hash(pass, rand1);
   System.out.println("the length" + hashpwd.length());
   System.out.println("the pwd" + hashpwd);
   qs = con.prepareStatement("insert into pwd_file(userid,pwd,salt,time) values(?,?,?,?)");
   qs.setInt(1, id);
   qs.setString(2, hashpwd);
   qs.setString(3, rand1);
   qs.setString(4, d.toString());
   res = qs.executeQuery();
   st = res.next();

  } catch (Exception e) {
   e.printStackTrace();
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
  return st;
 }
}