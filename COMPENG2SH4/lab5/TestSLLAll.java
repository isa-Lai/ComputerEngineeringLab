import static org.junit.Assert.*;

import org.junit.Test;


public class TestSLLAll {

	@Test
	public void testSLLSet() {

        
        
        //test constructor 1
        SLLSet listObj1 = new SLLSet();
        String expectedSet = "";
        int expectedSize = 0;
        assertEquals(expectedSize,listObj1.getSize());
        assertEquals(expectedSet,listObj1.toString());
	}

	@Test
	public void testSLLSetIntArray() {

        int[] arr = {1, 2, 3, 7, 8}; // this list      
        
        SLLSet listObj2 = new SLLSet(arr);
        String expectedSet = "1, 2, 3, 7, 8";
        int expectedSize = 5;
        assertEquals(expectedSize,listObj2.getSize());
        assertEquals(expectedSet,listObj2.toString());
	}
	
	@Test
	public void testSLLSetIntEmptyArray() {

        int[] arr = {}; // this list      
        
        SLLSet listObj2 = new SLLSet(arr);
        String expectedSet = "";
        int expectedSize = 0;
        assertEquals(expectedSize,listObj2.getSize());
        assertEquals(expectedSet,listObj2.toString());
	}


	@Test
	public void testCopy() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
		SLLSet copied = listObj2.copy();
        copied.add(100);
        String expectedObj2 = "1, 2, 3, 7, 8";
        String expectedCopied = "1, 2, 3, 7, 8, 100";
        int expectedObj2Size = 5;
        int expectedCopiedSize = 6;
        
        assertEquals(expectedObj2Size,listObj2.getSize());
        assertEquals(expectedObj2,listObj2.toString());
        
        assertEquals(expectedCopiedSize,copied.getSize());
        assertEquals(expectedCopied,copied.toString());


	}
	
	@Test
	public void testCopyEmpty() {
		
        int[] arr = {}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
		SLLSet copied = listObj2.copy();
        String expectedObj2 = "";
        String expectedCopied = "";
        int expectedObj2Size = 0;
        int expectedCopiedSize = 0;
        
        assertEquals(expectedObj2Size,listObj2.getSize());
        assertEquals(expectedObj2,listObj2.toString());
        
        assertEquals(expectedCopiedSize,copied.getSize());
        assertEquals(expectedCopied,copied.toString());


	}

	@Test
	public void testIsInTrue() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
        
        int in = 2;

        boolean expected = true;
        
        assertEquals(expected,listObj2.isIn(in));


	}

	@Test
	public void testIsInFalse() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
        

        int notin = 0; // test isIn
        boolean expected = false;
        
        assertEquals(expected,listObj2.isIn(notin));
        
	}
	
	@Test
	public void testIsInLast() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
        

        int notin = 8; // test isIn
        boolean expected = true;
        
        assertEquals(expected,listObj2.isIn(notin));
        
	}

	
	@Test
	public void testAdd1() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
        
        int notadd = 3;

        
        String expectedObj2 = "1, 2, 3, 7, 8";
        int expectedObj2Size = 5;

        listObj2.add(notadd);
        assertEquals(expectedObj2Size,listObj2.getSize());
        assertEquals(expectedObj2,listObj2.toString());
        

	}
	@Test
	public void testAdd2() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
        
        int addmiddle = 4;

        
        String expectedObj2 = "1, 2, 3, 4, 7, 8";
        int expectedObj2Size = 6;

        listObj2.add(addmiddle);
        assertEquals(expectedObj2Size,listObj2.getSize());
        assertEquals(expectedObj2,listObj2.toString());
        
        

	}

	@Test
	public void testAdd3() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
        

        int addlast = 9;

        
        String expectedObj2 = "1, 2, 3, 7, 8, 9";
        int expectedObj2Size = 6;

        listObj2.add(addlast);
        assertEquals(expectedObj2Size,listObj2.getSize());
        assertEquals(expectedObj2,listObj2.toString());
        
        

	}
	@Test
	public void testAdd4() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
        

        int addfirst = 0;  // test add
        
        String expectedObj2 = "0, 1, 2, 3, 7, 8";
        int expectedObj2Size = 6;

        listObj2.add(addfirst);
        assertEquals(expectedObj2Size,listObj2.getSize());
        assertEquals(expectedObj2,listObj2.toString());

	}
	
	@Test
	public void testAddToEmpty() {
		
        int[] arr = {}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
        

        int addfirst = 0;  // test add
        
        String expectedObj2 = "0";
        int expectedObj2Size = 1;

        listObj2.add(addfirst);
        assertEquals(expectedObj2Size,listObj2.getSize());
        assertEquals(expectedObj2,listObj2.toString());

	}
	
	@Test
	public void testRemove1() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
        
        int notremove = 10;
        
        //test remove

        listObj2.remove(notremove);
        
        String expectedObj2 = "1, 2, 3, 7, 8";
        int expectedObj2Size = 5;
        
        assertEquals(expectedObj2Size,listObj2.getSize());
        assertEquals(expectedObj2,listObj2.toString());
        
        
        

	}

	@Test
	public void testRemove2() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
        

        int removemiddle = 3;

        
        //test remove

        listObj2.remove(removemiddle);
        
        String expectedObj2 = "1, 2, 7, 8";
        int expectedObj2Size = 4;
        
        assertEquals(expectedObj2Size,listObj2.getSize());
        assertEquals(expectedObj2,listObj2.toString());
        
        

	}
	@Test
	public void testRemove3() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
        
        int removelast = 8;

        
        //test remove

        listObj2.remove(removelast);
        
        String expectedObj2 = "1, 2, 3, 7";
        int expectedObj2Size = 4;
        
        assertEquals(expectedObj2Size,listObj2.getSize());
        assertEquals(expectedObj2,listObj2.toString());
        

	}
	@Test
	public void testRemove4() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
        
        int removefirst = 1;  // test remove
        
        //test remove

        listObj2.remove(removefirst);
        
        String expectedObj2 = "2, 3, 7, 8";
        int expectedObj2Size = 4;
        
        assertEquals(expectedObj2Size,listObj2.getSize());
        assertEquals(expectedObj2,listObj2.toString());
        

	}	
	
	@Test
	public void testRemoveToEmpty() {
		
        int[] arr = {1}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
        
        int removefirst = 1;  // test remove
        
        //test remove

        listObj2.remove(removefirst);
        
        String expectedObj2 = "";
        int expectedObj2Size = 0;
        
        assertEquals(expectedObj2Size,listObj2.getSize());
        assertEquals(expectedObj2,listObj2.toString());
        

	}
	
	@Test
	public void testUnion1() {
        // test union
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
        
       

        
        SLLSet listObj3 = new SLLSet();
        SLLSet listObj4 = listObj2.union(listObj3);
        
        
        String expected = "1, 2, 3, 7, 8";
        int expectedSize = 5;
        
        assertEquals(expectedSize,listObj4.getSize());
        assertEquals(expected,listObj4.toString());
        

        

	}
	
	@Test
	public void testUnion2() {
        // test union
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
        int[] uni1 = {0, 2, 3, 8, 10, 20};   
        
        
        SLLSet listObj5 = new SLLSet(uni1);
        SLLSet listObj6 = listObj2.union(listObj5);
        
        String expected = "0, 1, 2, 3, 7, 8, 10, 20";
        int expectedSize = 8;
        
        assertEquals(expectedSize,listObj6.getSize());
        assertEquals(expected,listObj6.toString());
        


	}
	@Test
	public void testUnion3() {
        // test union
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);       
        int[] uni2 = {0, 2, 3}; // for testing union
        SLLSet listObj7 = new SLLSet(uni2);
        SLLSet listObj8 = listObj2.union(listObj7);
        
        String expected = "0, 1, 2, 3, 7, 8";
        int expectedSize = 6;
        
        assertEquals(expectedSize,listObj8.getSize());
        assertEquals(expected,listObj8.toString());

	}
	@Test
	public void testUnion4() {
        // test union
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);
        
                  
        SLLSet listObj3 = new SLLSet();
        SLLSet listObj9 = listObj3.union(listObj2);
        
        String expected = "1, 2, 3, 7, 8";
        int expectedSize = 5;
        
        assertEquals(expectedSize,listObj9.getSize());
        assertEquals(expected,listObj9.toString());
        

	}
	
	@Test
	public void testUnionSame() {
		// test union
        int[] arr = {1, 2, 3, 7, 8}; // this list       
        SLLSet listObj2 = new SLLSet(arr);       
        int[] uni2 = {1,2,3,7,8}; // for testing union
        SLLSet listObj7 = new SLLSet(uni2);
        SLLSet listObj8 = listObj2.union(listObj7);
        
        String expected = "1, 2, 3, 7, 8";
        int expectedSize = 5;
        
        assertEquals(expectedSize,listObj8.getSize());
        assertEquals(expected,listObj8.toString());
        

	}
	
	@Test
	public void testIntersection1() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list




        
	       // test intersection
        SLLSet listObj2 = new SLLSet(arr);
        
        
        SLLSet listObj9 = new SLLSet();
        SLLSet listObj10 = listObj2.intersection(listObj9);
        
        String expected = "";
        int expectedSize = 0;
        
        assertEquals(expectedSize,listObj10.getSize());
        assertEquals(expected,listObj10.toString());
        
        
       
	}
	@Test
	public void testIntersection2() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list

        int[] inter1 = {0, 2, 7};




        
	       // test intersection
        SLLSet listObj2 = new SLLSet(arr);
        
        
        SLLSet listObj11 = new SLLSet(inter1);
        SLLSet listObj12 = listObj2.intersection(listObj11);
        
        String expected = "2, 7";
        int expectedSize = 2;
        
        assertEquals(expectedSize,listObj12.getSize());
        assertEquals(expected,listObj12.toString());
        


        
	}
	@Test
	public void testIntersection3() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list


        int[] inter2 = {1, 3, 5, 6, 8, 10};




        
	       // test intersection
        SLLSet listObj2 = new SLLSet(arr);
        
        
        SLLSet listObj13 = new SLLSet(inter2);
        SLLSet listObj14 = listObj2.intersection(listObj13);
        
        String expected = "1, 3, 8";
        int expectedSize = 3;
        
        assertEquals(expectedSize,listObj14.getSize());
        assertEquals(expected,listObj14.toString());
        




        
	}
	@Test
	public void testIntersection4() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list
        int[] inter3 = {0, 10, 100}; //test intersection



        
	       // test intersection
        SLLSet listObj2 = new SLLSet(arr);
        
        
        SLLSet listObj15 = new SLLSet(inter3);
        SLLSet listObj16 = listObj2.intersection(listObj15);
        
        String expected = "";
        int expectedSize = 0;
        
        assertEquals(expectedSize,listObj16.getSize());
        assertEquals(expected,listObj16.toString());
        
	}
	@Test
	public void testIntersectionEmpty() {
		
        int[] arr = {1, 2, 3, 7, 8}; // this list




        
	       // test intersection
        SLLSet listObj2 = new SLLSet(arr);
        
        
        SLLSet listObj9 = new SLLSet();
        SLLSet listObj10 = listObj9.intersection(listObj2);
        
        String expected = "";
        int expectedSize = 0;
        
        assertEquals(expectedSize,listObj10.getSize());
        assertEquals(expected,listObj10.toString());
        
        
       
	}
	
	@Test
	public void testDifference1() {
		
	      // parameters inpt here

        int[] arr = {1, 2, 3, 7, 8}; // this list





        
	       // test difference
        SLLSet listObj2 = new SLLSet(arr);
        SLLSet listObj17 = new SLLSet();
        SLLSet listObj18 = listObj2.difference(listObj17);
        
        String expected = "1, 2, 3, 7, 8";
        int expectedSize = 5;
        
        assertEquals(expectedSize,listObj18.getSize());
        assertEquals(expected,listObj18.toString());
        
        
	}
	@Test
	public void testDifference2() {
		
	      // parameters inpt here

        int[] arr = {1, 2, 3, 7, 8}; // this list



        int[] diff1 = {1, 2, 3, 5, 7, 8, 9};
       


        
	       // test difference
        SLLSet listObj2 = new SLLSet(arr);
        SLLSet listObj19 = new SLLSet(diff1);
        SLLSet listObj20 = listObj2.difference(listObj19);
        
        String expected = "";
        int expectedSize = 0;
        
        assertEquals(expectedSize,listObj20.getSize());
        assertEquals(expected,listObj20.toString());
        


       
	}
	
	@Test
	public void testDifference3() {
		
	      // parameters inpt here

        int[] arr = {1, 2, 3, 7, 8}; // this list




        int[] diff2 = {1, 2, 3}; // test difference


        
	       // test difference
        SLLSet listObj2 = new SLLSet(arr);
        SLLSet listObj21 = new SLLSet(diff2);
        SLLSet listObj22 = listObj2.difference(listObj21);
        
        String expected = "7, 8";
        int expectedSize = 2;
        
        assertEquals(expectedSize,listObj22.getSize());
        assertEquals(expected,listObj22.toString());
        



    
	}
	
	@Test
	public void testDifference4() {
		
	      // parameters inpt here

        int[] arr = {1, 2, 3, 7, 8}; // this list

        
	       // test difference
        SLLSet listObj2 = new SLLSet(arr);
        SLLSet listObj17 = new SLLSet();
        SLLSet listObj1000 = listObj17.difference(listObj2);
        
        String expected = "";
        int expectedSize = 0;
        
        assertEquals(expectedSize,listObj1000.getSize());
        assertEquals(expected,listObj1000.toString());
        
	}
	
	@Test
	public void testDifferenceSame() {
		
	      // parameters inpt here

        int[] arr = {1, 2, 3, 7, 8}; // this list




        int[] diff2 = {1, 2, 3,7,8}; // test difference


        
	       // test difference
        SLLSet listObj2 = new SLLSet(arr);
        SLLSet listObj21 = new SLLSet(diff2);
        SLLSet listObj22 = listObj2.difference(listObj21);
        
        String expected = "";
        int expectedSize = 0;
        
        assertEquals(expectedSize,listObj22.getSize());
        assertEquals(expected,listObj22.toString());
        



    
	}
	
	@Test
	public void testUnionSLLSetArray() {

        int[] arr1 = {0, 1, 2};
        int[] arr2 = {1, 2, 3, 4};
        int[] arr3 = {0, 1};
        int[] arr4 = {6, 7};
        SLLSet listObj100 = new SLLSet(arr1);
        SLLSet listObj99 = new SLLSet(arr2);
        SLLSet listObj98 = new SLLSet(arr3);
        SLLSet listObj97 = new SLLSet(arr4);
        SLLSet listObj96 = new SLLSet();
        SLLSet[] Array = {listObj100, listObj99, listObj98, listObj97, listObj96}; // test static SLLSet union(sArray)
        
		 SLLSet listObj23 = SLLSet.union(Array);
		 
		 String expected = "0, 1, 2, 3, 4, 6, 7";
		 int expectedSize = 7;
	        
		 assertEquals(expectedSize,listObj23.getSize());
		 assertEquals(expected,listObj23.toString());
	        
	        
	}
	
	@Test
	public void testUnionSLLSetArraySameNEmpty() {

        int[] arr1 = {0, 1, 2};
        int[] arr2 = {};
        int[] arr3 = {0,1,2};
        int[] arr4 = {};
        SLLSet listObj100 = new SLLSet(arr1);
        SLLSet listObj99 = new SLLSet(arr2);
        SLLSet listObj98 = new SLLSet(arr3);
        SLLSet listObj97 = new SLLSet(arr4);
        SLLSet listObj96 = new SLLSet();
        SLLSet[] Array = {listObj100, listObj99, listObj98, listObj97, listObj96}; // test static SLLSet union(sArray)
        
		 SLLSet listObj23 = SLLSet.union(Array);
		 
		 String expected = "0, 1, 2";
		 int expectedSize = 3;
	        
		 assertEquals(expectedSize,listObj23.getSize());
		 assertEquals(expected,listObj23.toString());
	        
	        
	}

	

}
