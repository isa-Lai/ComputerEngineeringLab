//If you are using a different package name, change lab3 to the appropriate package name from your project
package lab3; 


public class TestBSTSet {
	public static void main(String[] args) {
		double FinalMarks=0;
		double BonusMarks=0;
		
		//int[] d1 = {1,2}; //no repetitions
		int[] d1 = {7, 10, 16, 3, 2, 11}; //no repetitions
		int[] d2 = {5, 6, 5, 1, 2, 2, 2, 4, 9, 8, 20, 32, 3, 17};//with repetitions
		int v1 = 11; //value in set d1
		int v2 = 14; //value not in set d1
		int v3 = 10; //another value in set d1
		int v4 = 3; //another value in set d1 
		double half_mark = 0.5;
		double total_marks = 42;
//		int c = 0;

		int[] d3 = {17, 2, 9, 8, 30}; //has elements in common with d1
		int[] d4 = {6, 1, 12, 5}; //no common elements with d1
		int[] d5 = {0,  2,  3,  4,  7,  8,  9,  11,  16,  17,  30, };
		int[] d6 = {2, 4, 6, 8, 11, 10, 14, 15, 18, 24, 22, 35, 28, 29}; //almost sorted array
		int[] d7 = {1, 3, 6, 10, 11, 22, 28, 35}; // sorted; has elements in common with d6		
		int[] d8 = {2, 3, 7, 8, 9, 10, 11, 16, 17, 30};
		int[] d9 = {1,  2,  3,  5,  6,  7,  10,  11,  12,  16};
		int[] d10 = {2};
		int[] d11 = {2,  4,  8,  14,  15,  18,  24,  29};
		int[] d12 = {1, 3, 6, 10, 11, 22, 28, 35};
		int[] d13 = {1, 3};
		int[] d14 = {2,  3,  7,  10,  11,  16};
		int[] d15 = {1,  2,  3,  4,  5,  6,  8,  9,  17,  20,  32};
//		int[] d16 = {2,  3,  7,  10,  11,  16};

		System.out.println("Test1---constructor1" );
	    BSTSet a0 = new BSTSet();
	    try {
//		a0.printBSTSet();
		if (a0.getRoot() == null) {
			System.out.println("BST empty: Pass (+2)" );
			FinalMarks = FinalMarks + 2;
		}
		else {
			System.out.println("BST not empty: Fail (-2)" );
		}
	    }
	    catch (Exception e) {
			System.out.println("Error in Test1");
		}
		System.out.println("\n");
				 
		System.out.println("Test2---constructor2---no repetitions" );
		BSTSet a1 = new BSTSet(d1);
//		BSTSet a10 = new BSTSet(d14);	
		try {
		
		if (EleCheck(d14, a1) == true) {
			System.out.print("constructor wtih no repetitions: Pass (+2) \n");
			FinalMarks=FinalMarks+2;
		}
		else 
			System.out.print("constructor wtih no repetitions: Fail (-2)");
		
		a1.printBSTSet();
		}
		catch (Exception e) {
			System.out.println("Error in Test2");
		}
		System.out.println("\n");
		
		System.out.println("Test3---constructor2---with repetitions" );
		
		BSTSet a2 = new BSTSet(d2);
//		BSTSet a15 = new BSTSet(d15);
		try {
		
		if (EleCheck(d15, a2) == true) {
			System.out.print("constructor wtih repetitions: Pass (+2) \n");
			FinalMarks=FinalMarks+2;
		}
		else 
			System.out.print("constructor wtih repetitions: Fail (-2)");
		a2.printBSTSet();
		}
		catch (Exception e) {
			System.out.println("Error in Test3");
		}
		System.out.println("\n");
		
		System.out.println("Test4---isIn(v1)" );
		try {
		System.out.println("v1 is in set d1");
		if (a1.isIn(v1)== true)	
		{
			System.out.println("isIn method for true: Pass (+2)");
			FinalMarks=FinalMarks+2;
		}
		else 
			System.out.println("isIn method for true: Fail (-2)");
		}
		catch (Exception e) {
		System.out.println("Error in isIn()");
		}
		System.out.println("\n");
		
		System.out.println("Test5---isIn(v2)---false" );
		try {
		System.out.println("v2 is in set d1");
		if (a1.isIn(v2)== false)	
		{
			System.out.println("isIn method for false: Pass (+2)");
			FinalMarks=FinalMarks+2;
		}
		else 
			System.out.println("isIn method for false: Fail (-2)");
		}
		catch (Exception e) {
		System.out.println("Error in isIn()");
		}
		System.out.println("\n");
		
		
		System.out.println("Test6---add(v1)---v1 was in the set; no change" );
		BSTSet b1 = new BSTSet(d1);
		BSTSet b2 = new BSTSet(d1);
		try {
		b1.add(v1);
		
		if (EleCheck(d1, b1) == true) {
			System.out.print("add() with no change: Pass (+2) \n");
			FinalMarks=FinalMarks+2;
		}
		else 
			System.out.print("add() with no change: Fail (-2)");
		

		b1.printBSTSet();
		b2.printBSTSet();
		}
		catch (Exception e) {
			System.out.println("Error in remove()");
			}
		System.out.println("\n");
		
		System.out.println("Test7---add(v2)---v2 was not in the set; it is added" );
		try {
		b1 = new BSTSet(d1);
		b2 = new BSTSet(d1);
		b1.add(v2);
		int flag=0;
		int p=visitNode(b1.getRoot(), b2.getRoot(),flag);
		if (p>0) {
			System.out.println("Add with change: Pass (+2)");
			FinalMarks=FinalMarks+2;
		}
		else 
			System.out.println("Add with change: fail (-2)");
		b1.printBSTSet();
		b2.printBSTSet();
		}
		catch (Exception e) {
			System.out.println("Error in add()");
		}
		System.out.println("\n");
			
		
		System.out.println("Test8---remove(v2)--v2 was not in the set; no change" );
		try {
		a1 = new BSTSet(d1);
		if (a1.remove(v2)== false)	
		{
			System.out.println("remove method for false: Pass (+2)");
			FinalMarks=FinalMarks+2;
		}
		else 
			System.out.println("remove method for false: Fail (-2)");
		a1.printBSTSet();
		}
		catch (Exception e) {
		System.out.println("Error in remove()");
		}
		System.out.println("\n");
		
		System.out.println("Test9---remove(v1)--v1 was in the set; it is removed" );
		try {
		a1 = new BSTSet(d1);
		if (a1.remove(v4)== true)	
		{
			System.out.println("remove method for true: Pass (+2)");
			FinalMarks=FinalMarks+2;
		}
		else 
			System.out.println("remove method for true: Fail (-2)");
		a1.printBSTSet();
		}
		catch (Exception e) {
			System.out.println("Error in remove()");
			}
		System.out.println("\n");
		
		System.out.println("Test10---remove(v3)--v3 was in the set; it is removed" );
		try {
		a1 = new BSTSet(d1);
		if (a1.remove(v3)== true)	
		{
			System.out.println("remove method for true: Pass (+2)");
			FinalMarks=FinalMarks+2;
		}
		else 
			System.out.println("remove method for true: Fail (-2)");
		}
		catch (Exception e) {
			System.out.println("Error in remove()");
			}
		System.out.println("\n");
		
		System.out.println("Test11---union()---sets with common elements" );
	
		a1 = new BSTSet(d1);
		BSTSet a3 = new BSTSet(d3);
		
		BSTSet a5 = new BSTSet(); 
		try {
		a5 =a1.union(a3); //union of d1 and d3
	
		if (EleCheck(d8, a5) == true) {
			System.out.print("test for union(): Pass (+2) \n");
			FinalMarks=FinalMarks+2;
		}
		else 
			System.out.print("test for union(): Fail (-2)");
		

		System.out.println("BST 1" );
		a1.printBSTSet();
		System.out.println("BST 2" );
		a3.printBSTSet();
		System.out.println("union(BST 1, BST 2)" );
		a5.printBSTSet();
//		b5.printBSTSet();
		}
		catch (Exception e) {
			System.out.println("Error in union()");
		}
		System.out.println("\n");
		
		
		System.out.println("Test12---union()---sets with common elements" );
		a1 = new BSTSet(d1);
		BSTSet a4 = new BSTSet(d9);
		try {
		a5 = a4.union(a1); //union of d1 and d4
//		a5.printBSTSet();
//		System.out.println("\n");
		if (EleCheck(d9, a5) == true) {
			System.out.print("test for union() with common elements: Pass (+2) \n");
			FinalMarks=FinalMarks+2;
		}
		else 
			System.out.print("test for union() with common elements: Fail (-2)");
		
		System.out.println("BST 1" );
		a1.printBSTSet();
		System.out.println("BST 2" );
		a4.printBSTSet();
		System.out.println("union(BST 1, BST 2)" );
		a5.printBSTSet();
//		b5.printBSTSet();
		}
		catch (Exception e) {
			System.out.println("Error in Union()");
		}
		System.out.println("\n");
		
		
		System.out.println("Test13---union()---with empty set" );
		a0 = new BSTSet(); //empty set
		a1 = new BSTSet(d1);
		try {
		a5 = a1.union(a0); //Union of d1 and the empty set; should be a1
//		a5.printBSTSet()
//		System.out.println("\n");
		
		if (EleCheck(d1, a5) == true) {
			System.out.print("union() with empty set: Pass (+2) \n");
			FinalMarks=FinalMarks+2;
		}
		else 
			System.out.print("union() with empty set: Fail (-2)");
		
	
		System.out.println("BST 1" );
		a0.printBSTSet();
		System.out.println("BST 2" );
		a1.printBSTSet();
		System.out.println("union(BST 1, BST 2)" );
		a5.printBSTSet();
//		b5.printBSTSet();
		}
		catch (Exception e) {
			System.out.println("Error in union()");
		}
		System.out.println("\n");
		
		System.out.println("Test14---intersection()---sets with common elements" );
		try {
		a1 = new BSTSet(d1);
		a3 = new BSTSet(d3);
		a5 = a3.intersection(a1); //intersection of d1 and d3
//		a5.printBSTSet();
		
		if (EleCheck(d10, a5) == true) {
			System.out.print("intersection() check: Pass (+2) \n");
			FinalMarks=FinalMarks+2;
		}
		else 
			System.out.print("intersection() check: Fail (-2)");
		
		System.out.println("BST 1" );
		a1.printBSTSet();
		System.out.println("BST 2" );
		a3.printBSTSet();
		System.out.println("intersection(BST 1, BST 2)" );
		a5.printBSTSet();
		}
		catch (Exception e) {
		System.out.println("Error in intersection");}
		System.out.println("\n");

		System.out.println("Test15---intersection()---sets with no common elements" );
		try {
		a1 = new BSTSet(d1);
		a4 = new BSTSet(d4);
		a5 = a1.intersection(a4); //intersection of d1 and d4; should be empty
//		a5.printBSTSet();
		if (a5.getRoot() == null) {
			System.out.println("BST empty: Pass (+2)" );
			FinalMarks = FinalMarks + 2;
		}
		else {
			System.out.println("BST not empty: Fail (-2)" );
		}
		System.out.println("BST 1" );
		a1.printBSTSet();
		System.out.println("BST 2" );
		a4.printBSTSet();
		System.out.println("intersection(BST 1, BST 2)" );
		a5.printBSTSet();
		}
		catch (Exception e) {
			System.out.println("Error in intersection()");
		}
		
		System.out.println("\n");
		
		
		System.out.println("Test16---intersection()---with empty set" );
		try {
		a0 = new BSTSet(); //empty set
		a1 = new BSTSet(d1);
		a5 = a1.intersection(a0); //intersection of d1 and the empty set; should be empty
//		a5.printBSTSet();
		
		if (a5.getRoot() == null) {
			System.out.println("BST intersection: Pass (+2)" );
			FinalMarks = FinalMarks + 2;
		}
		else {
			System.out.println("BST intersection: Fail (-2)" );
		}
		System.out.println("BST 1" );
		a0.printBSTSet();
		System.out.println("BST 2" );
		a1.printBSTSet();
		System.out.println("intersection(BST 1, BST 2)" );
		a5.printBSTSet();
		}
		catch (Exception e) {
			System.out.println("Error in intersection()");
		}
		System.out.println("\n");
		

		System.out.println("Test17---difference() -- one way");
		try {
		a0 = new BSTSet(d6);
		a1 = new BSTSet(d7);
		a5 = a0.difference(a1); //6, 11, 22, 28 are in common in both sets
//		a5.printBSTSet();
		
		if (EleCheck(d11, a5) == true && EleCheck(d12, a5) == false) {
			System.out.print("difference() check: Pass (+2) \n");
			FinalMarks=FinalMarks+2;
		}
		else 
			System.out.print("difference() check: Fail (-2)");
		
		System.out.println("BST 1" );
		a0.printBSTSet();
		System.out.println("BST 2" );
		a1.printBSTSet();
		System.out.println("difference(BST 1, BST 2)" );
		a5.printBSTSet();
		}
		catch (Exception e) {
			System.out.println("Error in difference()");
		}
		System.out.println("\n");

		
		System.out.println("Test18---difference() -- other way"); //Need attention! 
		try {
		a0 = new BSTSet(d6);
		a1 = new BSTSet(d7);
		a5 = a1.difference(a0); //6, 11, 22, 28 are in common in both sets
//		a5.printBSTSet();
		
		if (EleCheck(d13, a5) == true && EleCheck(d6, a5) == false) {
			System.out.print("difference() check: Pass (+2) \n");
			FinalMarks=FinalMarks+2;
		}
		else 
			System.out.print("difference() check: Fail (-2)");
		
		
		System.out.println("BST 1" );
		a0.printBSTSet();
		System.out.println("BST 2" );
		a1.printBSTSet();
		System.out.println("difference(BST 1, BST 2)" );
		a5.printBSTSet();
		}
		catch (Exception e) {
		System.out.println("Error in difference");
		}
		System.out.println("\n");
		
		

		System.out.println("Test19---difference() -- empty");
		try {
		a0 = new BSTSet(d6); 
		a1 = new BSTSet(d6);
		a5 = a1.difference(a0); 
//		a5.printBSTSet();
		
		if (a5.getRoot() == null) {
			System.out.println("BST Difference: Pass (+2)" );
			FinalMarks = FinalMarks + 2;
		}
		else {
			System.out.println("BST Difference: Fail (-2)" );
		}

		System.out.println("BST 1" );
		a0.printBSTSet();
		System.out.println("BST 2" );
		a1.printBSTSet();
		System.out.println("difference(BST 1, BST 2)" );
		a5.printBSTSet();
		}
		catch (Exception e) {
		System.out.println("Error in intersection");
		}
		System.out.println("\n");

		System.out.println("Test20---size() + height()" );
		try {
		a1 = new BSTSet(d1);
		
		if (a1.size() == 6) {
			System.out.println("The size of d1 is " + a1.size() + " --> Pass (+0.5)");
			FinalMarks = FinalMarks + half_mark;
		}else
			System.out.println("The size of d1 is " + a1.size() + " --> Fail (-0.5)");
		
		if (a1.height() >= 0) {
			System.out.println("The height d1 is " + a1.height() + " --> Pass (+0.5)"); //height should be minimum for bonus
			FinalMarks = FinalMarks + half_mark;
		}else
			System.out.println("The height d1 is " + a1.height() + " --> Fail (-0.5)");
		
//		System.out.println("The height d1 is " + a1.height()); //height should be minimum for bonus
		a1 = new BSTSet(d6);
		
		if (a1.size() == 14) {
			System.out.println("The size of d6 is " + a1.size() + " --> Pass (+0.5)");
			FinalMarks = FinalMarks + half_mark;
		}else
			System.out.println("The size of d6 is " + a1.size() + " --> Fail (-0.5)");
		
		if (a1.height() >= 0) {
			System.out.println("The height of d6 is " + a1.height() + " --> Pass (+0.5)"); 
			FinalMarks = FinalMarks + half_mark;
		}else
			System.out.println("The height of d6 is " + a1.height() + " --> Fail (-0.5)");
		}
		catch (Exception e) {
		System.out.println("Error in Test20");
		}
		
		System.out.println("\n");

		
		
		System.out.println("Test21---size() + height()---empty set" );
		try {
		a0 = new BSTSet(); //empty set
		
		if (a0.size() == 0) {
			System.out.println("The size of empty set is " + a1.size() + " --> Pass (+1)");
			FinalMarks = FinalMarks + 1;
		}else
			System.out.println("The size of empty set is " + a1.size() + " --> Fail (-1)");
		
		if (a0.height() == -1) {
			System.out.println("The height of empty set is " + a1.height() + " --> Pass (+1)"); 
			FinalMarks = FinalMarks + 1;
		}else
			System.out.println("The height of empty set is " + a1.height() + " --> Fail (-1)");
		}
		catch (Exception e) {
			System.out.println("Error in Test21");
		}
		//System.out.println("The size of the empty set is " + a0.size()); // should be 0
		//System.out.println("The height of the empty set is " + a0.height());//should be -1
		System.out.println("\n");

//		System.out.println("\n");
		
		System.out.println("Test22---printNonRec()--elements should be in increasing order" );
		try {
		a1 = new BSTSet(d1);
		a1.printNonRec(); //set d1
		a1.printLevelOrder(); 
		System.out.println("\n");
		
		a1 = new BSTSet(d6);
		a1.printNonRec(); //set d6
		a1.printLevelOrder();
		System.out.println("\n");
		}
		catch (Exception e) {
		System.out.println("Error in Test22");
		}
		
		
		System.out.println("\n");
		System.out.println("-------------------------------Bonus--------------------------------" );
		System.out.println("\n");
		//Test bonus
		System.out.println("Test23 (bonus)" );
		try {
		a1 = new BSTSet(d6);
		if (a1.height() == 3) {
			BonusMarks=BonusMarks + 5;
			System.out.println("The height of d6 is " + a1.height());
			}
		a2 = new BSTSet(d7);
		if (a2.height() == 3) {
			BonusMarks=BonusMarks + 5;
			System.out.println("The height of d7 2 is " + a2.height());
		}
		a3 = a1.union(a2); 
		a4 = a1.intersection(a2);
		System.out.println("Print union:");
		a3.printBSTSet(); 
		if (a3.height() == 4) {
			BonusMarks=BonusMarks + 5;
		System.out.println("The height of the union is " + a3.height());
		}
		System.out.println("Print intersection:");
		a4.printBSTSet(); 
		if (a4.height() == 2) {
			BonusMarks=BonusMarks + 5;
		System.out.println("The height of the intersection is " + a4.height());
		}
		System.out.println("\n");
		System.out.println("-------------------------------Total Marks--------------------------------" );
		System.out.println("\n");
		System.out.println("Total marks for tests (1-21): " + FinalMarks + " / " + total_marks);
		System.out.println("Bonus marks for test 23: " + BonusMarks + " / " + "20.0");
		}
		catch (Exception e) {
		System.out.println("Error in bonus part");
		System.out.println("\n");
		System.out.println("-------------------------------Total Marks--------------------------------" );
		System.out.println("\n");
		System.out.println("Total marks for tests (1-21): " + FinalMarks + " / " + total_marks);
		System.out.println("\n");
		}
		
	}
	static int CompareBST(TNode root1, TNode root2){
		//Condition to check both the trees are empty 
		if (root1 == null && root2 == null)
			return 1;
		//Condition to check if one of the trees is empty 
		else if (root1 != null && root2 == null) 
	        return 0; 
	    else if (root1 == null && root2 != null) 
	        return 0; 
	    else 
	    {  
	        // Recursive checking of the BSTs 
	        if (root1.element == root2.element &&  
	            CompareBST(root1.left, root2.left) == 1 &&  
	            CompareBST(root1.right, root2.right) == 1) 
	            return 1; 
	        else
	            return 0; 
	    } 

}
	
	
	
	
	static int visitNode(TNode root1,TNode root2,int flag) {
		//boolean flag2 = true;
	    if(root1.left != null) {
	    	int v = root1.element;
	    	boolean p= Compistree( v, root2);
	    	if (p==false)
				flag=flag+1;
	      flag=visitNode(root1.left,root2,flag);
	    }
	    if(root1.right != null) {
	    	int v = root1.element;
	    	boolean p= Compistree( v, root2);
	    	if (p==false)
	    		flag=flag+1;
	     flag= visitNode(root1.right,root2,flag);
	    }
	    if(root1.left == null && root1.right == null) {
	    	int v = root1.element;
	    	boolean p= Compistree( v, root2);
	    	if (p==false) {
	    		flag=flag+1;
	    	}
	    	
	    }
	       
	    	return flag;	    
	    }
	

	
	
	 static boolean Compistree(int v, TNode root2){
	        /*
	        Time Complexity: n, log n for full tree
	        Space Complexity: n, log n for full tree
	        */
	        return CompisRec(v, root2);
	    }
	    static boolean CompisRec(int v, TNode t){
	        /*
	        Time Complexity: n, log n for full tree
	        Space Complexity: n, log n for full tree
	        */
	        if (t == null) return false;
	        if (v == t.element) return true;
	        if (v < t.element) return CompisRec(v, t.left);
	        return CompisRec(v, t.right);
	    }

	    public static boolean NodeCheck(TNode root, int v) {
			if (root == null) {
				return false;
			}
			
			boolean counter = false;
			
			while (root != null) {
				if (v < root.element) {
					root = root.left;
				}else if (v > root.element) {
					root = root.right;
				} else {
					counter = true;
					break;
				}
			}
			return counter;
		}

		public static boolean EleCheck(int[] a, BSTSet b) {
			int v = 0;
			int c = 0;
			int d = 0;
			for (int i=0; i<a.length; i++) {
				v = a[i];
			if (NodeCheck(b.getRoot(), v) == true)
				c = c+1;
			else
				d = d+1;
			}
			if (d >= 1) {
				return false;
			}else {
				return true;
			}
		}
		


}
