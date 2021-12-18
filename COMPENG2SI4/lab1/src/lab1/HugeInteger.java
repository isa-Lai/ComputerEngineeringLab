//Yuying Lai 400268588 
package lab1;
import java.util.Random;
import java.util.zip.DataFormatException;

public class HugeInteger {
	
	private int[] num; //store each digit into array, the last element in array is the largest digit
	private int negNum = 1; //-1 for negative num
	private int size; //total number of digits
	
	public HugeInteger(String val) throws DataFormatException {
		//error thrown when empty
		if(val.length()==0) {
			throw new DataFormatException("Cannot enter blank content");
		}
		//check is number is negative
		int index = 0;
		if (val.startsWith("-")) {
			size = val.length()-1;
			//if no content after negative throw error
			if(size==0) {
				throw new DataFormatException("Cannot enter blank content");
			}
			negNum = -1;
			index = 1;
		}
		else {
			size = val.length();
		}
		//ignore the leading zero
		while(index<size) {
			if(Character.getNumericValue(val.charAt(index))==0 && size!=1) {
				index++;
				size--;
			}
			else
				break;
		}
		//store each number into num array. from the last to bigining
		num = new int[size];
		for (int i = size-1;i>=0;i--) {
			char cha = val.charAt(index);
			//if there is content other than number, throw error
			if(!Character.isDigit(cha)) {
				throw new DataFormatException("Please Enter Numeric Value");
			}
			
			num[i] = Character.getNumericValue(cha);
			index++;
			//System.out.println(num[i]);
		}
		
	}
	public HugeInteger(int n) throws DataFormatException {
		//error thrown if number of digit is less than 1
		if(n<1) {
			throw new DataFormatException("Number of digit cannot be less than 1");
		}
		Random randNum = new Random();
		//random sign-
		size = n;
		num = new int[n];
		if(!randNum.nextBoolean()) {
			negNum = -1;
		}
		//store random integer into array
		num[size-1] = randNum.nextInt(8)+1;//the first must be larger than 1.
		//System.out.println(num[0]);
		for (int i = size-2;i>=0;i--) {
			num[i] = randNum.nextInt(9);
			//System.out.println(num[i]);
		}
	}
	
	//additional method for convinience 
	public HugeInteger copy() {
		//make new integer and copy each value into this integer
		try {
			int total =  size;
			HugeInteger result = new HugeInteger(total);
			result.negNum =  negNum;
			for (int i = 0;i<total;i++) {
				result.num[i] =  num[i];
			}
			return result;
		}
		catch (DataFormatException e) {
			return null;
		}
	}
	
	
	public HugeInteger add(HugeInteger h) {
		//situation when one is pos the other is neg, use subtraction
		if ( negNum == 1 && h.negNum == -1) {
			h.negNum = h.negNum*(-1);
			HugeInteger result =  subtract(h);
			h.negNum = h.negNum*(-1); // have to change the sign of h back;
			return result;
		}
		else if ( negNum == -1 && h.negNum == 1) {
			 negNum =  negNum*(-1);
			HugeInteger result = h.subtract(this);
			 negNum =  negNum*(-1); // have to change the sign of this back;
			return result;
		}		
		
		//if two have same sign
		HugeInteger tempInt1;
		HugeInteger tempInt;
		int temSize;
		int carryout = 0;
		
		//make new Integer copy first
		if ( size>=h.size) {
			temSize =  size;
			//result =  copy();
			tempInt1 = this;
			tempInt = h;
		}
		else {
			temSize = h.size;
			//result = h.copy();
			tempInt = this;
			tempInt1 = h;
		}
		int[] total = new int[temSize+1];
		
		
		//adding process
		//the carry out will be pos which determine if we need to borrow highest digit
		for (int i = 0; i<temSize;i++) {
			int sum;
			if(i<tempInt.size) {
				sum = carryout + tempInt1.num[i]+tempInt.num[i] ;

			}
			//situation if adding is finished, still need copy the rest digit into result
			else {
				sum = carryout + tempInt1.num[i] ;
			}
			total[i] = sum%10;
			carryout = sum/10;
		}
		
		//situation when there is extra digit
		//it cannot be less digit so no need for change
		if (carryout != 0) {
			
				//make new array to store the extra digit
			/*
				int[] newNum = new int[result.size+1];
				for (int i = 0;i<result.size;i++) {
					newNum[i] = result.num[i];
				}*/
				total[temSize] = carryout;
				temSize++;
			
		}
		try {
			//set HugeInteger
			HugeInteger result = new HugeInteger(temSize);
			result.num = total;
			//determine the sign
			if(result.size==1 & result.num[0]==0) {
				result.negNum = 1; // if the result is 0, output without neg sign anyway
			}
			else {
					if( negNum ==1) {
						result.negNum = 1; // if two number both positive, result will be positive
					}
					else {
						result.negNum = -1; // if two number both negative, result will be neg
					}
				
			}
			
			return result;
		}
		catch(DataFormatException e) {
			return null;
		}
		
		
	}
	
	public HugeInteger subtract(HugeInteger h) {
		//situation when one is pos the other is neg, use add
		if ( negNum*h.negNum == -1) {
			h.negNum = h.negNum*(-1);
			HugeInteger result =  add(h);
			h.negNum = h.negNum*(-1); // have to change the sign of h back;
			return result;
		}
		
		HugeInteger result;
		int compare =  compareTo(h);
		int temSize;
		int carryout = 0;
		
		
		//fine the # of digit
		if ( size>=h.size) {
			temSize =  size;
		}
		else {
			temSize = h.size;
		}
		
		//get 9's complementary of h
		int[] diff = new int[temSize]; // result variable
		int k;
		for (k = 0; k<h.size;k++) {
			diff[k] = 9-h.num[k];
		}
		for (;k<temSize;k++) {
			diff[k] = 9;
		}
		
		
		//adding process // adding this to 9's complementary of h
		//the carry out will be pos which determine if we need to borrow highest digit
		for (int i = 0; i<temSize;i++) {
			int sum;
			if(i< size) {
				sum = carryout +diff[i]+ num[i] ; //9's com of h + this

			}
			//situation if adding is finished, still need copy the rest digit into result
			else {
				sum = carryout + diff[i] ;
			}
			diff[i] = sum%10;
			carryout = sum/10;
		}
		
		//situation when there is 1 in carryout, adding 1 to the last digit and ignore the carryout
		if (carryout != 0) {
			int complement = carryout;
			for (int i = 0; i<temSize;i++) {
				int sum = complement + diff[i];
				diff[i] = sum%10;
				complement = sum/10;
			}
			
			/*if (complement!= 0) {	
				//make new array to store the extra digit if necessary
				//int[] newNum = new int[result.size+1];
				/*
				for (int i = 0;i<result.size;i++) {
					newNum[i] = result.num[i];
				}// old version
				diff[temSize] = Math.abs(carryout);
				//result.num = newNum;
				temSize++;
			}*/
		}
		else {
			//situation when there is no carryout
			//get the 9's complement of the result
			for (int i = 0; i<temSize;i++) {
				diff[i] = 9-diff[i];
			}
		}
		
		//determine how many digit left
		//if result is 0, the last 0 will still remain for indication
		for(int i=temSize-1;i>0;i--) {
			if(diff[i] == 0) {
				temSize--;  
			}
			else
				break;
		}
		/*
		if(result.size>temSize) {
			//make new array if digit becomes less
			int[] newNum = new int[temSize];
			for (int i = 0;i<temSize;i++) {
				newNum[i] = result.num[i];
			}
			result.num = newNum;
			result.size=temSize;
		}*/
		
		try {
			//set value in result
			result = new HugeInteger(temSize);
			result.num = diff;
			//determine the sign
			if(compare==0) {
				result.negNum = 1; // if the result is 0, output without neg sign anyway
			}
			else {
					if(compare == 1) {
						result.negNum = 1; // larger minus smaller give positice
					}
					else {
						result.negNum = -1; // smaller minus larger give negatice
					}
				
			}
			
			return result;
		}
		catch (DataFormatException e) {
			return null;
		}
	}
	
	
	public HugeInteger multiply(HugeInteger h) {
		int tempSize =  size+h.size;
		int[] product = new int[tempSize];
		int carryout=0;
		
		//multiplying process
		//multiply each digit and added together
		/*
		 * for example
		 *  		2  6 x
		 *  		3  5
		 *  		------
		 *  		3  0 = 5*6
		 *  	 1  0    = 2*5
		 *  	 1  8    = 3*6
		 *   0   6       = 2*3
		 *   ------------------
		 *   0   9  1  0 = 30+100+180+600
		 */
		int digit = 0;
		int digitCarryout = 0;
		for (int i=0;i< size;i++) {
			for (int k = 0;k<h.size;k++) {
				int temppro =  num[i]*h.num[k]+carryout+digitCarryout;
				product[k+digit]+=temppro%10;
				carryout = temppro/10;
				digitCarryout = product[k+digit]/10;
				product[k+digit] = product[k+digit]%10;
			}
			product[h.size+digit] = carryout + digitCarryout;
			carryout = 0;
			digitCarryout = 0;
			digit++;
		}
		//check if # of digit is actually less than  size+h.size
		for(int i=product.length-1;i>0;i--) {
			if(product[i] == 0) {
				tempSize--;  
			}
			else
				break;
		}
		
		//create new HugeInteger and copy the value into it
		try {
			HugeInteger result = new HugeInteger(tempSize);
			result.negNum =  negNum;
			/*
			for (int i = 0;i<tempSize;i++) {
				result.num[i] = product[i];
			}*///original type of copying data
			result.num = product;
			//set the sign
			if(result.size==1 & result.num[0]==0) {
				result.negNum = 1; // if the result is 0, output without neg sign anyway
			}
			else
				result.negNum =  negNum*h.negNum;
			return result;
		}
		catch(DataFormatException e) {
			return null;
		}
	}
	
	public int compareTo(HugeInteger h) {
		//compare positive and negative first
		if( negNum==1 && h.negNum==-1) {
			return 1;    //pos num always larger than neg, and vice versa
		}
		else if( negNum==-1 && h.negNum==1) {
			return -1;
		}
		else {
			//compare the number of digit, this point only compare abs
			int result = 0;
			if( size>h.size) {
				result = 1;    //for abs, more digit is larger
			}
			else if( size < h.size) {
				result = -1;
			}
			//compare from the largest digit until one is larger
			//-100 -150
			else {
				for(int i =  size-1; i>=0;i--) {
					if( num[i]>h.num[i]) {
						result = 1;   //for abs with same # of digit, a larger value in larger digit result in larger number
						break;
					}
					else if( num[i]<h.num[i]) {
						result = -1;
						break;
					}
				}
			}
			// if both integers are negative, than the one have larger absolute value will be smaller
			//same will remain 0 at this step
			return (result* negNum);
		}
	}
	
	public String toString() {
		String result = new String("");
		if(negNum == -1) {
			result+="-";  // print neg if exist
		}
		for(int i = size-1;i>=0;i--) {
			result += num[i];  // print from the last to the first in array, from larger digit to smaller
		}
		return result;
	}
}
