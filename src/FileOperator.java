

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;


class ConverterThread extends Thread
{
	byte[] b;
	String bstr="";
	int start,end;
	ConverterThread(byte[] b, int start, int end)
	{
		this.b = b;
		this.start = start;
		this.end =end;
	}
	
	public void run()
	{
		String s1;
		bstr="";
		for(int i=start ; i<end; i++)
		{
			s1 = String.format("%8s", Integer.toBinaryString(b[i] & 0xFF)).replace(' ', '0');
		    bstr = bstr+s1;
		}
	}
}

class FileOperator {
   
	String getBitString(String path)
	{
		
	  //System.out.println("Getting String of Bits From File...");
	   int i=0;
	   //byte[] b = new byte[8];
	   String bstr="",s1;
	   try{
		   
		   FileInputStream fin = new FileInputStream(path); 
		   File file = new File(path);
		   int fileSize = (int)file.length();
		   System.out.println("File Size: "+fileSize);
		   byte[] b = new byte[fileSize];
		   
		   fin.read(b);
		   fin.close();
		   
		   System.out.println("No of Bytes To be Read: "+b.length);
		   
		   int num = 10;
		   ConverterThread[] threadList = new ConverterThread[num];
		   int tlen = b.length/num;
		   for(i=0;i<num-1;i++)
		   {
			  threadList[i]= new ConverterThread(b,i*tlen,i*tlen+tlen);
			  threadList[i].start();
		   }
		   
		   threadList[num-1] = new ConverterThread(b,(num-1)*tlen,b.length);
		   threadList[num-1].start();
		   
		   /*for(byte ele : b)
		    {
			  s1 = String.format("%8s", Integer.toBinaryString(ele & 0xFF)).replace(' ', '0');
		      bstr = bstr+s1;
		      //System.out.println(s1);
		    }
	       */
	      
		   bstr="";
		   
		   
		   for(i=0;i<num;i++)
		   {
			   threadList[i].join();
			   bstr=bstr+threadList[i].bstr;
		   }
	       /*
		   for(i=0;i<num;i++)
		   {
			   System.out.println("-------Thread "+i+" ------------");
			   System.out.println(threadList[i].start+" - "threadList);
			   System.out.println("--------------------------------");
		   }*/
		   //System.out.println("----------------\n"+bstr+"\n---------------");
	   
		   return bstr;
	     }
	   catch (Exception e)
	   {
		   System.out.println("Exception at  readBits:");
		   System.out.println(e);
	   }
	 
	   //System.out.println("Retrieved the bits from File.");
	   return bstr;
	}
	
	
	
	String getStringFromBytes(byte[] b)
	{
		
		   int i=0;
		   String bstr="",s1;
		   int num = 10;
	       ConverterThread[] threadList = new ConverterThread[num];
		   int tlen = b.length/num;
		   
		   for(i=0;i<num-1;i++)
		   {
		      threadList[i]= new ConverterThread(b,i*tlen,i*tlen+tlen);
		      threadList[i].start();
		   }
			   
		   threadList[num-1] = new ConverterThread(b,(num-1)*tlen,b.length);
		   threadList[num-1].start();
			   
			  
		      
		   try
			 {
			  for(i=0;i<num;i++)
			  {
				 threadList[i].join();
				 bstr=bstr+threadList[i].bstr;
			   }
			 }
			 catch(Exception e)
			 {
				  System.out.println("Exception at getStringFromBytes : "+e );
			 }
			  
			  
		return bstr;
		   
	}
	
	
	byte[] getBytes(String bstr)
	{
		byte[]  b = new byte[bstr.length()/8];
		String s1;
		for(int i=0; i<b.length;i++)
		{
		  s1 = bstr.substring(i*8, i*8+8);
		  b[i] = (byte)Integer.parseInt(s1, 2);
		}
		
		return b;
		
	}
	
	 void writeBits(String path, String cipher)
	{
		
		try{
		
			OutputStream os = new FileOutputStream(path);
			byte[]  b = new byte[cipher.length()/8];
			//int len = cipher.length()/8;
			String s1;
			for(int i=0; i<b.length;i++)
			{
			  s1 = cipher.substring(i*8, i*8+8);
			  b[i] = (byte)Integer.parseInt(s1, 2);
			}
			//System.out.println("No of Bytes Written: "+b.length);
			os.write(b);
		    os.close();
		    
		   
		}
		catch(Exception e)
		{
			System.out.println("Exception at writeBits");
			System.out.println(e);
		}
	}
}
