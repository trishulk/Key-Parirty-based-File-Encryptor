import java.util.Random;
import java.security.SecureRandom;
import java.math.*;
public class pseudorandom {

 public String randomnum(String email) {
  SecureRandom random = new SecureRandom();
  byte bytes[] = new byte[20];
  byte bytes1[] = new byte[20];
  String teststr = "";
  String salt = "";
  random.nextBytes(bytes);
  if (email.length() < 40) {
   for (int j = 0; j < (40 - email.length()); j++) {
    teststr = teststr + "a";

   }

   email = email + teststr;
   System.out.println(email);
  }

  for (int i = 0; i < 20; i++) {

   bytes1[i] = (byte) Math.abs((byte)(bytes[i] ^ email.charAt(i)));
   System.out.println(bytes1[i]);
   salt = salt + bytes1[i];
  }
  return salt;
 }

 public int[] getbyte(char c) {
  int[] arraybyte = new int[8];
  for (int i = 0; i < 8; ++i) {

   arraybyte[i] = ((c & 1 << i) >> i);
   System.out.println(arraybyte[i]);
  }
  return arraybyte;
 }

 public String hash(String message, String salt) {
  String messg = message;
  String teststr = "";

  if (messg.length() < 40) {
   for (int j = 0; j < (40 - messg.length()); j++) {
    teststr = teststr + "a";

   }

   messg = messg + teststr;
   System.out.println(messg);
  }
  /*for password*/
  int[] bytestore = new int[8];
  int[][] pwd = new int[40][8];
  int k = 0;
  for (int i = 0; i < 40; i++) {
   bytestore = getbyte(messg.charAt(i));
   for (int j = 0; j < 8; j++) {
    pwd[k][j] = bytestore[j];
   }
   k++;


  }
  /*for salt*/
  int[][] salt1 = new int[40][8];
  int l = 0;
  for (int i = 0; i < 40; i++) {
   bytestore = getbyte(salt.charAt(i));
   for (int j = 0; j < 8; j++) {
    salt1[l][j] = bytestore[j];
   }
   l++;


  }


  for (int m = 0; m < 40; m++) {
   for (int l1 = 0; l1 < 8; l1++) {
    System.out.print(pwd[m][l1]);

   }
   System.out.println("" + m);
  }


  for (int m = 0; m < 40; m++) {
   for (int l1 = 0; l1 < 8; l1++) {
    System.out.print(salt1[m][l1]);

   }
   System.out.println("" + m);
  }

  int matrixstr[][] = new int[40][8];
  for (int i = 0; i < 40; i++) {
   for (int j = 0; j < 8; j++) {

    matrixstr[i][j] = (pwd[i][j] ^ salt1[i][j]);
    //System.out.print(matrixstr[i][j]);
   }
   //System.out.println();
  }

  String s = "";
  String test1 = "";

  for (int i = 0; i < 40; i++) {
   for (int j = 0; j < 8; j++) {

    s = s + matrixstr[i][j];


   }
   System.out.println(s);
   //conv(s);
   test1 = test1 + conv(s);
   s = "";
  }

  return test1;

 }

 public char conv(String test) {
  int i = Integer.parseInt(test, 2);
  System.out.println(i);
  char c = (char) i;
  return c;
 }
 public static void main(String args[]) {
  pseudorandom rand = new pseudorandom();
  String rand1 = rand.randomnum("test@123");
  System.out.println(rand.hash("bbbbbbbb", rand1).length());

 }
}