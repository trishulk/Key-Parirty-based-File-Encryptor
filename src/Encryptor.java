

public class Encryptor extends Thread
{
	String bstr;
    String cipher;
	char[][] keymat;
    int[] parity;
    int size=8;
    Encryptor(String bstr, char[][] keymat, int[] parity) //The plain bits and the 'key' string
    {
    	this.bstr = bstr;
    	this.keymat = keymat;
    	this.parity = parity;
    	/*
    	keymat = new char[size][size];
    	parity =  new int[size];
    	for(int i=0;i<size;i++)
    	{
    		keymat[i]=kstr.substring(i*size, i*size+size).toCharArray();
            parity[i] = getParity(keymat[i]);
    	}*/
    	
    }
    
    
    
    /*
    int getParity(char[] c)
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
    	
    
    }*/
    
    
	public void run()
	{
		cipher=Encrypt(bstr);
	}
	
	 String Encrypt(String bstr )
	{
		//System.out.println("Encrypting the Data...");
		int matrixBlockSize=8;
		int blockLen = matrixBlockSize*matrixBlockSize;
		String bits;
		char[][] mat = new char[matrixBlockSize][matrixBlockSize];
		char temp;
		//char[][] keymat= new char[matrixBlockSize][matrixBlockSize];
		String cipher="";
		
		/*
		keymat[0]="11011010".toCharArray();
		keymat[1]="10101010".toCharArray();
		keymat[2]="01101101".toCharArray();
		keymat[3]="11111010".toCharArray();
		keymat[4]="10110101".toCharArray();
		keymat[5]="11001101".toCharArray();
		keymat[6]="11101101".toCharArray();
		keymat[7]="00010110".toCharArray();
		
	    
		int[] parity = new int[8];
		
		parity[0]=1;
		parity[1]=0;
		parity[2]=1;
		parity[3]=0;
		parity[4]=1;
		parity[5]=1;
		parity[6]=0;
		parity[7]=1;
		*/
		
		
		//System.out.println("No of Bits : "+bstr.length());
		//System.out.println("No of Blocks : "+bstr.length()/blockLen);
		for(int i=0;i<bstr.length()/blockLen;i++)
		{
		   //System.out.print("Block no. "+i+" ");
		   bits = bstr.substring(i*blockLen, i*blockLen+blockLen);
		   mat = toMatrix(bits,matrixBlockSize);
		   
		   
		   //TRANSPOSE
		   for(int j=0;j<matrixBlockSize;j++)
		   {
			   for(int k=0;k<matrixBlockSize;k++)
			   {
				 temp = mat[j][k];
				 mat[j][k]=mat[k][j];
				 mat[k][j]=temp;
			   }
		   }
		   
		   
		   //PARITY CIRCULAR SHIFT
		   for(int j=0;j<matrixBlockSize;j++)
		   {
			   if(parity[j]==1)
			   {
				 temp = mat[j][0];
				 
				 for(int k=0;k<matrixBlockSize-1;k++)
				 {
					 mat[j][k]=mat[j][k+1];
				 }
				 
				 mat[j][matrixBlockSize-1]=temp;
			   }
		   }
		   
		   //KEY OPERATION
		   for(int j=0;j<matrixBlockSize;j++)
		   {
			   for(int k=0;k<matrixBlockSize;k++)
			   {
				   mat[j][k]=charXOR(mat[j][k],keymat[j][k]);
			   }
		   }
		   
		   
		   for(int j=0;j<matrixBlockSize;j++)
		   {
			   for(int k=0;k<matrixBlockSize;k++)
			   {
				   cipher=cipher+mat[j][k];
			   }
		   }
		}
		//System.out.println("Encrypted the Data.");
		return cipher;
	}
	
	static char[][] toMatrix(String bits , int size)
	{
		String s;
		char[][] mat = new char[size][size];
		for(int i=0;i<size;i++)
		{
			s = bits.substring(i*size,i*size+size);
			mat[i]=s.toCharArray();
		}
		
		return  mat;
	}
	
	static char charXOR(char a, char b)
	{
		if(a=='0'&& b=='0')
			return '0';
		
		if(a=='0'&& b=='1')
			return '1';
		
		if(a=='1' && b=='0')
			return '1';
	
		if(a=='1' && b=='1')
	        return '0';
	
	 System.out.println("ERROR: Expected '0' or '1'. Given: a="+a+", b="+b);
	 return '0';
	}
	
}