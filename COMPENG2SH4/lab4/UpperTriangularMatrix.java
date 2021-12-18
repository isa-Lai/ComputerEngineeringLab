/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author jianfeng
 */
public class UpperTriangularMatrix {
    private int n;
    private int[] array;
    
    public UpperTriangularMatrix(int n){
    	if(n>0) {
			this.n = n;
		}
		else {
			this.n = 1;
		}
		this.array= new int[this.n*(this.n+1)/2];
        
    }
    
    public UpperTriangularMatrix(Matrix upTriM) throws IllegalArgumentException{
        if(upTriM.isUpperTr()) {
        	this.n=upTriM.getsizeofrows();
        	this.array= new int[this.n*(this.n+1)/2];
        	int count = 0;
        	for(int i=0;i<this.n;i++) {
        		for(int j=i;j<this.n;j++) {
        			this.array[count] = upTriM.getElement(i, j);
        			count++;
        		}
        	}
        }
        else
        	throw new IllegalArgumentException("Not an upper triangular Matrix");
        
    }    
        
    public int getDim(){
        
		
        return this.n; 
    }
    
    public int getElement(int i, int j) throws IndexOutOfBoundsException{

    	if((i<0||i>=this.n)||(j<0||j>=this.n))
			throw new IndexOutOfBoundsException("Invalid indexes");
    	else if(j<i)
    		return 0;
    	else {
    		int index = i*this.n-(i*i-i)/2  +  j-i; //first get the row then get the col
    		return this.array[index];
    	}
 		/* write your implementation here and update return accordingly */
    }
    
    public void setElement(int x, int i, int j) throws IndexOutOfBoundsException,IllegalArgumentException{
    	if((i<0||i>=this.n)||(j<0||j>=this.n))
			throw new IndexOutOfBoundsException("Invalid index");
    	else if(j<i)
    		throw new IllegalArgumentException("Incorrect arguement");
    	else {
    		int index = i*this.n-(i*i-i)/2  +  j-i; //first get the row then get the col
    		this.array[index] = x;
    	}
    }
    
    public Matrix fullMatrix(){

        Matrix full= new Matrix(this.n,this.n); 
        
        int count = 0;
        for(int i=0;i<this.n;i++) {
    		for(int j=i;j<this.n;j++) {
    			full.setElement(this.array[count], i, j);
    			count++;
    		}
    	}
        
		
        return full; 
    }
    
   
      
    public String toString(){
       String output= "";
       for (int i=0;i<this.n;i++) {
			for(int j=0;j<this.n;j++) {
				output+=(String.valueOf(this.getElement(i, j))+" ");

			}
			output+="\n";
					
		}
       
       return output;
    }
    
    public int getDet(){
       int det = 1;
       for (int i=0;i<this.n;i++) {
    	   det*= this.getElement(i, i);
       }
       return det; 
    }

    
    public double[] effSolve(double[] b) throws IllegalArgumentException{

        /* fix the following and write your implementation */
    	double[] sol = new double[b.length];
    	
    	//check dimension of b
    	if (b.length!=this.n) 
    		throw new IllegalArgumentException("The dimension of the matrix is not defined for operation");
    	
    	//compute the determiant
    	int det = 1;
    	for (int i=0;i<this.n;i++) {
    		det *= this.array[i*this.n-(i*i-i)/2];
    	}
    	if (det == 0)
    		throw new IllegalArgumentException("The determinant of the matrix is 0");
    	
    	//calculation of x
    	//calculate the last element in sol first
    	for (int i=this.n-1;i>=0;i--) {
    		// sum is the sum of already calculated sol[i] in this calculating row
        	double sum = 0;
    		for (int j=this.n-1;j>i;j--) {
    			int index = i*this.n-(i*i-i)/2  +  j-i;
    			sum += this.array[index]*sol[j];
    		}
    		sol[i]=(b[i]-sum)/this.array[i*this.n-(i*i-i)/2];
    	}
    	
        return sol;  
    }   
}