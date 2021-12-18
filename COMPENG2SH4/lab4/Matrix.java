/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author jianfeng
 */
public class Matrix {
	
	//comment
    
	private int[][]  matrixData;
	private int    rowsNum;	
	private int    colsNum;	
	
	public Matrix( int row, int col ) 
	{   
		/*
		* constructs a row-by-col matrix with
		* all elements equal to 0; if row <= 0, the number of rows of the matrix is set to
		* 3; likewise, if col <= 0 the number of columns of the matrix is set to 3.		
		*/
		//set rowsNum & colsNum
		if(row>0) {
			this.rowsNum = row;
		}
		else {
			this.rowsNum = 3;
		}
		if(col>0) {
			this.colsNum = col;
		}
		else {
			this.colsNum = 3;
		}
		this.matrixData= new int[this.rowsNum][this.colsNum];
	}

	public Matrix( int[][] table) 
	{	

		/*
		* constructs a matrix out of the two dimensional array table, with the same number of rows, columns, and the same
		* element in each position as array table.
		*/ 
		this.rowsNum = table.length;
		this.colsNum = table[0].length;
		this.matrixData = new int[this.rowsNum][this.colsNum];
		for(int i=0;i<rowsNum;i++) {
			for(int k=0;k<colsNum;k++) {
				this.matrixData[i][k] = table[i][k];
			}
		}
		
	}
	
	public int getElement(int i, int j) throws IndexOutOfBoundsException
	{ 	
		/*
		* returns the element on row i and column j of this matrix; 
		* it throws an exception (IndexOutOfBoundsException) if any of indexes i and j is not in the required range 
		* (rows and columns indexing starts with 0)
		*  the detail message of the exception should read: "Invalid indexes".
		*/
		if((i>=0 && i<this.rowsNum)&&(j>=0 && j<this.colsNum))
			return this.matrixData[i][j];
		else
			throw new IndexOutOfBoundsException("Invalid indexes.");
	}
        
    public boolean setElement(int x, int i, int j){ 
            
        /* the detail message of the exception should read: "Invalid indexes" */
    	if((i>=0 && i<this.rowsNum)&&(j>=0 && j<this.colsNum)) {
    		this.matrixData[i][j] = x;
    		return true;
    	}
    	return false;
    } 

    public Matrix copy(){ 
        
    	/* fix the code and write your implementation below */
		Matrix copy = new Matrix (this.matrixData);
		
		
		
		
        return  copy; 
    }    
                
	public void addTo( Matrix m ) throws ArithmeticException
	{
		
		/* the detail message of the exception should read: "Invalid operation". */
		if((this.rowsNum == m.rowsNum)&&(this.colsNum == m.colsNum)) {
			for (int i=0;i<this.rowsNum;i++) {
				for (int j=0;j<this.colsNum;j++) {
					this.matrixData[i][j] += m.getElement(i, j);
				}
			}
		}
		else
			throw new ArithmeticException("Invalid operation");
		
	
	}
	
    public Matrix subMatrix(int i, int j) throws ArithmeticException{ 
        
		/* The exception detail message should read: "Submatrix not defined"*/
    	
    	/* fix the code and write your implementation below */
		Matrix subM = new Matrix (i+1,j+1);
		if((i<0||i>=this.rowsNum)||(j<0||j>=this.colsNum))
			throw new ArithmeticException("Submatrix not defined");
		
		for (int k=0;k<=i;k++) {
			for (int l=0;l<=j;l++) {
				subM.setElement(this.matrixData[k][l], k, l);
			}
		}
		
		
        return  subM; 
        
    }
        
    public int getsizeofrows(){ 
           
		/* update below return */
		return this.rowsNum;
    }
        
    public int getsizeofcols(){
		
		/* update below return */
        return this.colsNum; 
    }
        
    public boolean isUpperTr(){
            
		/* write your implementation here and update return accordingly */
    	for (int i=1;i<this.rowsNum;i++)
    		for (int j=0;(j<i&&j<this.colsNum);j++) {
    			if(this.matrixData[i][j]!=0) {
    				return false;
    			}
    		}
        return true;  
	}
        
    public static Matrix sum(Matrix[] matArray) throws ArithmeticException{
            
            
        Matrix superMatrix = new Matrix (matArray[0].rowsNum,matArray[0].colsNum); 
        for (int i=0;i<matArray.length;i++) {
        	if((matArray[i].rowsNum!=superMatrix.rowsNum)||(matArray[i].colsNum!=superMatrix.colsNum)) {
        		throw new ArithmeticException("Invalid operation");
        	}
        	superMatrix.addTo(matArray[i]);
        }
            
            
        return superMatrix; 
    }
        
	public String toString(  )
	{
		String output = new String(""); 
		
		for (int i=0;i<this.rowsNum;i++) {
			for(int j=0;j<this.colsNum;j++) {
				output+=(String.valueOf(this.matrixData[i][j])+" ");

			}
			output+="\n";
					
		}

        	
		return output;
	}
    
}