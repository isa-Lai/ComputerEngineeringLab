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
public class TestHugeInteger2 {
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
    
    // when test failed, the size of number <= NUM_DISPLAY, display the number
    public static final int NUM_DISPLAY = 1000;
    // iterate the MAX_SIZE_ARRAY MAX_RUN times
    public static final int MAX_RUN = 50;
    // use for generating invalid string for constructor 1 (with string input)
    public static final int MAX_INVALID_STRING = 5;
    // different size for test
    public static final int[] MAX_SIZE_ARRAY = new int[] {1,20,50,500,5000};

    public static void main(String args[]){
        double totalMark = 0;
        
        /*
         ********************
         *  Lab 1 test code *
         ********************
         */ 
//        // Test constructor 1 (with string input)
//        totalMark += mainConstructorsString();
//        // Test constructor 2 (with number input)
//        totalMark += mainConstructorsNumber();
//        // Test positive addition
//        totalMark += mainPositiveAddition();

        /*
         ********************
         *  Lab 2 test code *
         ********************
         */
        // Test addition and subtraction
        totalMark += mainAdditionSubtraction();
        // Test compareTo
        totalMark += mainCompareTo();
        // Test multiplication
        totalMark += mainMultiplication();
        
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
        int[] sizes = MAX_SIZE_ARRAY;

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
        int[] sizes = MAX_SIZE_ARRAY;

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
            i)two numbers have same size, e.g. "111222" + "222111" [1.5 mark]
            ii)fisrt number have larger size, e.g."111222" + "33" [1.5 mark]
            iii)second number have larger size, e.g. "22" + "111333" [1.5 mark]
        2) cases with carry
            i)two numbers have same size, e.g. "99999999999" + "99999999999" [2.5 mark]
            ii)fisrt number have larger size, e.g."99999999999" + "1" [2.5 mark]
            iii)second number have larger size, e.g. "55" + "555555555555" [2.5 mark]
        3) random number addition [1 mark]
            n digit random number add m digit random number
        */
        int fails = 0;
        int each_fail = 0;
        int maxRun = MAX_RUN;
        int[] sizes = MAX_SIZE_ARRAY;        
        
        double totalMark = 0; 
        double case_1_1_mark = 1.5;
        double case_1_2_mark = 1.5;
        double case_1_3_mark = 1.5;
        double case_2_1_mark = 2.5;
        double case_2_2_mark = 2.5;
        double case_2_3_mark = 2.5;
        double case_3_mark = 1;
        String msg;
        
        System.out.println(ANSI_PURPLE + "BEGIN TEST POSITIVE ADDITION" + ANSI_RESET);
        
        // case 1.1
        // cases without carry
        // have the same size        
        msg = "Addition for the SAME size WITHOUT carry";
        each_fail = testPositiveAddition(sizes, 1, 1, 0, 4, maxRun, false, msg);
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
        each_fail = testPositiveAddition(sizes, 2, 1, 0, 4, maxRun, false, msg);
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
        each_fail = testPositiveAddition(sizes, 1, 2, 0, 4, maxRun, false, msg);
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
        each_fail = testPositiveAddition(sizes, 1, 1, 5, 9, maxRun, false, msg);
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
        each_fail = testPositiveAddition(sizes, 2, 1, 5, 9, maxRun, false, msg);
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
        each_fail = testPositiveAddition(sizes, 1, 2, 0, 4, maxRun, false, msg);
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
        each_fail = testPositiveAddition(sizes, 1, 1, 0, 9, maxRun, true, msg);
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
        
    public static double mainAdditionSubtraction(){ 
        /* 
        Three main test cases for addition and subtraction (Total marks: 15, for each sub test case, 0.5 mark for both add & subtract)
        1) same input sizes
            i)same sign without borrow or carry 
            ii)same sign with borrow or carry 
            iii)different sign without borrow or carry 
            iv)different sign with borrow or carry 
            v)zero
        2) the size of first input integer larger than second
            i)same sign without borrow or carry 
            ii)same sign with borrow or carry 
            iii)different sign without borrow or carry 
            iv)different sign with borrow or carry 
            v)zero
        3) the size of first input integer less than second
            i)same sign without borrow or carry 
            ii)same sign with borrow or carry 
            iii)different sign without borrow or carry 
            iv)different sign with borrow or carry 
            v)zero
        */
        int fails = 0;
        int each_fail = 0;
        int maxRun = MAX_RUN;
        int[] sizes = MAX_SIZE_ARRAY;
        
        double totalMark = 0; 
        double case_1_1_mark = 1;
        double case_1_2_mark = 1;
        double case_1_3_mark = 1;
        double case_1_4_mark = 1;
        double case_1_5_mark = 1;
        double case_2_1_mark = 1;
        double case_2_2_mark = 1;
        double case_2_3_mark = 1;
        double case_2_4_mark = 1;
        double case_2_5_mark = 1;
        double case_3_1_mark = 1;
        double case_3_2_mark = 1;
        double case_3_3_mark = 1;
        double case_3_4_mark = 1;
        double case_3_5_mark = 1;

        double[][] caseMarks = new double[][] { {case_1_1_mark,case_1_2_mark,case_1_3_mark,case_1_4_mark,case_1_5_mark}, // same size
                                                {case_2_1_mark,case_2_2_mark,case_2_3_mark,case_2_4_mark,case_2_5_mark}, // the size of first input integer larger than second
                                                {case_3_1_mark,case_3_2_mark,case_3_3_mark,case_3_4_mark,case_3_5_mark}}; // the size of first input integer less than second
        String[] mainCaseMsg = {"SAME SIZE",
                                "FIRST SIZE LARGER",
                                "SECOND SIZE LARGER"};
        String[] subCaseMsg = { "SAME Sign WITHOUT Borrow or Carry",
                                "SAME Sign WITH Borrow or Carry",
                                "DIFFERENT Sign WITHOUT Borrow or Carry",
                                "DIFFERENT Sign WITH Borrow or Carry",
                                "ZERO"};
        String[] mainCaseTestType = {"Equal",
                                     "Larger",
                                     "Smaller"};        
        String[] subCaseTestType = {"SameSignWithoutBorrowCarry",
                                    "SameSignWithBorrowCarry",
                                    "DiffSignWithoutBorrowCarry",
                                    "DiffSignWithBorrowCarry",
                                    "Zero"};
        String msg;
                
        System.out.println(ANSI_PURPLE + "BEGIN TEST ADDITION AND SUBTRACTION" + ANSI_RESET);
                       
        for (int mainIdx = 0; mainIdx < mainCaseTestType.length; mainIdx++ ){
            // main cases:
            // 1) same input sizes
            // 2) the size of first input integer larger than second
            // 3) the size of first input integer less than second
            for (int subIdx = 0; subIdx < subCaseTestType.length; subIdx++){
                // sub cases: (test addition and subtraction)
                // i)same sign without borrow or carry 
                // ii)same sign with borrow or carry 
                // iii)different sign without borrow or carry 
                // iv)different sign with borrow or carry 
                // v)zero
                boolean addFlag = true;
                msg = "Addition for " + mainCaseMsg[mainIdx] +" and "+ subCaseMsg[subIdx];
                each_fail = testAdditionSubtraction(sizes, maxRun, addFlag, mainCaseTestType[mainIdx], subCaseTestType[subIdx], msg);
                fails += each_fail;
                if (each_fail == 0){
                    totalMark += caseMarks[mainIdx][subIdx]/2;
                    System.out.println(ANSI_GREEN + "Test cases in " + msg +" success" + " ( +" + caseMarks[mainIdx][subIdx]/2 + " ) "+ ANSI_RESET);
                }else{
                    System.out.println(ANSI_RED + "Test cases in " + msg +" fail" + " ( -" + caseMarks[mainIdx][subIdx]/2 + " ) "+ ANSI_RESET);
                }
                msg = "Subtraction for " + mainCaseMsg[mainIdx] +" and "+ subCaseMsg[subIdx];
                each_fail = testAdditionSubtraction(sizes, maxRun, !addFlag, mainCaseTestType[mainIdx], subCaseTestType[subIdx], msg);                
                fails += each_fail;
                if (each_fail == 0){
                    totalMark += caseMarks[mainIdx][subIdx]/2;
                    System.out.println(ANSI_GREEN + "Test cases in " + msg +" success" + " ( +" + caseMarks[mainIdx][subIdx]/2 + " ) "+ ANSI_RESET);
                }else{
                    System.out.println(ANSI_RED + "Test cases in " + msg +" fail" + " ( -" + caseMarks[mainIdx][subIdx]/2 + " ) "+ ANSI_RESET);
                }
            }
        }
                
        if(fails==0){
            System.out.println(ANSI_PURPLE + "Test addition and subtraction PASS: ALL" + ANSI_RESET);
        }else{
            System.out.println(ANSI_PURPLE + "Test addition and subtraction FAILED for " + fails + " case(s)" + ANSI_RESET);
        }        
        
        System.out.println(ANSI_PURPLE + "END TEST ADDITION AND SUBTRACTION, TOTAL MARK IS " + totalMark + ANSI_RESET);
        
        return totalMark;
    }    
    
    public static double mainCompareTo(){ 
        /* 
        Three main test cases for compareTo (Total marks: 5)
        1) two inputs are equal
            i)  0==0 [0.5 mark]
            ii) -==-/+==+ [0.5 mark]
        2) first input larger than second input
            i)  ->- [0.5 mark]
            ii) +>+ [0.5 mark]
            iii)+>0 [0.5 mark]
            iv) +>- [0.5 mark]
        3) first input smaller than second input
            i)  -<- [0.5 mark]
            ii) +<+ [0.5 mark]
            iii)-<0 [0.5 mark]
            iv) -<+ [0.5 mark]
        */
        int fails = 0;
        int each_fail = 0;
        int maxRun = MAX_RUN;
        int[] sizes = MAX_SIZE_ARRAY;        
        
        double totalMark = 0; 
        double case_1_1_mark = 0.5;
        double case_1_2_mark = 0.5;
        double case_2_1_mark = 0.5;
        double case_2_2_mark = 0.5;
        double case_2_3_mark = 0.5;
        double case_2_4_mark = 0.5;
        double case_3_1_mark = 0.5;
        double case_3_2_mark = 0.5;
        double case_3_3_mark = 0.5;
        double case_3_4_mark = 0.5;
        
        double[][] caseMarks = new double[][] { {case_1_1_mark,case_1_2_mark}, // same size
                                                {case_2_1_mark,case_2_2_mark,case_2_3_mark,case_2_4_mark}, // the size of first input integer larger than second
                                                {case_3_1_mark,case_3_2_mark,case_3_3_mark,case_3_4_mark}}; // the size of first input integer less than second
        String[] mainCaseMsg = {"Equal",
                                "FIRST LARGER",
                                "FIRST SMALLER"};
        String[][] subCaseMsg = {   {"Zero = Zero","Positive/Negative = Positive/Negative"},
                                    {"Negative > Negative","Positive > Positive","Positive > Zero","Positive > Negative"},
                                    {"Negative < Negative","Positive < Positive","Negative < Zero","Negative < Positive"}};        
        String[] mainCaseTestType = {"Equal","Larger", "Smaller"};        
        String[][] subCaseTestType = {  {"0==0","-+==+-"},
                                        {"->-","+>+","+>0","+>-"},
                                        {"-<-","+<+","-<0","-<+"}};
        String msg;
                
        System.out.println(ANSI_PURPLE + "BEGIN TEST COMPARETO" + ANSI_RESET);
                       
        for (int mainIdx = 0; mainIdx < mainCaseTestType.length; mainIdx++ ){
            // main cases:
            // 1) two inputs are equal
            // 2) first input larger than second input
            // 3) first input smaller than second input
            for (int subIdx = 0; subIdx < subCaseTestType[mainIdx].length; subIdx++){
                // sub cases:
                // 1) 0==0, -+==+- [1 mark]
                // 2) ->-, +>+, +>0, +>- [2 mark]
                // 3) -<-, +<+, -<0, -<+ [2 mark]
                msg = "Compare for " + mainCaseMsg[mainIdx] +" and "+ subCaseMsg[mainIdx][subIdx];
                each_fail = testCompareTo(sizes, maxRun, mainCaseTestType[mainIdx], subCaseTestType[mainIdx][subIdx], msg);
                fails += each_fail;
                if (each_fail == 0){
                    totalMark += caseMarks[mainIdx][subIdx];
                    System.out.println(ANSI_GREEN + "Test cases in " + msg +" success" + " ( +" + caseMarks[mainIdx][subIdx] + " ) "+ ANSI_RESET);
                }else{
                    System.out.println(ANSI_RED + "Test cases in " + msg +" fail" + " ( -" + caseMarks[mainIdx][subIdx] + " ) "+ ANSI_RESET);
                }
            }
        }
                
        if(fails==0){
            System.out.println(ANSI_PURPLE + "Test compareTo PASS: ALL" + ANSI_RESET);
        }else{
            System.out.println(ANSI_PURPLE + "Test compareTo FAILED for " + fails + " case(s)" + ANSI_RESET);
        }        
        
        System.out.println(ANSI_PURPLE + "END TEST COMPARETO, TOTAL MARK IS " + totalMark + ANSI_RESET);
        
        return totalMark;
    }    
    
    public static double mainMultiplication(){ 
        /* 
        Three main test cases for multiplication (Total marks: 15, 1 mark for each)
        1) same input sizes
            i)same sign without carry 
            ii)same sign with carry 
            iii)different sign without carry 
            iv)different sign with carry 
            v)multiply zero
        2) the size of first input integer larger than second
            i)same sign without carry 
            ii)same sign with carry 
            iii)different sign without carry 
            iv)different sign with carry 
            v)multiply zero
        3) the size of first input integer less than second
            i)same sign without carry 
            ii)same sign with carry 
            iii)different sign without carry 
            iv)different sign with carry 
            v)multiply zero
        */
        int fails = 0;
        int each_fail = 0;
        int maxRun = MAX_RUN;
        int[] sizes = MAX_SIZE_ARRAY;
        
        double totalMark = 0; 
        double case_1_1_mark = 1;
        double case_1_2_mark = 1;
        double case_1_3_mark = 1;
        double case_1_4_mark = 1;
        double case_1_5_mark = 1;
        double case_2_1_mark = 1;
        double case_2_2_mark = 1;
        double case_2_3_mark = 1;
        double case_2_4_mark = 1;
        double case_2_5_mark = 1;
        double case_3_1_mark = 1;
        double case_3_2_mark = 1;
        double case_3_3_mark = 1;
        double case_3_4_mark = 1;
        double case_3_5_mark = 1;
        
        double[][] caseMarks = new double[][] { {case_1_1_mark,case_1_2_mark,case_1_3_mark,case_1_4_mark,case_1_5_mark}, // same size
                                                {case_2_1_mark,case_2_2_mark,case_2_3_mark,case_2_4_mark,case_2_5_mark}, // the size of first input integer larger than second
                                                {case_3_1_mark,case_3_2_mark,case_3_3_mark,case_3_4_mark,case_3_5_mark}}; // the size of first input integer less than second
        String[] mainCaseMsg = {"SAME SIZE",
                                "FIRST SIZE LARGER",
                                "SECOND SIZE LARGER"};       
        String[] subCaseMsg = { "SAME Sign WITHOUT Carry",
                                "SAME Sign WITH Carry",
                                "DIFFERENT Sign WITHOUT Carry",
                                "DIFFERENT Sign WITH Carry",
                                "Multiply ZERO"};         
        String[] mainCaseTestType = {"Equal",
                                     "Larger",
                                     "Smaller"};        
        String[] subCaseTestType = {"SameSignWithoutCarry",
                                    "SameSignWithCarry",
                                    "DiffSignWithoutCarry",
                                    "DiffSignWithCarry",
                                    "Zero"};        
        String msg;
                
        System.out.println(ANSI_PURPLE + "BEGIN TEST MULTIPLICATION" + ANSI_RESET);
                
        for (int mainIdx = 0; mainIdx < mainCaseTestType.length; mainIdx++ ){
            // main cases:
            // 1) same input sizes
            // 2) the size of first input integer larger than second
            // 3) the size of first input integer less than second
            for (int subIdx = 0; subIdx < subCaseTestType.length; subIdx++){
                // sub cases: 
                // i)same sign without carry 
                // ii)same sign with carry 
                // iii)different sign without carry 
                // iv)different sign with carry 
                // v)multiply zero              
                msg = "Multiplication for " + mainCaseMsg[mainIdx] +" and "+ subCaseMsg[subIdx];
                each_fail = testMultiplication(sizes, maxRun, mainCaseTestType[mainIdx], subCaseTestType[subIdx], msg);
                fails += each_fail;
                if (each_fail == 0){
                    totalMark += caseMarks[mainIdx][subIdx];
                    System.out.println(ANSI_GREEN + "Test cases in " + msg +" success" + " ( +" + caseMarks[mainIdx][subIdx] + " ) "+ ANSI_RESET);
                }else{
                    System.out.println(ANSI_RED + "Test cases in " + msg +" fail" + " ( -" + caseMarks[mainIdx][subIdx] + " ) "+ ANSI_RESET);
                }
            }
        }
                
        if(fails==0){
            System.out.println(ANSI_PURPLE + "Test multiplication PASS: ALL" + ANSI_RESET);
        }else{
            System.out.println(ANSI_PURPLE + "Test multiplication FAILED for " + fails + " case(s)" + ANSI_RESET);
        }        
        
        System.out.println(ANSI_PURPLE + "END TEST MULTIPLICATION, TOTAL MARK IS " + totalMark + ANSI_RESET);
        
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
                        // generate a empty string, and "-", "!@#",etc. not test
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
     * @param scale to generate positive, zero and negative numbers
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
                    boolean testFail = false;
                    for(int i=0;i<sizes.length;i++){
                        Random random = new Random();
                        size = random.nextInt(sizes[i]) + 1;
                        size = scale * size;
                        x = new HugeInteger(size);
                        
                        // Check whether x is valid or not
                        BigInteger y = new BigInteger(x.toString());
                        String ys = y.toString();
                        int numLength = 0;
                        if (ys.charAt(0) == '-') // negative number
                            numLength = ys.length() - 1;
                        else
                            numLength = ys.length();
                        
                        if(numLength!=size){
                            System.out.println(ANSI_RED + "Test Constructor with Number for valid case fail ->" + ANSI_RESET);
                            if(size<=NUM_DISPLAY){
                                System.out.println(ANSI_RED + "Input    size:"+ ANSI_RESET +size);
                                System.out.println(ANSI_RED + "Output string:"+ ANSI_RESET +x.toString());
                            }else{
                                System.out.println(ANSI_CYAN + "Display size limited: " + ANSI_RESET + NUM_DISPLAY);
                            }
                            each_fail++;
                            testFail = true;
                            break;
                        }
                    }
                    if(testFail)
                        break;                    
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
    
    /**
     * @param sizes the array number of size
     * @param maxRun max run number
     * @param addFlag addition or subtraction
     * @param mainTestType main test type
     * @param subTestType sub test type
     * @param msg display message
     * @return the number of test failed cases
     */
    public static int testAdditionSubtraction(int[] sizes, int maxRun, boolean addFlag, String mainTestType, String subTestType, String msg){                
        int failNum = 0;
        
        for(int n = 0; n < maxRun; n++){
            boolean testFail = false;
            for(int i=0;i<sizes.length;i++){
                Random random = new Random();
                int smallSize = random.nextInt(sizes[i]) + 1;
                int largeSize = random.nextInt(smallSize) + smallSize + 1;  // generate a number [smallSzie+1,2*smallSzie]                

                switch(mainTestType){
                    case "Equal":{
                        switch(subTestType){
                            case "SameSignWithoutBorrowCarry":{
                                // same input sizes                                
                                String number1P_5_9 = positiveRandomString(smallSize, 5, 9);
                                String number1P_0_4 = positiveRandomString(smallSize, 0, 4);
                                String number2P_0_5 = positiveRandomString(smallSize, 0, 5);                                
                                String number1N_5_9 = "-" + number1P_5_9;
                                String number1N_0_4 = "-" + number1P_0_4;
                                String number2N_0_5 = "-" + number2P_0_5;
                                if (addFlag){
                                    //not test +/+
                                    failNum += testAdditionSubtractionExd(number1N_0_4, number2N_0_5, addFlag, msg); // -/- : addition no carry                            
                                }else{
                                    //test subtraction: +/+,-/-
                                    failNum += testAdditionSubtractionExd(number1P_5_9, number2P_0_5, addFlag, msg); // +/+ : subtraction no borrow
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N_5_9, number2N_0_5, addFlag, msg); // -/- : subtraction no borrow
                                }                                                                 
                                break;
                            }
                            case "SameSignWithBorrowCarry":{
                                // same input sizes
                                String numberMS_5_9 = positiveRandomString(1, 5, 9); // most significant digit
                                String numberMS_1_4 = positiveRandomString(1, 1, 4); // most significant digit
                                
                                String number1P_5_9 = positiveRandomString(smallSize, 5, 9);
                                String number1P_0_4 = positiveRandomString(smallSize, 0, 4);
                                String number2P_5_9 = positiveRandomString(smallSize, 5, 9);                                                                                                                                
                                String number1P9_0_4 = numberMS_5_9 + number1P_0_4;
                                String number2P1_5_9 = numberMS_1_4 + number2P_5_9;
                                                                                             
                                String number1N_5_9 = "-" + number1P_5_9;                                
                                String number1N9_0_4 = "-" + number1P9_0_4;
                                String number2N_5_9 = "-" + number2P_5_9;
                                String number2N1_5_9 = "-" + number2P1_5_9;                                                              
                                
                                if (addFlag){
                                    //not test +/+
                                    failNum += testAdditionSubtractionExd(number1N_5_9, number2N_5_9, addFlag, msg); // -/- : addition carry                            
                                }else{
                                    //test subtraction: +/+,-/-
                                    failNum += testAdditionSubtractionExd(number1P9_0_4, number2P1_5_9, addFlag, msg); // +/+ : subtraction borrow
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N9_0_4, number2N1_5_9, addFlag, msg); // -/- : subtraction borrow
                                }                                 
                                break;
                            }
                            case "DiffSignWithoutBorrowCarry":{
                                // same input sizes
                                String number1P_5_9 = positiveRandomString(smallSize, 5, 9);
                                String number1P_0_4 = positiveRandomString(smallSize, 0, 4);
                                String number2P_0_5 = positiveRandomString(smallSize, 0, 5);
                                
                                String number1N_5_9 = "-" + number1P_5_9;
                                String number1N_0_4 = "-" + number1P_0_4;
                                String number2N_0_5 = "-" + number2P_0_5;
                                if (addFlag){
                                    //test addition: +/-,-/+
                                    failNum += testAdditionSubtractionExd(number1P_5_9, number2N_0_5, addFlag, msg); // +/- : addition no borrow
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N_5_9, number2P_0_5, addFlag, msg); // -/+ : addition no borrow                          
                                }else{
                                    //test subtraction: +/-,-/+
                                    failNum += testAdditionSubtractionExd(number1P_0_4, number2N_0_5, addFlag, msg); // +/- : subtraction no carry
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N_0_4, number2P_0_5, addFlag, msg); // -/+ : subtraction no carry
                                }                                 
                                break;
                            }
                            case "DiffSignWithBorrowCarry":{
                                // same input sizes
                                String numberMS_5_9 = positiveRandomString(1, 5, 9); // most significant digit
                                String numberMS_1_4 = positiveRandomString(1, 1, 4); // most significant digit
                                
                                String number1P_5_9 = positiveRandomString(smallSize, 5, 9);
                                String number1P_0_4 = positiveRandomString(smallSize, 0, 4);
                                String number2P_5_9 = positiveRandomString(smallSize, 5, 9);                                                                                                                                
                                String number1P9_0_4 = numberMS_5_9 + number1P_0_4;
                                String number2P1_5_9 = numberMS_1_4 + number2P_5_9;
                                                                                             
                                String number1N_5_9 = "-" + number1P_5_9;                                
                                String number1N9_0_4 = "-" + number1P9_0_4;
                                String number2N_5_9 = "-" + number2P_5_9;
                                String number2N1_5_9 = "-" + number2P1_5_9;                                                              
                                
                                if (addFlag){
                                    //test addition: +/-,-/+
                                    failNum += testAdditionSubtractionExd(number1P9_0_4, number2N1_5_9, addFlag, msg); // +/- : addition borrow
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N9_0_4, number2P1_5_9, addFlag, msg); // -/+ : addition borrow                           
                                }else{
                                    //test subtraction: +/-,-/+
                                    failNum += testAdditionSubtractionExd(number1P_5_9, number2N_5_9, addFlag, msg); // +/- : subtraction carry
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N_5_9, number2P_5_9, addFlag, msg); // -/+ : subtraction carry
                                }                                 
                                break;
                            }
                            case "Zero":{
                                // same input sizes
                                String number = randomString(1);
                                failNum += testAdditionSubtractionExd("0", "0", addFlag, msg); // 0+/-0
                                if(failNum > 0) {break;}                            
                                failNum += testAdditionSubtractionExd(number, "0", addFlag, msg);  // n+/-0
                                if(failNum > 0) {break;}
                                failNum += testAdditionSubtractionExd("0", number, addFlag, msg);  // 0+/-n
                                if(failNum > 0) {break;}
                                if (addFlag == false){
                                    number = randomString(smallSize);
                                    failNum += testAdditionSubtractionExd(number, number, addFlag, msg); // n-n
                                }                                
                                break;
                            }
                            default:
                                throw new NumberFormatException("[testAdditionSubtraction] Equal subTestType error");
                        }
                        break;
                    }
                    case "Larger":{
                        switch(subTestType){
                            case "SameSignWithoutBorrowCarry":{
                                // the size of first input integer larger than second
                                String number1P_5_9 = positiveRandomString(largeSize, 5, 9);
                                String number1P_0_4 = positiveRandomString(largeSize, 0, 4);
                                String number2P_0_5 = positiveRandomString(smallSize, 0, 5);

                                String number1N_5_9 = "-" + number1P_5_9;
                                String number1N_0_4 = "-" + number1P_0_4;
                                String number2N_0_5 = "-" + number2P_0_5;                                
                                if (addFlag){
                                    //test addition: -/-, not test +/+
                                    failNum += testAdditionSubtractionExd(number1N_0_4, number2N_0_5, addFlag, msg); // -/- : addition no carry                            
                                }else{
                                    //test subtraction: +/+,-/-
                                    failNum += testAdditionSubtractionExd(number1P_5_9, number2P_0_5, addFlag, msg); // +/+ : subtraction no borrow
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N_5_9, number2N_0_5, addFlag, msg); // -/- : subtraction no borrow
                                }                                
                                break;
                            }
                            case "SameSignWithBorrowCarry":{
                                // the size of first input integer larger than second
                                String number1P_0_4 = positiveRandomString(largeSize, 0, 4);
                                String number1P_5_9 = positiveRandomString(largeSize, 5, 9);
                                String number2P_5_9 = positiveRandomString(smallSize, 5, 9);

                                String number1N_0_4 = "-" + number1P_0_4;
                                String number1N_5_9 = "-" + number1P_5_9;
                                String number2N_5_9 = "-" + number2P_5_9;

                                if (addFlag){
                                    //test addition: -/-, not test +/+
                                    failNum += testAdditionSubtractionExd(number1N_5_9, number2N_5_9, addFlag, msg); // -/- : addition carry                            
                                }else{
                                    //test subtraction: +/+, -/-
                                    failNum += testAdditionSubtractionExd(number1P_0_4, number2P_5_9, addFlag, msg); // +/+ : subtraction borrow
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N_0_4, number2N_5_9, addFlag, msg); // -/- : subtraction borrow
                                }                                 
                                break;
                            }
                            case "DiffSignWithoutBorrowCarry":{
                                // the size of first input integer larger than second
                                String number1P_5_9 = positiveRandomString(largeSize, 5, 9);
                                String number1P_0_4 = positiveRandomString(largeSize, 0, 4);
                                String number2P_0_5 = positiveRandomString(smallSize, 0, 5);

                                String number1N_5_9 = "-" + number1P_5_9;
                                String number1N_0_4 = "-" + number1P_0_4;
                                String number2N_0_5 = "-" + number2P_0_5;                                
                                if (addFlag){
                                    //test addition: +/-,-/+
                                    failNum += testAdditionSubtractionExd(number1P_5_9, number2N_0_5, addFlag, msg); // +/- : addition no borrow
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N_5_9, number2P_0_5, addFlag, msg); // -/+ : addition no borrow                           
                                }else{
                                    //test subtraction: +/-,-/+
                                    failNum += testAdditionSubtractionExd(number1P_0_4, number2N_0_5, addFlag, msg); // +/- : subtraction no carry
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N_0_4, number2P_0_5, addFlag, msg); // -/+ : subtraction no carry
                                }                                
                                break;
                            }
                            case "DiffSignWithBorrowCarry":{
                                // the size of first input integer larger than second
                                String number1P_0_4 = positiveRandomString(largeSize, 0, 4);
                                String number1P_5_9 = positiveRandomString(largeSize, 5, 9);
                                String number2P_5_9 = positiveRandomString(smallSize, 5, 9);

                                String number1N_0_4 = "-" + number1P_0_4;
                                String number1N_5_9 = "-" + number1P_5_9;
                                String number2N_5_9 = "-" + number2P_5_9;

                                if (addFlag){
                                    //test addition: +/-,-/+,
                                    failNum += testAdditionSubtractionExd(number1P_0_4, number2N_5_9, addFlag, msg); // +/- : addition borrow
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N_0_4, number2P_5_9, addFlag, msg); // -/+ : addition borrow                           
                                }else{
                                    //test subtraction: +/+,+/-,-/+,-/-
                                    failNum += testAdditionSubtractionExd(number1P_5_9, number2N_5_9, addFlag, msg); // +/- : subtraction carry
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N_5_9, number2P_5_9, addFlag, msg); // -/+ : subtraction carry
                                }                                 
                                break;
                            }
                            case "Zero":{
                                // the size of first input integer larger than second
                                String number = randomString(largeSize);                            
                                failNum += testAdditionSubtractionExd(number, "0", addFlag, msg); // n+/-0                                
                                break;
                            }
                            default:
                                throw new NumberFormatException("[testAdditionSubtraction] Larger subTestType error");
                        }                        
                        break;
                    }
                    case "Smaller":{
                        switch(subTestType){
                            case "SameSignWithoutBorrowCarry":{
                                // the size of first input integer less than second
                                String number1P_0_4 = positiveRandomString(smallSize, 0, 4);
                                String number2P_0_5 = positiveRandomString(largeSize, 0, 5);
                                String number2P_5_9 = positiveRandomString(largeSize, 5, 9);

                                String number1N_0_4 = "-" + number1P_0_4;
                                String number2N_0_5 = "-" + number2P_0_5;
                                String number2N_5_9 = "-" + number2P_5_9;

                                if (addFlag){
                                    //test addition: -/-, not test +/+
                                    failNum += testAdditionSubtractionExd(number1N_0_4, number2N_0_5, addFlag, msg); // -/- : addition no carry                            
                                }else{
                                    //test subtraction: +/+,-/-
                                    failNum += testAdditionSubtractionExd(number1P_0_4, number2P_5_9, addFlag, msg); // +/+ : subtraction no borrow
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N_0_4, number2N_5_9, addFlag, msg); // -/- : subtraction no borrow
                                }                                
                                break;
                            }
                            case "SameSignWithBorrowCarry":{
                                // the size of first input integer less than second
                                String number1P_5_9 = positiveRandomString(smallSize, 5, 9);
                                String number2P_0_4 = positiveRandomString(largeSize, 0, 4);
                                String number2P_5_9 = positiveRandomString(largeSize, 5, 9);

                                String number1N_5_9 = "-" + number1P_5_9;
                                String number2N_0_4 = "-" + number2P_0_4;
                                String number2N_5_9 = "-" + number2P_5_9;

                                if (addFlag){
                                    //test addition: -/-, not test +/+
                                    failNum += testAdditionSubtractionExd(number1N_5_9, number2N_5_9, addFlag, msg); // -/- : addition carry                            
                                }else{
                                    //test subtraction: +/+,-/-
                                    failNum += testAdditionSubtractionExd(number1P_5_9, number2P_0_4, addFlag, msg); // +/+ : subtraction borrow
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N_5_9, number2N_0_4, addFlag, msg); // -/- : subtraction borrow
                                }                                 
                                break;
                            }
                            case "DiffSignWithoutBorrowCarry":{
                                // the size of first input integer less than second
                                String number1P_0_4 = positiveRandomString(smallSize, 0, 4);
                                String number2P_0_5 = positiveRandomString(largeSize, 0, 5);
                                String number2P_5_9 = positiveRandomString(largeSize, 5, 9);

                                String number1N_0_4 = "-" + number1P_0_4;
                                String number2N_0_5 = "-" + number2P_0_5;
                                String number2N_5_9 = "-" + number2P_5_9;

                                if (addFlag){
                                    //test addition: +/-,-/+
                                    failNum += testAdditionSubtractionExd(number1P_0_4, number2N_5_9, addFlag, msg); // +/- : addition no borrow
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N_0_4, number2P_5_9, addFlag, msg); // -/+ : addition no borrow                           
                                }else{
                                    //test subtraction: +/-,-/+
                                    failNum += testAdditionSubtractionExd(number1P_0_4, number2N_0_5, addFlag, msg); // +/- : subtraction no carry
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N_0_4, number2P_0_5, addFlag, msg); // -/+ : subtraction no carry
                                }                                
                                break;
                            }
                            case "DiffSignWithBorrowCarry":{
                                // the size of first input integer less than second
                                String number1P_5_9 = positiveRandomString(smallSize, 5, 9);
                                String number2P_0_4 = positiveRandomString(largeSize, 0, 4);
                                String number2P_5_9 = positiveRandomString(largeSize, 5, 9);

                                String number1N_5_9 = "-" + number1P_5_9;
                                String number2N_0_4 = "-" + number2P_0_4;
                                String number2N_5_9 = "-" + number2P_5_9;

                                if (addFlag){
                                    //test addition: +/-,-/+
                                    failNum += testAdditionSubtractionExd(number1P_5_9, number2N_0_4, addFlag, msg); // +/- : borrow
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N_5_9, number2P_0_4, addFlag, msg); // -/+ : borrow                           
                                }else{
                                    //test subtraction: +/-,-/+
                                    failNum += testAdditionSubtractionExd(number1P_5_9, number2N_5_9, addFlag, msg); // +/- : carry
                                    if(failNum > 0) {break;}
                                    failNum += testAdditionSubtractionExd(number1N_5_9, number2P_5_9, addFlag, msg); // -/+ : carry
                                }                                 
                                break;
                            }
                            case "Zero":{
                                // the size of first input integer less than second
                                String number = randomString(largeSize);
                                failNum += testAdditionSubtractionExd("0", number, addFlag, msg); // 0+/-n
                                break;
                            }
                            default:
                                throw new NumberFormatException("[testAdditionSubtraction] Smaller subTestType error");
                        }                          
                        break;
                    }
                    default:
                        throw new NumberFormatException("[testAdditionSubtraction] mainTestTypee error");
                }
                                                              
                if(failNum > 0){
                    testFail = true;
                    failNum = 1; // just conunt one fail case
                    break;
                }
            }
            if(testFail)
                break;
        }
        return failNum;
    }       
    
    /**
     * @param sizes the array number of size
     * @param maxRun max run number
     * @param mainTestType main test type
     * @param subTestType sub test type
     * @param msg display message
     * @return the number of failed test cases
     */    
    public static int testCompareTo(int[] sizes, int maxRun, String mainTestType, String subTestType, String msg){                
        int failNum = 0;
        
        for(int n = 0; n < maxRun; n++){
            boolean testFail = false;
            for(int i=0;i<sizes.length;i++){                
                Random random = new Random();
                int smallSize = random.nextInt(sizes[i]) + 1;
                int largeSize = random.nextInt(smallSize) + smallSize + 1;  // generate a number [smallSzie+1,2*smallSzie]                
                
                switch(mainTestType){
                    case "Equal":
                        // 0==0, -==-, +==+
                        switch(subTestType){
                            case "0==0" :{
                                failNum += testCompareToExd("0", "0", msg); // 0==0
                                break;
                            }
                            case "-+==+-":{
                                String numberP1 = positiveRandomString(smallSize, 0, 9);
                                String numberN1 = "-" + numberP1;                                
                                failNum += testCompareToExd(numberN1, numberN1, msg); // -==-
                                if(failNum > 0) {break;}
                                failNum += testCompareToExd(numberP1, numberP1, msg); // +==+                                
                                break;
                            }
                            default:
                                throw new NumberFormatException("[testCompareTo] Equal subTestType error");
                        }
                        break;

                    case "Larger":
                        // ->-, +>+, +>0, +>-  
                        switch(subTestType){
                            case "->-" :{
                                String numberP1 = positiveRandomString(smallSize, 0, 4);
                                String numberP2 = positiveRandomString(smallSize, 5, 9);
                                String numberP3 = positiveRandomString(largeSize, 0, 9);
                                String numberN1 = "-" + numberP1;
                                String numberN2 = "-" + numberP2;
                                String numberN3 = "-" + numberP3;
                                
                                failNum += testCompareToExd(numberN1, numberN2, msg); // same size
                                if(failNum > 0) {break;}
                                failNum += testCompareToExd(numberN1, numberN3, msg); // different size
                                break;
                            }
                            case "+>+":{
                                String numberP1 = positiveRandomString(smallSize, 0, 4);
                                String numberP2 = positiveRandomString(smallSize, 5, 9);
                                String numberP3 = positiveRandomString(largeSize, 0, 9);
                                
                                failNum += testCompareToExd(numberP2, numberP1, msg); // same size
                                if(failNum > 0) {break;}
                                failNum += testCompareToExd(numberP3, numberP1, msg); // different size 
                                break;
                            }
                            case "+>0":{
                                String numberP1 = positiveRandomString(smallSize, 0, 9); 
                                
                                failNum += testCompareToExd(numberP1, "0", msg); 
                                break;
                            }
                            case "+>-":{
                                String numberP1 = positiveRandomString(smallSize, 0, 4);
                                String numberP2 = positiveRandomString(smallSize, 5, 9);
                                String numberP3 = positiveRandomString(largeSize, 0, 9);
                                String numberN1 = "-" + numberP1;
                                String numberN2 = "-" + numberP2;
                                String numberN3 = "-" + numberP3;
                                
                                failNum += testCompareToExd(numberP1, numberN2, msg); // same size
                                if(failNum > 0) {break;}
                                failNum += testCompareToExd(numberP1, numberN3, msg); // different size                                
                                break;
                            }
                            default:
                                throw new NumberFormatException("[testCompareTo] Larger subTestType error");
                        }                                               
                        break;

                    case "Smaller":
                        // -<-, +<+, -<0, -<+
                        switch(subTestType){
                            case "-<-" :{
                                String numberP1 = positiveRandomString(smallSize, 0, 4);
                                String numberP2 = positiveRandomString(smallSize, 5, 9);
                                String numberP3 = positiveRandomString(largeSize, 0, 9);
                                String numberN1 = "-" + numberP1;
                                String numberN2 = "-" + numberP2;
                                String numberN3 = "-" + numberP3;
                                
                                failNum += testCompareToExd(numberN2, numberN1, msg); // same size
                                if(failNum > 0) {break;}
                                failNum += testCompareToExd(numberN3, numberN1, msg); // different size
                                break;
                            }
                            case "+<+":{
                                String numberP1 = positiveRandomString(smallSize, 0, 4);
                                String numberP2 = positiveRandomString(smallSize, 5, 9);
                                String numberP3 = positiveRandomString(largeSize, 0, 9);
                                
                                failNum += testCompareToExd(numberP1, numberP2, msg); // same size
                                if(failNum > 0) {break;}
                                failNum += testCompareToExd(numberP1, numberP3, msg); // different size 
                                break;
                            }
                            case "-<0":{
                                String numberP1 = positiveRandomString(smallSize, 0, 9); 
                                String numberN1 = "-" + numberP1;
                                
                                failNum += testCompareToExd(numberN1, "0", msg);                                
                                break;
                            }
                            case "-<+":{
                                String numberP1 = positiveRandomString(smallSize, 0, 4);
                                String numberP2 = positiveRandomString(smallSize, 5, 9);
                                String numberP3 = positiveRandomString(largeSize, 0, 9);
                                String numberN1 = "-" + numberP1;
                                String numberN2 = "-" + numberP2;
                                String numberN3 = "-" + numberP3;
                                
                                failNum += testCompareToExd(numberN2, numberP1, msg); // same size
                                if(failNum > 0) {break;}
                                failNum += testCompareToExd(numberN3, numberP1, msg); // different size                                
                                break;
                            }
                            default:
                                throw new NumberFormatException("[testCompareTo] Smaller subTestType error");
                        }                                                                    
                        break;

                    default:
                        throw new NumberFormatException("[testCompareTo] mainTestType error");                            
                }                 

                if(failNum > 0){
                    testFail = true;
                    failNum = 1; // just conunt one fail case
                    break;
                }
            }
            if(testFail)
                break;
        }
        return failNum;
    }     
         
    /**
     * @param sizes the array number of size
     * @param maxRun max run number
     * @param mainTestType main test type
     * @param subTestType sub test type
     * @param msg display message
     * @return the number of failed test cases
     */    
    public static int testMultiplication(int[] sizes, int maxRun, String mainTestType, String subTestType, String msg){                
        int failNum = 0;
        
        for(int n = 0; n < maxRun; n++){
            boolean testFail = false;
            for(int i=0;i<sizes.length;i++){             
                Random random = new Random();               
                int firstSize = 0;
                int secondSize = 0;               
                int smallSize = random.nextInt(sizes[i]) + 1;
                int largeSize = random.nextInt(smallSize) + smallSize + 1;  // generate a number [smallSzie+1,2*smallSzie]                 
            
                switch(mainTestType){
                    case "Equal":{
                        // same input sizes
                        firstSize = smallSize;
                        secondSize = smallSize;
                        break;
                    }
                    case "Larger":{
                        // the size of first input integer larger than second
                        firstSize = largeSize;
                        secondSize = smallSize;                        
                        break;
                    }
                    case "Smaller":{
                        // the size of first input integer less than second
                        firstSize = smallSize;
                        secondSize = largeSize;                         
                        break;
                    }
                    default:
                        throw new NumberFormatException("[testMultiplication] mainTestType error");
                }
                
                switch(subTestType){
                    case "SameSignWithoutCarry":{
                        String number1P_0_3 = positiveRandomString(firstSize, 0, 3);
                        String number2P_0_3 = positiveRandomString(secondSize, 0, 3);
                        String number1N_0_3 = "-" + number1P_0_3;
                        String number2N_0_3 = "-" + number2P_0_3;
                        
                        failNum += testMultiplicationExd(number1P_0_3, number2P_0_3, msg); // +/+ : no carry
                        if(failNum > 0) {break;}
                        failNum += testMultiplicationExd(number1N_0_3, number2N_0_3, msg); // -/- : no carry
                        break;
                    }
                    case "SameSignWithCarry": {                       
                        String number1P_4_9 = positiveRandomString(firstSize, 4, 9);
                        String number2P_4_9 = positiveRandomString(secondSize, 4, 9);
                        String number1N_4_9 = "-" + number1P_4_9;
                        String number2N_4_9 = "-" + number2P_4_9;
                        
                        failNum += testMultiplicationExd(number1P_4_9, number2P_4_9, msg); // +/+ : carry
                        if(failNum > 0) {break;}
                        failNum += testMultiplicationExd(number1N_4_9, number2N_4_9, msg); // -/- : carry
                        break;
                    }
                    case "DiffSignWithoutCarry":{
                        String number1P_0_3 = positiveRandomString(firstSize, 0, 3);
                        String number2P_0_3 = positiveRandomString(secondSize, 0, 3);
                        String number1N_0_3 = "-" + number1P_0_3;
                        String number2N_0_3 = "-" + number2P_0_3;
                        
                        failNum += testMultiplicationExd(number1P_0_3, number2N_0_3, msg); // +/- : no carry
                        if(failNum > 0) {break;}
                        failNum += testMultiplicationExd(number1N_0_3, number2P_0_3, msg); // -/+ : no carry                     
                        break;
                    }
                    case "DiffSignWithCarry": {                    
                        String number1P_4_9 = positiveRandomString(firstSize, 4, 9);
                        String number2P_4_9 = positiveRandomString(secondSize, 4, 9);
                        String number1N_4_9 = "-" + number1P_4_9;
                        String number2N_4_9 = "-" + number2P_4_9;
                        
                        failNum += testMultiplicationExd(number1P_4_9, number2N_4_9, msg); // +/- : carry
                        if(failNum > 0) {break;}
                        failNum += testMultiplicationExd(number1N_4_9, number2P_4_9, msg); // -/+ : carry
                        break;
                    }
                    case "Zero":{                        
                        if(firstSize == secondSize){
                            // same input sizes
                            String number = randomString(1);
                            failNum += testMultiplicationExd("0", "0", msg); // 0*0
                            if(failNum > 0) {break;}
                            failNum += testMultiplicationExd(number, "0", msg); // n*0
                            if(failNum > 0) {break;}
                            failNum += testMultiplicationExd("0", number, msg); // 0*n
                        } else if(firstSize > secondSize) {
                            // the size of first input integer larger than second
                            String number = randomString(firstSize);
                            failNum += testMultiplicationExd(number, "0", msg); // n*0
                        } else if(firstSize < secondSize) {
                            // the size of first input integer less than second
                            String number = randomString(secondSize);
                            failNum += testMultiplicationExd("0", number, msg); // 0*n                           
                        }
                        break;
                    }
                    default:
                        throw new NumberFormatException("[testMultiplication] subTestType error");                        
                }                
                                                   
                if(failNum > 0){
                    testFail = true;
                    failNum = 1; // just conunt one fail case
                    break;
                }
            }
            if(testFail)
                break;
        }
        return failNum;
    }    
    
    /**
     * @param number1 first number 
     * @param number2 second number
     * @param addFlag addition or subtraction
     * @param msg display message
     * @return the number of failed test cases
     */    
    public static int testAdditionSubtractionExd(String number1, String number2, boolean addFlag, String msg){
        HugeInteger x1;
        HugeInteger x2;
        HugeInteger x3;

        BigInteger y1;
        BigInteger y2;
        BigInteger y3;
                
        int failNum = 0;        
        String opSign;

        if(addFlag){
            opSign = "+";
        } else{
            opSign = "-";
        }
        
        try{
            y1 = new BigInteger(number1);
            y2 = new BigInteger(number2);

            x1 = new HugeInteger(number1);
            x2 = new HugeInteger(number2);            
            
            if(addFlag){
                y3 = y1.add(y2);
                x3 = x1.add(x2);                
            }else{
                y3 = y1.subtract(y2);
                x3 = x1.subtract(x2);                
            }

            if(!(y3.toString().equals(x3.toString()))){
                System.out.println(ANSI_RED + "Error in " + msg + " ->"+ANSI_RESET);
                if(y3.toString().length()<=NUM_DISPLAY){
                    System.out.println(ANSI_RED + "Input1(" + opSign + "): " + ANSI_RESET + number1);
                    System.out.println(ANSI_RED + "Input2(" + opSign + "): "+ ANSI_RESET + number2);
                    System.out.println(ANSI_RED + "Expected output= "+ ANSI_RESET + y3.toString());
                    System.out.println(ANSI_RED + "Current  output= "+ ANSI_RESET + x3);
                }else{
                    System.out.println(ANSI_CYAN + "Display size limited: " + ANSI_RESET + NUM_DISPLAY);
                }
                failNum++;
            }
        }catch(Exception e){
            System.out.println(ANSI_RED + e + ANSI_RESET);
            System.out.println(ANSI_RED + "Input1(" + opSign + "): " + ANSI_RESET + number1);
            System.out.println(ANSI_RED + "Input2(" + opSign + "): "+ ANSI_RESET + number2);            
            failNum++;            
        }
        return failNum;
    }
    
    /**
     * @param number1 first number 
     * @param number2 second number
     * @param msg display message
     * @return the number of failed test cases
     */
    public static int testCompareToExd(String number1, String number2, String msg){
        HugeInteger x1;
        HugeInteger x2;
        int x3;

        BigInteger y1;
        BigInteger y2;
        int y3;
                
        int failNum = 0;
        int maxLen = Math.max(number1.length(), number2.length());        
        String opSign = "=";
               
        try{
            y1 = new BigInteger(number1);
            y2 = new BigInteger(number2);
            y3 = y1.compareTo(y2);
            
            x1 = new HugeInteger(number1);
            x2 = new HugeInteger(number2);
            x3 = x1.compareTo(x2);
                                   
            if(y3!=x3){
                System.out.println(ANSI_RED + "Error in " + msg + " ->"+ANSI_RESET);
                if(maxLen<=NUM_DISPLAY){
                    System.out.println(ANSI_RED + "Input1(" + opSign + "): " + ANSI_RESET + number1);
                    System.out.println(ANSI_RED + "Input2(" + opSign + "): "+ ANSI_RESET + number2);
                    System.out.println(ANSI_RED + "Expected output= "+ ANSI_RESET + y3);
                    System.out.println(ANSI_RED + "Current  output= "+ ANSI_RESET + x3);
                }else{
                    System.out.println(ANSI_CYAN + "Display size limited: " + ANSI_RESET + NUM_DISPLAY);
                }
                failNum++;
            }
        }catch(Exception e){
            System.out.println(ANSI_RED + e + ANSI_RESET);
            System.out.println(ANSI_RED + "Input1(" + opSign + "): " + ANSI_RESET + number1);
            System.out.println(ANSI_RED + "Input2(" + opSign + "): "+ ANSI_RESET + number2);            
            failNum++;            
        }       
        return failNum;
    }        

    /**
     * @param number1 first number 
     * @param number2 second number
     * @param msg display message
     * @return the number of failed test cases
     */    
    public static int testMultiplicationExd(String number1, String number2, String msg){
        HugeInteger x1;
        HugeInteger x2;
        HugeInteger x3;

        BigInteger y1;
        BigInteger y2;
        BigInteger y3;
                
        int failNum = 0;        
        String opSign = "x";
        
        try{
            y1 = new BigInteger(number1);
            y2 = new BigInteger(number2);
            y3 = y1.multiply(y2);

            x1 = new HugeInteger(number1);
            x2 = new HugeInteger(number2); 
            x3 = x1.multiply(x2);
            
            if(!(y3.toString().equals(x3.toString()))){
                System.out.println(ANSI_RED + "Error in " + msg + " ->"+ANSI_RESET);
                if(y3.toString().length()<=NUM_DISPLAY){
                    System.out.println(ANSI_RED + "Input1(" + opSign + "): " + ANSI_RESET + number1);
                    System.out.println(ANSI_RED + "Input2(" + opSign + "): "+ ANSI_RESET + number2);
                    System.out.println(ANSI_RED + "Expected output= "+ ANSI_RESET + y3.toString());
                    System.out.println(ANSI_RED + "Current  output= "+ ANSI_RESET + x3);
                }else{
                    System.out.println(ANSI_CYAN + "Display size limited: " + ANSI_RESET + NUM_DISPLAY);
                }
                failNum++;
            }
        }catch(Exception e){
            System.out.println(ANSI_RED + e + ANSI_RESET);
            System.out.println(ANSI_RED + "Input1(" + opSign + "): " + ANSI_RESET + number1);
            System.out.println(ANSI_RED + "Input2(" + opSign + "): "+ ANSI_RESET + number2);            
            failNum++;            
        }       
        return failNum;
    }
    
    /**
     * @param size the size of the number 
     * @return the string of the number
     */     
    public static String randomString(int size){
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

    /**
     * @param size the size of the number 
     * @param min the lower bound (inclusive)
     * @param max the upper bound (inclusive)
     * @return the string of the number
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

    /**
     * @param val the string with leading zeros 
     * @return the string of the number without leading zeros
     */     
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
    
    /**
     * @param n the length of the invalid string 
     * @return the invalid string
     */     
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