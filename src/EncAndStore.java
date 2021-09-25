import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class EncAndStore {
	static int matrixBlockSize=8; //DIMENSION OF MATRIX BLOCK
	static int threadNum=40;      //NUMBER OF THREADS
	
	byte[] bplain,benc;
	String keystr;
	
	static String removeNulls(String plaintext)
	{
		int pos,len,i;
		len = plaintext.length();
		pos = len;
		String str,str2;
		
		for(i=len-8;i>0;i=i-8)
		{
			str = plaintext.substring(i, i+8);
			//System.out.println("Substring: "+str);
			if(!str.equals("00000000"))
			{
				//System.out.println("Break!");
				break;
			}
			
			pos = i;
		}
		
		str2 = plaintext.substring(0,pos);
		return str2;
	}
	
	 static int getParity(char[] c)
	    {
	    	int count=0;
	    	for(char ele : c)
	    	{
	    		if(ele=='1')
	    			count++;
	    			
	    	}
	    	
	    	if(count%2==0)
	    		return 0;
	    	else
	    		return 1;
	    	
	    
	    }
	 
	 void Encrypt()
	 {
		
			    
	    //###########--------------ENCRYPTION--------------------------
	   
	    char[][] keymat = new char[matrixBlockSize][matrixBlockSize];
	    int[] parity =  new int[matrixBlockSize];
    	for(int i=0;i<matrixBlockSize;i++)
    	{
    		keymat[i]=keystr.substring(i*matrixBlockSize, i*matrixBlockSize+matrixBlockSize).toCharArray();
            parity[i] = getParity(keymat[i]);
    	}
	    
	    
	    FileOperator f = new FileOperator();
	    
	    //Get bit String
		String bstr = f.getStringFromBytes(bplain);
		
	   //Encrypt the File
		int blen = bstr.length();
		int blockSize = matrixBlockSize*matrixBlockSize;
		int noOfBlocks = blen/blockSize;
		//System.out.println("String Length before padding: "+blen);
		
		//PADDING WITH ZEROS
		int padLen = blockSize - blen%blockSize;
		if(blen%blockSize==0)
		{
			padLen=0;
		}
		else
		{
			noOfBlocks++;
		}
		char[] pad = new char[padLen];
		Arrays.fill(pad, '0');
		bstr = bstr+new String(pad);
		blen = bstr.length();
		
		//System.out.println("No of Bits Padded: "+padLen);
		//System.out.println("ENCRYPTING THE FILE...");
		
		
		//CREATE THREADS
		Encryptor[] threadList = new Encryptor[threadNum];
		
		//String cipher = Encrypt(bstr);
		
		
		String[] b = new String[threadNum] ;
		
		int blocksPerThread=noOfBlocks/threadNum;
		int tlen1 = (blocksPerThread+1)*blockSize;
		int tlen2 = (blocksPerThread)*blockSize;
		int i;
		
		//System.out.println("String Length after padding: "+blen);
		//System.out.println("String Length Per Thread: "+tlen2+" or "+tlen1);
		//System.out.println("No of Blocks: "+noOfBlocks);
		//System.out.println("Blocks Per Thread: "+blocksPerThread+" or "+(blocksPerThread+1));
		
		for( i=0; i<noOfBlocks%threadNum;i++)
		{
			//System.out.println("i="+i);
			b[i]=bstr.substring(i*tlen1,i*tlen1+tlen1 );
			//System.out.println("b["+i+"] : "+(i*tlen1)+"-----> "+(i*tlen1+tlen1)+" : "+tlen1);
		}
		
		int k = (i-1)*tlen1+tlen1;
		System.out.println("------------------------------");
		/*
		for(i=noOfBlocks%threadNum;i<threadNum;i++)
		{
			//System.out.println("i="+i);
			b[i]=bstr.substring(i*tlen2,i*tlen2+tlen2);
			System.out.println("b["+i+"] : "+(i*tlen2)+"-----> "+(i*tlen2+tlen2)+" : "+tlen2);
		}*/
		
		int n1=threadNum-noOfBlocks%threadNum;
		int j=0,s,e;
		for(i=noOfBlocks%threadNum;i<threadNum;i++)
		{
			//System.out.println("i="+i);
			s = k + j*tlen2;
			e = k+ j*tlen2 + tlen2;
			b[i]=bstr.substring(s,e);
			//System.out.println("b["+i+"] : "+s+"-----> "+e+" : "+tlen2);
			j++;
		}
		//b[num-1] = b[num-1]+bstr.substring(i*tlen);	
		
		//Date dt = new Date();
		//long t1 = dt.getTime();
		//System.out.println("-----ENCRYPTION STARTED ----------------");
		//long time1 = System.nanoTime();
		
		for(i=0;i<threadNum;i++)
		{
		    //System.out.println("Starting Thread "+i+"...");
			threadList[i] = new Encryptor(b[i],keymat,parity);
		    threadList[i].start();
		}
		
		//System.out.println("Ciphers generated from Threads:");
		String cipher="";
		
		try
		{
		  for(i=0;i<threadNum;i++)
		  {
			 threadList[i].join();
			 cipher = cipher+threadList[i].cipher;
			//System.out.println(threadList[i].cipher.length());
		  }
		}
		catch(Exception e1)
		{
			System.out.println("Exception at EncAndStore Encrypt : "+e1);
		}
		
		/*
		long t2 = dt.getTime();
		long time2 = System.nanoTime();
		long enctime = t2-t1;
		long enctime2 = time2-time1;
		double enctime3 = enctime2/Math.pow(10, 9);
		System.out.println("-----------------ENCRYPTION FINISHED -----------");
		System.out.println("-----------------------------");
		System.out.println("ENCRYPTION TIME: "+enctime+" ms");
		System.out.println("ENCRYPTION TIME: "+enctime2+" ns");
		System.out.println("ENCRYPTION TIME: "+enctime3+" secs");
		System.out.println("-----------------------------");
		System.out.println("Cipher Length Generated: "+cipher.length());
		*/
		
		
		benc=f.getBytes(cipher);
		
		
		 
		 
	 }
	 
	
	 
	void Decrypt()
	{
		
		 
		 
	   
	    //###########--------------ENCRYPTION--------------------------
	    //GENERATE KEY
	    Random rnd = new Random();
	    char[][] keymat = new char[matrixBlockSize][matrixBlockSize];
	    int[] parity =  new int[matrixBlockSize];
	
	   
	   
    	for(int i=0;i<matrixBlockSize;i++)
    	{
    		keymat[i]=keystr.substring(i*matrixBlockSize, i*matrixBlockSize+matrixBlockSize).toCharArray();
            parity[i] = getParity(keymat[i]);
    	}
	    
	    
	    FileOperator f = new FileOperator();
	    String cipher2 = f.getStringFromBytes(benc);
	  	Decryptor[] threadList2 = new Decryptor[threadNum]; 
	
	  	int blen = cipher2.length();
		int blockSize = matrixBlockSize*matrixBlockSize;
		int noOfBlocks = blen/blockSize;
		//System.out.println("String Length before padding: "+blen);
		
		//PADDING WITH ZEROS
		int padLen = blockSize - blen%blockSize;
		if(blen%blockSize==0)
		{
			padLen=0;
		}
		else
		{
			noOfBlocks++;
		}
		char[] pad = new char[padLen];
		
		/*
		Arrays.fill(pad, '0');
		bstr = bstr+new String(pad);
		blen = bstr.length();
		
		System.out.println("No of Bits Padded: "+padLen);
		System.out.println("ENCRYPTING THE FILE...");
		
		
		//CREATE THREADS
		Encryptor[] threadList = new Encryptor[threadNum];
		
		//String cipher = Encrypt(bstr);
		
		*/
		
		
		String[] b = new String[threadNum] ;
		
		int blocksPerThread=noOfBlocks/threadNum;
		int tlen1 = (blocksPerThread+1)*blockSize;
		int tlen2 = (blocksPerThread)*blockSize;
		int i;
		int k;
	
		int n1=threadNum-noOfBlocks%threadNum;
		int j=0,s,e;
	
		
	    
	  //###################--------------DECRYPTION -----------------------####################-
	   
		
		for( i=0; i<noOfBlocks%threadNum;i++)
		{
			//System.out.println("i="+i);
			b[i]=cipher2.substring(i*tlen1,i*tlen1+tlen1 );
			//System.out.println("b["+i+"] : "+(i*tlen1)+"-----> "+(i*tlen1+tlen1));
		}
		k = (i-1)*tlen1+tlen1;
		
		//System.out.println("-------------------------------");
		j=0;
		
		for(i=noOfBlocks%threadNum;i<threadNum;i++)
		{
			//System.out.println("i="+i);
			s = k + j*tlen2;
			e = k+ j*tlen2 + tlen2;
			b[i]=cipher2.substring(s,e);
			//System.out.println("b["+i+"] : "+s+"-----> "+e+" : "+tlen2);
			j++;
		}
		
		//b[num-1] = b[num-1]+bstr.substring(i*tlen);	
		
		
		for(i=0;i<threadNum;i++)
		{
		    //System.out.println("Starting Thread "+i+"...");
			threadList2[i] = new Decryptor(b[i],keymat,parity);
		    threadList2[i].start();
		}
		
		String plaintext="";
	    
		try{
		  
		  for(i=0;i<threadNum;i++)
		  {
			 threadList2[i].join();
			 plaintext = plaintext+threadList2[i].plainBits;
		  }
		}
		catch(Exception e2)
		{
			System.out.println("Exception at EncAndStore Decryption : "+e2);
		}
		
		plaintext = removeNulls(plaintext);
		

		/*Date dt = new Date();
		long t1 = dt.getTime();
		System.out.println("-----ENCRYPTION STARTED ----------------");
		long time1 = System.nanoTime();
		
		long t2 = dt.getTime();
		long time2 = System.nanoTime();
		long enctime = t2-t1;
		long enctime2 = time2-time1;
		double enctime3 = enctime2/Math.pow(10, 9);
		
		
		System.out.println("-----------------ENCRYPTION FINISHED -----------");
		System.out.println("-----------------------------");
		System.out.println("ENCRYPTION TIME: "+enctime+" ms");
		System.out.println("ENCRYPTION TIME: "+enctime2+" ns");
		System.out.println("ENCRYPTION TIME: "+enctime3+" secs");
		System.out.println("-----------------------------");
		System.out.println("Cipher Length Generated: "+cipher.length());
		*/
		
	

	    bplain=f.getBytes(plaintext);
            
		
	}
	 
	 
}
