/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1;
import java.math.BigInteger; 
import java.util.Random;

/**
 *
 * @author Ming Chen, Han Zhang, Mehrshad Kafi
 * 
 */
public class TestHugeInteger {
    // if your terminal support to print color in console, use:
//    public static final String ANSI_RESET = "\u001B[0m";
//    public static final String ANSI_BLACK = "\u001B[30m";
//    public static final String ANSI_RED = "\u001B[31m";
//    public static final String ANSI_GREEN = "\u001B[32m";
//    public static final String ANSI_YELLOW = "\u001B[33m";
//    public static final String ANSI_BLUE = "\u001B[34m";
//    public static final String ANSI_PURPLE = "\u001B[35m";
//    public static final String ANSI_CYAN = "\u001B[36m";
//    public static final String ANSI_WHITE = "\u001B[37m";
    // otherwise, use:
    public static final String ANSI_RESET = "";
    public static final String ANSI_BLACK = "";
    public static final String ANSI_RED = "";
    public static final String ANSI_GREEN = "";
    public static final String ANSI_YELLOW = "";
    public static final String ANSI_BLUE = "";
    public static final String ANSI_PURPLE = "";
    public static final String ANSI_CYAN = "";
    public static final String ANSI_WHITE = "";
    
    // when test failed, the size of number <= NUM_DISPLAY, display the number. 
    public static final int NUM_DISPLAY = 1000;
    // iterate the SIZE_ARRAY MAX_RUN times.
    public static final int MAX_RUN = 50;
    // use for generating invalid string for constructor 1.
    public static final int MAX_INVALID_STRING = 5;
    // different size for test
    public static final int[] SIZE_ARRAY = new int[] {1,(int)1e3,(int)5e3,(int)1e4};

    public static void main(String args[]){
        double totalMark = 0;
             
        // Test constructor 1
        totalMark += mainConstructorsString();
        // Test constructor 2
        totalMark += mainConstructorsNumber();
        // Test positive addition
        totalMark += mainPositiveAddition();
        
        System.out.println(ANSI_PURPLE + "FINAL TOTAL MARK IS " + totalMark + ANSI_RESET);
    }
    
    public static double mainConstructorsString(){
        /* 
        Six test cases for constructor with string (Total marks: 6, 1 mark for each)
        1) valid string: e.g. 123 [1 mark]
        2) invalid string in the middle: e.g. 12^%12 [1 mark]
        3) invalid string at the beginning: e.g. %$123 [1 mark]
        4) invalid string at the end: e.g. 1234*& [1 mark]
        5) empty string: "" [1 mark]
        6) leading zero negative invalid: e.g. 0000-1234 [1 mark]
        */        
        double totalMark = 0; 
        double case_1_mark = 1;
        double case_2_mark = 1;
        double case_3_mark = 1;
        double case_4_mark = 1;
        double case_5_mark = 1;
        double case_6_mark = 1;
        int fails = 0;
        int each_fail = 0;
        int maxRun = MAX_RUN;
        int[] sizes = SIZE_ARRAY;

        System.out.println(ANSI_PURPLE + "BEGIN TEST CONSTRUCTOR WITH STRING" + ANSI_RESET);        
        // case 1 
        // valid string: 123        
        each_fail = testConstructorString(sizes, 1, 1, maxRun, false, false);
        fails += each_fail;
        if (each_fail == 0){
            totalMark += case_1_mark;
            System.out.println(ANSI_GREEN + "Test cases in Constructor with STRING for valid input success" + " ( +" + case_1_mark + " ) "+ ANSI_RESET);
        }else{
            System.out.println(ANSI_RED + "Test cases in Constructor with STRING for valid input fail" + " ( -" + case_1_mark + " ) " + ANSI_RESET);
        }        

        // case 2 
        // invalid string in the middle: 12^%12
        each_fail = testConstructorString(sizes, 1, 1, maxRun, true, false);
        fails += each_fail;
        if (each_fail == 0){
            totalMark += case_2_mark;
            System.out.println(ANSI_GREEN + "Test cases in Constructor with STRING for invalid in the middle success" + " ( +" + case_2_mark + " ) " + ANSI_RESET);
        }else{
            System.out.println(ANSI_RED + "Test cases in Constructor with STRING for invalid in the middle fail" + " ( -" + case_2_mark + " ) " + ANSI_RESET);
        }
               
        // case 3 
        // invalid string at the beginning: %$123
        each_fail = testConstructorString(sizes, 0, 1, maxRun, true, false);
        fails += each_fail;
        if (each_fail == 0){
            totalMark += case_3_mark;
            System.out.println(ANSI_GREEN + "Test cases in Constructor with STRING for invalid at the beginning success" + " ( +" + case_3_mark + " ) " + ANSI_RESET);
        }else{
            System.out.println(ANSI_RED + "Test cases in Constructor with STRING for invalid at the beginning fail" + " ( -" + case_3_mark + " ) " + ANSI_RESET);
        }

        // case 4 
        // invalid string at the end: 1234*&
        each_fail = testConstructorString(sizes, 1, 0, maxRun, true, false);
        fails += each_fail;
        if (each_fail == 0){
            totalMark += case_4_mark;
            System.out.println(ANSI_GREEN + "Test cases in Constructor with STRING for invalid at the end success" + " ( +" + case_4_mark + " ) " + ANSI_RESET);
        }else{
            System.out.println(ANSI_RED + "Test cases in Constructor with STRING for invalid at the end fail" + " ( -" + case_4_mark + " ) " + ANSI_RESET);
        }
        
        // case 5 
        // empty string: ""
        each_fail = testConstructorString(sizes, 0, 0, maxRun, true, false);;
        fails += each_fail;
        if (each_fail == 0){
            totalMark += case_5_mark;
            System.out.println(ANSI_GREEN + "Test cases in Constructor with STRING for invalid empty success" + " ( +" + case_5_mark + " ) " + ANSI_RESET);
        }else{
            System.out.println(ANSI_RED + "Test cases in Constructor with STRING for invalid empty fail" + " ( -" + case_5_mark + " ) " + ANSI_RESET);
        }         
        
        // case 6
        // leading zero negative invalid: 0000-1234
        each_fail = testConstructorString(sizes, 1, 1, maxRun, true, true);;
        fails += each_fail;
        if (each_fail == 0){
            totalMark += case_6_mark;
            System.out.println(ANSI_GREEN + "Test cases in Constructor with STRING for invalid leading zero negative success" + " ( +" + case_6_mark + " ) " + ANSI_RESET);
        }else{
            System.out.println(ANSI_RED + "Test cases in Constructor with STRING for invalid leading zero negative fail" + " ( -" + case_6_mark + " ) " + ANSI_RESET);
        }         
        
        if(fails==0){
            System.out.println(ANSI_PURPLE + "Test constructor with string PASS: ALL" + ANSI_RESET);
        }else{
            System.out.println(ANSI_PURPLE + "Test constructor with string FAILED for " + fails + " case(s)" + ANSI_RESET);
        }
        
        System.out.println(ANSI_PURPLE + "END TEST CONSTRUCTOR WITH STRING, TOTAL MARK IS " + totalMark + ANSI_RESET);
        
        return totalMark;
    }    
    
    public static double mainConstructorsNumber(){
        /* 
        Three test cases for constructor with number (Total marks: 3)
        1) positive valid number, e.g. 12 [1 mark]
        2) invalid zero [1 mark]
        3) negative invalid number, e.g. -13 [1 mark]
        */        
        double totalMark = 0; 
        double case_1_mark = 1;
        double case_2_mark = 1;
        double case_3_mark = 1;
        int fails = 0;
        int each_fail = 0;
        int maxRun = MAX_RUN;
        int[] sizes = SIZE_ARRAY;

        System.out.println(ANSI_PURPLE + "BEGIN TEST CONSTRUCTOR WITH NUMBER" + ANSI_RESET);        
        
        // case 1
        // positive valid number        
        each_fail = testConstructorNumber(sizes, 1, maxRun);
        fails += each_fail;
        if (each_fail == 0){
            totalMark += case_1_mark;
            System.out.println(ANSI_GREEN + "Test cases in Constructor with NUMBER for POSITIVE valid input success" + " ( +" + case_1_mark + " ) "+ ANSI_RESET);
        }else{
            System.out.println(ANSI_RED + "Test cases in Constructor with NUMBER for POSITIVE valid input fail" + " ( -" + case_1_mark + " ) " + ANSI_RESET);
        }        

        // case 2 
        // invalid zero
        each_fail = testConstructorNumber(sizes, 0, 1);
        fails += each_fail;
        if (each_fail == 0){
            totalMark += case_2_mark;
            System.out.println(ANSI_GREEN + "Test cases in Constructor with NUMBER for size ZERO success" + " ( +" + case_2_mark + " ) " + ANSI_RESET);
        }else{
            System.out.println(ANSI_RED + "Test cases in Constructor with NUMBER for size ZERO fail" + " ( -" + case_2_mark + " ) " + ANSI_RESET);
        }
               
        // case 3 
        // negative invalid number
        each_fail = testConstructorNumber(sizes, -1, maxRun);
        fails += each_fail;
        if (each_fail == 0){
            totalMark += case_3_mark;
            System.out.println(ANSI_GREEN + "Test cases in Constructor with NUMBER for NEGATIVE invalid input success" + " ( +" + case_3_mark + " ) " + ANSI_RESET);
        }else{
            System.out.println(ANSI_RED + "Test cases in Constructor with NUMBER for NEGATIVE invalid input fail" + " ( -" + case_3_mark + " ) " + ANSI_RESET);
        }        

        if(fails==0){
            System.out.println(ANSI_PURPLE + "Test constructor with number PASS: ALL" + ANSI_RESET);
        }else{
            System.out.println(ANSI_PURPLE + "Test constructor with number FAILED for " + fails + " case(s)" + ANSI_RESET);
        }
        
        System.out.println(ANSI_PURPLE + "END TEST CONSTRUCTOR WITH NUMBER, TOTAL MARK IS " + totalMark + ANSI_RESET);
        
        return totalMark;
    }
    
    public static double mainPositiveAddition(){ 
        /* 
        Three test cases for positive addition (Total marks: 13)
        1) cases without carry
            i)two numbers have same size, eg. "111222" + "222111" [1.5 mark]
            ii)fisrt number have larger size, eg."111222" + "33" [1.5 mark]
            iii)second number have larger size, eg. "22" + "111333" [1.5 mark]
        2) cases with carry
            i)two numbers have same size, eg. "99999999999" + "99999999999" [2.5 mark]
            ii)fisrt number have larger size, eg."99999999999" + "1" [2.5 mark]
            iii)second number have larger size, eg. "55" + "555555555555" [2.5 mark]
        3) random number addition [1 mark]
            n digit random number add m digit random number
        */
        int fails = 0;
        int each_fail = 0;
        int maxRun = MAX_RUN;
        int[] sizes = SIZE_ARRAY;        
        
        double totalMark = 0; 
        double case_1_1_mark = 1.5;
        double case_1_2_mark = 1.5;
        double case_1_3_mark = 1.5;
        double case_2_1_mark = 2.5;
        double case_2_2_mark = 2.5;
        double case_2_3_mark = 2.5;
        double case_3_mark = 1;
        String msg = "";
        
        System.out.println(ANSI_PURPLE + "BEGIN TEST POSITIVE ADDITION" + ANSI_RESET);
        
        // case 1.1
        // cases without carry
        // have the same size        
        msg = "Addition for the SAME size WITHOUT carry";
        each_fail = testPositiveAddition(sizes,1, 1, 0, 4, maxRun, false, msg);
        fails += each_fail;                
        if (each_fail == 0){
            totalMark += case_1_1_mark;
            System.out.println(ANSI_GREEN + "Test cases in " + msg +" success" + " ( +" + case_1_1_mark + " ) "+ ANSI_RESET);
        }else{
            System.out.println(ANSI_RED + "Test cases in " + msg +" fail" + " ( -" + case_1_1_mark + " ) "+ ANSI_RESET);
        }
        // case 1.2
        // cases without carry
        // fisrt numbers have larger size        
        msg = "Addition for the FIRST number has LARGER size WITHOUT carry";
        each_fail = testPositiveAddition(sizes,2, 1, 0, 4, maxRun, false, msg);
        fails += each_fail;                
        if (each_fail == 0){
            totalMark += case_1_2_mark;
            System.out.println(ANSI_GREEN + "Test cases in " + msg +" success" + " ( +" + case_1_2_mark + " ) "+ ANSI_RESET);
        }else{
            System.out.println(ANSI_RED + "Test cases in " + msg +" fail" + " ( -" + case_1_2_mark + " ) "+ ANSI_RESET);
        }       
        // case 1.3
        // cases without carry
        // second numbers have larger size      
        msg = "Addition for the SECOND number has LARGER size WITHOUT carry";
        each_fail = testPositiveAddition(sizes,1, 2, 0, 4, maxRun, false, msg);
        fails += each_fail;                
        if (each_fail == 0){
            totalMark += case_1_3_mark;
            System.out.println(ANSI_GREEN + "Test cases in " + msg +" success" + " ( +" + case_1_3_mark + " ) " + ANSI_RESET);
        }else{
            System.out.println(ANSI_RED + "Test cases in " + msg +" fail" + " ( -" + case_1_3_mark + " ) " + ANSI_RESET);
        }
        // case 2.1
        // cases with carry
        // have the same size        
        msg = "Addition for the SAME size WITH carry";
        each_fail = testPositiveAddition(sizes,1, 1, 5, 9, maxRun, false, msg);
        fails += each_fail;                
        if (each_fail == 0){
            totalMark += case_2_1_mark;
            System.out.println(ANSI_GREEN + "Test cases in " + msg +" success" + " ( +" + case_2_1_mark + " ) " + ANSI_RESET);
        }else{
            System.out.println(ANSI_RED + "Test cases in " + msg +" fail" + " ( -" + case_2_1_mark + " ) " + ANSI_RESET);
        }
        // case 2.2
        // cases with carry
        // fisrt numbers have larger size        
        msg = "Addition for the FIRST number has LARGER size WITH carry";
        each_fail = testPositiveAddition(sizes,2, 1, 5, 9, maxRun, false, msg);
        fails += each_fail;                
        if (each_fail == 0){
            totalMark += case_2_2_mark;
            System.out.println(ANSI_GREEN + "Test cases in " + msg +" success" + " ( +" + case_2_2_mark + " ) " + ANSI_RESET);
        }else{
            System.out.println(ANSI_RED + "Test cases in " + msg +" fail" + " ( -" + case_2_2_mark + " ) " + ANSI_RESET);
        }       
        // case 2.3
        // cases with carry
        // second numbers have larger size      
        msg = "Addition for the SECOND number has LARGER size WITH carry";
        each_fail = testPositiveAddition(sizes,1, 2, 0, 4, maxRun, false, msg);
        fails += each_fail;                
        if (each_fail == 0){
            totalMark += case_2_3_mark;
            System.out.println(ANSI_GREEN + "Test cases in " + msg +" success" + " ( +" + case_2_3_mark + " ) " + ANSI_RESET);
        }else{
            System.out.println(ANSI_RED + "Test cases in " + msg +" fail" + " ( -" + case_2_3_mark + " ) "+ ANSI_RESET);
        }
        // case 3
        // random number addition
        // n digit random number add m digit random number      
        msg = "Addition for N digit random number add M digit random number";
        each_fail = testPositiveAddition(sizes,1, 1, 0, 9, maxRun, true, msg);
        fails += each_fail;                
        if (each_fail == 0){
            totalMark += case_3_mark;
            System.out.println(ANSI_GREEN + "Test cases in " + msg +" success" + " ( +" + case_3_mark + " ) " + ANSI_RESET);
        }else{
            System.out.println(ANSI_RED + "Test cases in " + msg +" fail" + " ( -" + case_3_mark + " ) " + ANSI_RESET);
        }
        
        if(fails==0){
            System.out.println(ANSI_PURPLE + "Test positive addition PASS: ALL" + ANSI_RESET);
        }else{
            System.out.println(ANSI_PURPLE + "Test positive addition FAILED for " + fails + " case(s)" + ANSI_RESET);
        }        
        
        System.out.println(ANSI_PURPLE + "END TEST POSITIVE ADDITION, TOTAL MARK IS " + totalMark + ANSI_RESET);
        
        return totalMark;
    }        
    
    /**
     * @param sizes the array number of size
     * @param scaleLeft the scale for the left string size
     * @param scaleRight the scale for the right string size
     * @param maxRun max run number
     * @param invalidFlag whether test invalid cases
     * @param negativeInvalidFlag whether test negative invalid cases
     * @return the number of test failed cases
     */        
    public static int testConstructorString(int [] sizes, int scaleLeft, int scaleRight, int maxRun, boolean invalidFlag, boolean negativeInvalidFlag){
        HugeInteger x;
     
        int each_fail = 0;
        
        if(invalidFlag){
            // invalid case test
            for(int n = 0; n < maxRun; n++){
                boolean testFail = false;
                for(int i=0;i<sizes.length;i++){
                    int size = sizes[i];
                    int leftSize = 0;
                    int rightSize = 0;
                    
                    Random random = new Random();
                    
                    leftSize = random.nextInt(size) + 1;
                    leftSize = leftSize * scaleLeft;
                    rightSize = random.nextInt(size) + 1;
                    rightSize = rightSize * scaleRight;
                    String finalNumber = "";
                    String leftNumber = "";
                    String rightNumber = "";
                    String invalidString = getInvalidString(random.nextInt(MAX_INVALID_STRING)+1);
                    if (leftSize == 0 && rightSize == 0) {
                        // generate a empty string
                        finalNumber = "";
                    } else if (leftSize == 0 && rightSize != 0) {
                        // invalid string at the beginning
                        rightNumber = positiveRandomString(rightSize, 0, 9);
                        while(invalidString.length()==1 && invalidString.charAt(0) == '-'){
                            // the minus is valid at the beginning of valid positive string
                            invalidString = getInvalidString(random.nextInt(MAX_INVALID_STRING)+1);
                        }                                
                        finalNumber = leftNumber + invalidString + rightNumber;
                    } else if (leftSize != 0 && rightSize == 0) {
                        // invalid string at the end
                        leftNumber = positiveRandomString(leftSize, 0, 9);
                        finalNumber = leftNumber + invalidString + rightNumber;
                    } else if (leftSize != 0 && rightSize != 0) {
                        // invalid string in the middle
                        leftNumber = positiveRandomString(leftSize, 0, 9);
                        rightNumber = positiveRandomString(rightSize, 0, 9);                            
                        if (negativeInvalidFlag){
                            for (int k = 0; k < leftSize; k++) 
                                finalNumber += '0';
                            finalNumber += '-';
                            finalNumber += rightNumber;
                        } else {
                            finalNumber = leftNumber + invalidString + rightNumber; 
                        }
                    }                
                    
                    try{
                        x = new HugeInteger(finalNumber);
                        // not detect the invalid string
                        System.out.println(ANSI_RED +"Test Constructor with String for invalid case fail ->"+ANSI_RESET);
                        if(finalNumber.length()<=NUM_DISPLAY){
                            System.out.println(ANSI_RED + "Input  string:" + ANSI_RESET + finalNumber);
                        }else{
                            System.out.println(ANSI_CYAN + "Display size limited: " + ANSI_RESET + NUM_DISPLAY);
                        }
                        each_fail++;
                        testFail = true;
                        break;
                    }catch(Exception e){
                        // success
                    }                                      
                }
                if(testFail)
                    break;
            }            
            
        }else{
            // valid case test
            String number = "";
            try{                
                for(int n = 0; n < maxRun; n++){
                    boolean testFail = false;
                    for(int i=0;i<sizes.length;i++){
                        int size = sizes[i];                    
                        number = randomString(size);

                        x = new HugeInteger(number);

                        if(!(number.equals(x.toString()))){
                            System.out.println(ANSI_RED + "Test Constructor with String for valid case fail ->" + ANSI_RESET);
                            if(size<=NUM_DISPLAY){
                                System.out.println(ANSI_RED + "Input  string:"+ ANSI_RESET +number);
                                System.out.println(ANSI_RED + "Output string:"+ ANSI_RESET +x.toString());
                            }else{
                                System.out.println(ANSI_CYAN + "Display size limited: " + ANSI_RESET + NUM_DISPLAY);
                            }
                            each_fail++;
                            testFail = true;
                            break;
                        }
                    }
                    if (testFail)
                        break;
                }                
            }catch(Exception e){
                System.out.println(ANSI_RED + e + ANSI_RESET);
                System.out.println(ANSI_RED + "Input  string:"+ ANSI_RESET + number);
                each_fail++;
            }                                             
        }
        
        return each_fail;
    }    
    
    /**
     * @param sizes the array number of size
     * @param scale to generate positice, zero and negative numbers
     * @param maxRun max run number
     * @return the number of test failed cases
     */        
    public static int testConstructorNumber(int [] sizes, int scale, int maxRun){
        HugeInteger x;
        int each_fail = 0;
  
        if (scale > 0){
            // valid case test
            int size = 0;
            try{
                for(int n = 0; n < maxRun; n++){
                    for(int i=0;i<sizes.length;i++){
                        Random random = new Random();
                        size = random.nextInt(sizes[i]) + 1;
                        size = scale * size;
                        x = new HugeInteger(size);
                    }
                }
            }catch(Exception e){
                System.out.println(ANSI_RED + e + ANSI_RESET);
                System.out.println(ANSI_RED + "Test Constructor with NUMBER for valid case fail ->" + ANSI_RESET);
                System.out.println(ANSI_RED + "Input  size:"+ ANSI_RESET + size);
                each_fail++;
            }        
        
        }else{
            //invalid case test
            for(int n = 0; n < maxRun; n++){
                boolean testFail = false;
                for(int i=0;i<sizes.length;i++){
                    Random random = new Random();
                    int size = random.nextInt(sizes[i]) + 1;
                    size = scale * size;
                    
                    try{
                        x = new HugeInteger(size);
                        // not detect the invalid size
                        System.out.println(ANSI_RED +"Test Constructor with NUMBER for invalid case fail ->"+ANSI_RESET);
                        System.out.println(ANSI_RED +"Input  size:"+ANSI_RESET+size);
                        each_fail++;
                        testFail = true;
                        break;                        
                    }catch(Exception e){
                        // success
                    }                                        
                }
                if(testFail)
                    break;
            }
        }
                       
        return each_fail;
    }
    
    /**
     * @param sizes the array number of size
     * @param scaleSmall the scale for the small size
     * @param scaleLarge the scale for the small size
     * @param min the lower bound (inclusive) for digit in the string
     * @param max the upper bound (inclusive) for digit in the string
     * @param maxRun max run number
     * @param randomFlag whether generate random size number
     * @param msg display message
     * @return the number of test failed cases
     */
    public static int testPositiveAddition(int [] sizes, int scaleSmall, int scaleLarge, int min, int max, int maxRun, boolean randomFlag, String msg){
        HugeInteger x1;
        HugeInteger x2;
        HugeInteger x3;

        BigInteger y1;
        BigInteger y2;
        BigInteger y3;
        
        String number1 = "";
        String number2 = "";
        
        int failNum = 0;
        int samllSize = 0;
        int largeSize = 0;
        
        try{
            for(int n = 0; n < maxRun; n++){
                boolean testFail = false;
                for(int i=0;i<sizes.length;i++){                
                    samllSize = scaleSmall * sizes[i];
                    largeSize = scaleLarge * sizes[i];

                    if (randomFlag){
                        Random random = new Random();
                        samllSize = random.nextInt(samllSize) + 1;
                        largeSize = random.nextInt(largeSize) + 1;
                    }

                    number1 = positiveRandomString(samllSize, min, max);
                    number2 = positiveRandomString(largeSize, min, max);
                                       
                    y1 = new BigInteger(number1);
                    y2 = new BigInteger(number2);
                    y3 = y1.add(y2);

                    x1 = new HugeInteger(number1);
                    x2 = new HugeInteger(number2);
                    x3 = x1.add(x2);

                    //For lab1, the leading zeros are ok, don't deduct marks.
                    String x3S = removeLeadingZeros(x3.toString());
                    //String x3S = x3.toString();

                    if(!(y3.toString().equals(x3S))){
                        System.out.println(ANSI_RED + "Error in " + msg + " ->"+ANSI_RESET);
                        if(largeSize<=NUM_DISPLAY){
                            System.out.println(ANSI_RED + "Input1(+): " + ANSI_RESET + number1);
                            System.out.println(ANSI_RED + "Input2(+): "+ ANSI_RESET + number2);
                            System.out.println(ANSI_RED + "Expected output= "+ ANSI_RESET + y3.toString());
                            System.out.println(ANSI_RED + "Current  output= "+ ANSI_RESET + x3S);
                        }else{
                            System.out.println(ANSI_CYAN + "Display size limited: " + ANSI_RESET + NUM_DISPLAY);
                        }
                        failNum++;
                        testFail = true;
                        break;
                    }
                }
                if(testFail)
                    break;
            }
        }
        catch(Exception e){
            System.out.println(ANSI_RED + e + ANSI_RESET);
            System.out.println(ANSI_RED + "Input1(+): " + ANSI_RESET + number1);
            System.out.println(ANSI_RED + "Input2(+): "+ ANSI_RESET + number2);            
            failNum++;
        }
        return failNum;
    }
    
    public static String randomString(int size){
        boolean neg = false;
        Random random = new Random();

        String rand_string = "";
        if (size < 1){
            throw new NumberFormatException("randomString input size should >= 1");
        }else if (size == 1){
            rand_string += random.nextInt(10);
        }else{
            rand_string += random.nextInt(9) + 1; // The first number cannot be zero
            for(int i=1;i<=size-1;i++){
                int next_digit = random.nextInt(10);
                rand_string = rand_string+next_digit;
            }            
        }
        
        if(random.nextInt(9)<5 && rand_string.charAt(0) != '0'){ // don't generate "-0"
            rand_string = "-"+rand_string;
        }
        return rand_string;
    }
    /*
    @param size length of the String
    @param min the lower bound (inclusive)
    @param max the upper bound (inclusive)
    */
    public static String positiveRandomString(int size, int min, int max){
        Random random = new Random();

        String rand_string = "";
        
        if (min < 0 || max > 9)
            throw new NumberFormatException("positiveRandomString input min/max error");
        
        if (size < 1){
            throw new NumberFormatException("positiveRandomString input size should >= 1");
        } else {
            if (min == 0){
                rand_string += random.nextInt((max - min)) + min + 1; // The first number cannot be zero
                for(int i=1;i<=size-1;i++){
                    int next_digit = random.nextInt((max - min) + 1) + min; // Other numbers can be zero
                    rand_string = rand_string+next_digit;
                }
            } else {
                for(int i=1;i<=size;i++){
                    int next_digit = random.nextInt((max - min) + 1) + min; // all numbers can be min
                    rand_string = rand_string+next_digit;
                }                
            }
        }

        return rand_string;
    }

    public static String removeLeadingZeros(String val){
        int len = val.length();
        String newVal = "";
        for (int i = 0; i < len; i++){
            if (i == len - 1) {
                newVal = val.substring(i);
            } else if (val.charAt(i) != '0'){
                newVal = val.substring(i);
                break;                
            }
        }
        return newVal;
    }
    
    public static String getInvalidString(int n){
        Random r = new Random();
        byte[] b = new byte[n];
        for (int i = 0; i < n;){
            int number = r.nextInt(127-32) + 32;
            if((number >= 48 && number <= 57)){
                // valid char
                continue; 
            } else {
                b[i] = (byte)number;
                i++;
            }            
        }
        return new String(b);
    }    
}
