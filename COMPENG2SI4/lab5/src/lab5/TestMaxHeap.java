package lab5;
//package Tests;
//import Lab5.*;

import java.util.Random;
import java.util.Arrays;
import java.util.Collections;

import java.lang.Math;

/**
 *
 * @author Weijian, Sara
 * 
 */

public class TestMaxHeap {   
	
	public static void main(String[] args){	
        /** Students should have the following public methods implemented.
         * getSizeHeap() -- returns the number of elements in the array.
         * getSizeArr() -- returns the size of the array (maximum number of elements that we can have).
         * toString() -- separates the numbers by a comma (e.g., 45,21,13).
		 * heapsort() -- returns the numbers in the descending order.
		 * deleteMax() -- delete the max value from the heap.
		 * getHeap() -- returns the heap.
         */
        
        MaxHeap heap1;
        Random random = new Random();

        double cons1Marks = 0.0;
        double cons2Marks = 0.0;
        double insertMarks = 0.0;
        double toStringMarks = 0.0;
        double heapsortMarks = 0.0;
        double marksEach = 0.0;

        //Testing constructor 1: public MaxHeap(int size) and methods: public int getSizeHeap(), public int getSizeHeap()

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("(Test1) Testing Constructor1: create an empty heap with random array size\n");
        int maxPower = 6; marksEach = 0.5;
        for(int power = 2; power <= maxPower; power+=2){
            int ArraySize = (int)Math.pow(random.nextInt(10) + 1, power);
            try{
                heap1 = new MaxHeap(ArraySize);

                if(heap1.getSizeArr() < ArraySize){
                    System.out.println("(Test1) Test failed for Constructor1: public MaxHeap(" + ArraySize + 
                    		") and methods: public int getSizeArr()  (-" + marksEach + ")");
                    System.out.println("(Test1) Expected output: ArraySize >= " + ArraySize);
                    System.out.println("(Test1) Current output: ArraySize = " + heap1.getSizeArr());
                }
                else {
                	System.out.println("(Test1) Test passed for Constructor1: public MaxHeap(" + ArraySize + 
                			") and methods: public int getSizeArr()  (+" + marksEach + ")");
                	cons1Marks += marksEach;
                }

                if(heap1.getSizeHeap() != 0){
                    System.out.println("(Test1) Test failed for Constructor1: public MaxHeap(" + ArraySize + 
                    		") and methods: public int getSizeHeap()  (-" + marksEach + ")");
                    System.out.println("(Test1) Expected output: HeapSize = 0");
                    System.out.println("(Test1) Current output: HeapSize = "+ heap1.getSizeHeap());
                }
                else {
                	System.out.println("(Test1) Test passed for Constructor1: public MaxHeap(" + ArraySize + 
                			") and methods: public int getSizeHeap()  (+" + marksEach + ")");
                	cons1Marks += marksEach;
                }
            }
            catch(Exception e){
                System.out.println("(Test1) Cannot process public MaxHeap(" + ArraySize + ")  (-" + marksEach + ")");
                System.out.println(e.toString());
            }
            System.out.println();
            System.out.println("------------------------------------------------");
        }
        System.out.println("Marks: " + cons1Marks + "/" + maxPower * marksEach);

        System.out.println();
        System.out.println();
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

        //Testing constructor 2: public MaxHeap(int[] someArray) and method: public String toString() 

        System.out.println("(Test2-3) Testing constructor 2 and toString()\n");
        int lowerBound = 8, upperBound = 20 - lowerBound, rounds = 2; marksEach = 1.75;
        String[] msgs = {"no duplicates", "duplicates"};
        int[] sizes = {
        		random.nextInt(upperBound)+lowerBound, random.nextInt(upperBound)+lowerBound,
        		random.nextInt(upperBound)+lowerBound, random.nextInt(upperBound)+lowerBound,
        		random.nextInt(upperBound)+lowerBound};

        for (int round = 0; round < rounds; round++) {
	        for(int loc = 0; loc < sizes.length; loc++){	        	
	            int size = sizes[loc];
	            Integer [] someArray = new Integer[size];
	            for(int j = 0; j < size; j++){
	                someArray[j] = random.nextInt((int)1e4);
	                if (random.nextInt(10) > 5){				// negative numbers
	                    someArray[j] = -someArray[j];
	                }
	            }
                if (round == 1) {
                	someArray[size/2] = someArray[random.nextInt(size/2)];
                	someArray[size/2 + 2] = someArray[random.nextInt(size/2) + size/2];
                }
	            
		        try {	
		            heap1 = new MaxHeap(someArray);		            
 		            String maxheap1 = heap1.toString();
		            
		            try{
		            	Integer[] heapArray1 = stringToArray(maxheap1);
		                boolean is_max_heap = isMaxHeap(heapArray1, someArray, round + 2);
		                if(!is_max_heap){
		                    System.out.println("(Test" + (round+2) + ") Test with array containing " + msgs[round] +
		                    		" failed: Test for size " + sizes[loc] + "  (-" + marksEach + ")");
		                }
		                else {
		                	System.out.println("(Test" + (round+2) + ") Test with array containing " + msgs[round] +
		                			" passed: Test for size " + sizes[loc] + "  (+" + marksEach + ")");
		                	cons2Marks += marksEach;
		                }
	                    System.out.println("(Test" + (round+2) + ") Input array: " + Arrays.toString(someArray));
	                	System.out.println("(Test" + (round+2) + ") Output array: " + Arrays.toString(heap1.getHeap()));
		            }
		            catch(IllegalArgumentException e){
		                System.out.println("(Test" + (round+2) + 
		                		") Invalid character/ Wrong format of string for size " + sizes[loc] + "  (-" + marksEach + ")");
		            }
		        }
		        catch(Exception e){
		            System.out.println("(Test" + (round+2) + ") Cannot Create Heap with size " + sizes[loc] + "  (-" + marksEach + ")");
		            System.out.println(e.toString());
		        }
	            System.out.println("\n------------------------------------------------------");
	        }
        }
        System.out.println("Marks: " + cons2Marks + "/" + sizes.length * rounds * marksEach);
       

        System.out.println();
        System.out.println();
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

        //Testing public void insert() method  
        System.out.println("(Test4-8) Testing insert() method\n");
        int k = 0, arrMax = 0, arrMin = 0, inserts; rounds = 5; 
        String[] msgs1 = {"duplicated values", "random (n > MAX)", "random (n < MIN)", "random (MIN < n < MAX)", "random n"};
        double marksEach2 = 1; marksEach = 0.5;
        for (int round = 0; round < rounds; round++) {
	        for(int loc = 0; loc < sizes.length-2; loc++){            
	            int size = sizes[loc];
	            try{
		            Integer [] someArray = new Integer[size];
		            for(int j = 0; j < size; j++){
		                someArray[j] = random.nextInt((int)1e4);
		                if (random.nextInt(10) > 5){				// negative numbers
		                    someArray[j] = -someArray[j];
		                }
		            }
		            heap1 = new MaxHeap(someArray);  
	            	inserts = 3;
		            if (round == 4) {
		        	    inserts += (heap1.getSizeArr() - heap1.getSizeHeap());
		        	    System.out.println("(Test" + (round + 4) + ") Insert " + msgs1[round] + " into a full array");
		            }
		            else{
		            	System.out.println("(Test" + (round + 4) + ") Insert " + msgs1[round] + " into the array");
		            }
		            System.out.println("(Test" + (round + 4) + ") Input Array: " + Arrays.toString(someArray));
		            System.out.println("(Test" + (round + 4) + ") Your Heap: " + heap1.toString() +
		            		" x,".repeat(heap1.getSizeArr()-heap1.getSizeHeap()));
	        	    System.out.println("(Test" + (round + 4) + ") Your arrSize: " + heap1.getSizeArr());
	        	    System.out.println("(Test" + (round + 4) + ") Your heapSize: " + heap1.getSizeHeap());
	        	    System.out.println();
	        	    
	        	    Integer[] insertVal = new Integer[inserts];	        	    
		            for (int i = 0; i < inserts; i++) {
			            arrMax = Collections.max(Arrays.asList(someArray));
			            arrMin = Collections.min(Arrays.asList(someArray));
			            switch(round){
			            case 0:		// insert duplicated value into the array
			            	k = someArray[random.nextInt(someArray.length)];
			            	break;
			            case 1:		// insert random (n > MAX) into the array
			            	k = random.nextInt(100) + arrMax;
			            	break;
			            case 2:		// insert random (n < MIN) into the array
			            	k = arrMin - random.nextInt(100);
			            	break;
			            case 3:		// insert random (MIN < n < MAX) into the array
			            	k = random.nextInt(arrMax - arrMin) + arrMin;
			            	break;
			            case 4:		// insert random n into the full array;
			            	k = random.nextInt((int)1e4);
			            	break;
			            }
			            heap1.insert(k);
			            insertVal[i] = k;
		            }
	                String maxheap1 = heap1.toString();  
	                System.out.println("(Test" + (round + 4) + ") Insert: " + Arrays.toString(insertVal));
	                System.out.println("(Test" + (round + 4) + ") Output array: " + maxheap1 + 
	                		" x,".repeat(heap1.getSizeArr()-heap1.getSizeHeap()));
	                
	        	    System.out.println("(Test" + (round + 4) + ") arrSize after insert: " + heap1.getSizeArr());
	        	    System.out.println("(Test" + (round + 4) + ") heapSize after insert: " + heap1.getSizeHeap());
	        	    System.out.println();
		            
	                // HeapSize Test
	                if (heap1.getSizeHeap() != (size + inserts)) {
	                	System.out.println("(Test" + (round + 4) + ") HeapSize is Incorrect.  (-" + marksEach + ")");
	                    System.out.println("(Test" + (round + 4) + ") Expected HeapSize: " + (size + inserts));
	                    System.out.println("(Test" + (round + 4) + ") Current HeapSize: "+ heap1.getSizeHeap());
	                }
	                else {
	                	System.out.println("(Test" + (round + 4) + 
	                			") HeapSize(" + heap1.getSizeHeap() + "|" + (size + inserts) + ") is Correct  (+" + marksEach + ")");
	                	insertMarks += marksEach;
	                }                
	                Integer[] resultArray = new Integer[size + inserts];
	                System.arraycopy(someArray, 0, resultArray, 0, size);
	                System.arraycopy(insertVal, 0, resultArray, size, inserts);
	                
	                try{
	                	Integer[] heapArray1 = stringToArray(maxheap1);
	                    boolean is_max_heap = isMaxHeap(heapArray1, resultArray, round + 4);
	                    
	                    if(!is_max_heap){
	                        System.out.println("(Test" + (round + 4) + ") Max heap test failed.  (-" + marksEach2 + ")");
	                    }
	                    else {
	                    	System.out.println("(Test" + (round + 4) + ") Max heap test passed  (+" + marksEach2 + ")");
	                    	insertMarks += marksEach2;
	                    }
	                }
	                catch(IllegalArgumentException e){
	                    System.out.println("(Test" + (round + 4) + 
	                    		") Invalid character/ Wrong format of string for heap size " + "," + (size + inserts) + 
	                    		")  (-" + marksEach2 + ")");
	                }	
	            }
	            catch(Exception e){
	                System.out.println("(Test" + (round + 4) + 
	                		") Could not perform insertion  (-" + (marksEach + marksEach2) + ")");
	                System.out.println(e.toString());
	            }
	            System.out.println("\n------------------------------------------------------");
	        }
        }
        
        System.out.println("Marks: " + insertMarks + "/" + (sizes.length-2) * rounds * (marksEach + marksEach2));
        
        System.out.println();
        System.out.println();
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        
        
        // Testing public toStirng() method         
        System.out.println("(test9) Test toString, represent this.heap in level order\n");
        
        marksEach = 1;
        for(int loc = 0; loc < sizes.length-3; loc++){	        	
            int size = sizes[loc];
            Integer [] someArray = new Integer[size];
            for(int j = 0; j < size; j++){
                someArray[j] = random.nextInt((int)1e4);
                if (random.nextInt(10) > 5){				// negative numbers
                    someArray[j] = -someArray[j];
                }
            }
            String s = new String();           
            
	        try {	
	            heap1 = new MaxHeap(someArray);
	            for (int i = 0; i < heap1.getHeap().length; i++) {
	            	if (heap1.getHeap()[i] != null) {
		            	s += heap1.getHeap()[i];
		            	s += ",";
	            	}
	            }
	            System.out.println("(Test9) Your MaxHeap is " + Arrays.toString(heap1.getHeap()));
                System.out.println("(Test9) Expected MaxHeap String is: " + s);
                System.out.println("(Test9) Output MaxHeap String is " + heap1.toString());
                
                if (heap1.toString().equals(s)) {
                	System.out.println("(Test9) toSring() is Correct  (+" + marksEach + ")");
                	toStringMarks += marksEach;
                }
	            else {
	            	System.out.println("(Test9) toSring() is Incorrect  (-" + marksEach + ")");
	            }	            
	        }
	        catch(Exception e){
	            System.out.println("(Test9) Cannot Create Heap with size " + sizes[loc] + "  (-" + marksEach + ")");
	            System.out.println(e.toString());
	        }	        
            System.out.println();
        }
        System.out.println("Marks: " + toStringMarks + "/" + (sizes.length-3) * marksEach);
        
        
        System.out.println();
        System.out.println();
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");


        //Testing public static void heapsort() method 
        
        System.out.println("(Test10-11) Testing heapsort method\n");
        rounds = 2; marksEach = 1.5;
        String[] msgs2 = {" no ", " "};
        for (int round = 0; round < rounds; round++) {
	        for(int loc = 0; loc < sizes.length; loc++){
	            
	        	boolean flag = true;
	            int size = sizes[loc];
	            Integer [] someArray1 = new Integer[size];
	            Integer [] someArray2 = new Integer[size];
	            for(int j=0;j<size;j++){
	                someArray1[j] = (int)random.nextInt((int)1e6);
	                if(random.nextInt(10)<5){
	                    someArray1[j] = -someArray1[j];
	                }
	            }
                if (round == 1) {
                	someArray1[size/2] = someArray1[random.nextInt(size/2)];
                	someArray1[size/2 + 2] = someArray1[random.nextInt(size/2) + size/2];
                }
                System.arraycopy(someArray1, 0, someArray2, 0, size);
                System.out.println("(test" + (round+10) + ") Apply heapsort on a array with" + msgs2[round] + "duplicate values");
	            System.out.println("(test" + (round+10) + ") Input Array is " + Arrays.toString(someArray1));
	            try {
		            MaxHeap.heapsort(someArray1);
		            Arrays.sort(someArray2, Collections.reverseOrder());
		            System.out.println("(test" + (round+10) + ") Output Array is " + Arrays.toString(someArray1));
		            
		
		            for(int j = 0; j < someArray1.length; j++){
		                if(Integer.compare(someArray1[j], someArray2[j]) != 0) {
		                    System.out.println("(test" + (round+10) + ") heapsort method() test with size " + sizes[loc] + " failed.  (-1)");
		                    System.out.println("(test" + (round+10) + ") Error found at [" + j + "], break the test.");
		                    System.out.println("(test" + (round+10) + 
		                    		") Expected value at [" + j + "] is " + someArray2[j] + ", Current value is " + someArray1[j]);
		                    flag = false;
		                    break;
		                }
		            }
		            
		            if (flag) {
		            	System.out.println("(test" + (round+10) + ") heapsort test with size " + sizes[loc] + " passed.  (+1)");
		            	heapsortMarks += marksEach;
		            }
	            }
	            catch(Exception e){
	                System.out.println("(test" + (round+10) + ") Could not run heapsort with size " + sizes[loc] + "  (-1)");
	                System.out.println(e.toString());
	            }
	            System.out.println("\n------------------------------------------------------");
	        }
        }
        System.out.println("Marks: " + heapsortMarks + "/" + (sizes.length) * rounds * marksEach);

        System.out.println();
        System.out.println();
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        
        System.out.println("Total Marks = " + (cons1Marks + cons2Marks + insertMarks + toStringMarks + heapsortMarks) + "/60.0");
    
	//for my own test
      //  Integer[] myown = {-216957, 439074, -492786, -835751, 6966, 971984, 971984, 677705, 500460, -356412, 500460, 410452, 778000};
        //MaxHeap.heapsort(myown);
        
        
	}
    
    // ---------------------------------------------------- End of the Test ---------------------------------------------------- //
    
    
    public static Integer[] stringToArray(String maxheap)throws IllegalArgumentException{
        String[] strArray = maxheap.split(", |,");        
        Integer[] intArray = new Integer[strArray.length];
        try{            
            for(int i = 0; i < strArray.length; i++) {
                intArray[i] = Integer.parseInt(strArray[i]);
            }
        }
         catch(Exception e){
            System.out.println("Error in toString() method");
            throw new IllegalArgumentException();
        }
        return intArray;
    }

    public static boolean isMaxHeap(Integer[] heapArray, Integer[] someArray, int round){
        boolean is_max_heap = true;
        for(int root_index = 0; root_index <= heapArray.length / 2; root_index++){

            int left_index = 2 * root_index + 1;
            int right_index = 2 * root_index + 2;

            if(left_index < heapArray.length){
                if(heapArray[root_index] < heapArray[left_index]){
                    is_max_heap = false;
                    System.out.println("Max Heap condition violated.");
                    System.out.println("Parent idx = " + root_index + "\t Parent value = " + heapArray[root_index]);
                    System.out.println("Left child idx = " + left_index + "\t Left child value = " + heapArray[left_index]);
                    break;
                }
            }

            if(right_index < heapArray.length){
                if(heapArray[root_index] < heapArray[right_index]){
                    is_max_heap = false;
                    System.out.println("(Test" + (round) + ") Max Heap condition violated.");
                    System.out.println("(Test" + (round) + ") Parent idx = " + root_index + "\t Parent value = " + heapArray[root_index]);
                    System.out.println("(Test" + (round) + ") Right child idx = " + right_index + "\t Right child value = " + heapArray[right_index]);
                    break;
                }
            }
        }
        Integer[] someArrayCopied = new Integer[someArray.length];
        Integer[] heapArrayCopied = new Integer[heapArray.length];
        System.arraycopy(someArray, 0, someArrayCopied, 0, someArray.length);
        System.arraycopy(heapArray, 0, heapArrayCopied, 0, heapArray.length);
        Arrays.sort(someArrayCopied);
        Arrays.sort(heapArrayCopied);
        boolean flag = Arrays.equals(someArrayCopied, heapArrayCopied);
        
        if (!flag) {
        	System.out.println("(Test" + (round) + ") Heap elements don't match with the input array.");
        }
        
        return is_max_heap && flag;
    }
}