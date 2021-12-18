package lab4;

import java.util.Random;

public class TestHashTable {
	
	public static void main(String[] args) {
		
		double TotalMarks=0;
		double BonusMarks=0;
		
		System.out.println("***************Testing Linear Probing*******************");
		System.out.println("\n");
		int maxKeys = 5;
        double lambda = 0.4;
        int a=22,b=17,c=30,d=4,e=9,f=5,g=61 ;//Key
 
       //Test constructor (Linear Probing constructor)
        System.out.println("***************Test 1 - Constructor***************");
        HashTableLin HTest = new HashTableLin(maxKeys, lambda);
        try {
        	if (HTest.getTableSize() == 13) {          
    			System.out.println("Hash Table Size: Pass (+2)" );
    			TotalMarks= TotalMarks + 2;
    		}
    		else {
    			System.out.println("Hash Table Size: Fail (-2)" );
    			System.out.println(" Expected Table Size =13 " );
    		}
    		if (HTest.getNumKeys() == 0) {
    			System.out.println("Hash Table Empty: Pass (+2)" );
    			TotalMarks = TotalMarks + 2;
    		}
    		else {
    			System.out.println("Hash Table Empty: Fail (-2)" );
    			System.out.println(" Hash Table is Expected to be empty" );
    		}
	     }
	    catch (Exception x) {
		System.out.println("Error in Test1");
	   }
        System.out.println("Table Size = " + HTest.getTableSize());
        System.out.println("# of Keys = " + HTest.getNumKeys());
        System.out.println("Load factor = " + HTest.getMaxLoadFactor());
        HTest.printKeysAndIndexes(); 
         System.out.println("************************************************************************");

    
      //Test insert
        System.out.println("***************Test 2a - Insert into empty table***************");
        HTest.insert(a);
        try {
			if (HTest.getTableSize()==13 &&  HTest.getNumKeys()==1) {
					TotalMarks= TotalMarks+2; 
					System.out.println("Insert into empty table: Pass (+2)" );
			}
				
			else {
				System.out.println(" Insert into empty table: Fail (-2)" );
				System.out.println(" Expected Table Size =13 and #of Keys = 1 " );
			}
		}
		catch (Exception x) {
				System.out.println("Error in Test2a");
		}
        System.out.println("Table Size = " + HTest.getTableSize());
        System.out.println("Load factor = " + HTest.getMaxLoadFactor());
        System.out.println("# of Keys = " + HTest.getNumKeys());
        HTest.printKeysAndIndexes();  
        System.out.println("************************************************************************");
        
               
        System.out.println("***************Test 2b - Insert a duplicate key***************");
        HTest.insert(a);
        try {
			if (HTest.getTableSize()==13 && HTest.getNumKeys()==1) {
					TotalMarks= TotalMarks+2; 
					System.out.println(" Insert a duplicate key: Pass (+2)" );
			}
				
			else {
				System.out.println("  Insert a duplicate key: Fail (-2)" );
				System.out.println(" Expected Table Size =13 and #of Keys = 1 " );
			}
		}
		catch (Exception x) {
				System.out.println("Error in Test2b");
		}
        System.out.println("Table Size = " + HTest.getTableSize());
        System.out.println("Load factor = " + HTest.getMaxLoadFactor());
        System.out.println("# of Keys = " + HTest.getNumKeys());
        HTest.printKeys();
        HTest.printKeysAndIndexes();        
        System.out.println("************************************************************************");
        
        System.out.println("***************Test 2c - Insert with collision***************");
        HTest.insert(b);
        HTest.insert(c);
        HTest.insert(d);
        try {
			if (HTest.getTableSize()==13 && HTest.getNumKeys()==4) {
					TotalMarks= TotalMarks+2; 
					System.out.println("Insert with collision: Pass (+2)" );
			}
				
			else {
				System.out.println(" Insert with collision: Fail (-2)" );
				System.out.println(" Expected Table Size =13 and #of Keys = 4 " );
			}
			}
		catch (Exception x) {
				System.out.println("Error in Test2c");
		}
        System.out.println("Table Size = " + HTest.getTableSize());
        System.out.println("Load factor = " + HTest.getMaxLoadFactor());
        System.out.println("# of Keys = " + HTest.getNumKeys());
        HTest.printKeys();
        HTest.printKeysAndIndexes();
        System.out.println("************************************************************************");
        
        //insert with rehash
        System.out.println("***************Test 3 - Insert with rehash***************");
        HTest.insert(0);
        System.out.println("Table Size before rehash = " + HTest.getTableSize());
        System.out.println("Load factor before rehash = " + HTest.getMaxLoadFactor());
        System.out.println("# of Keys before rehash = " + HTest.getNumKeys());
        HTest.printKeysAndIndexes();
        HTest.insert(e);
        try {
			if (HTest.getTableSize()==29) {
					TotalMarks= TotalMarks+2; 
					System.out.println("Table size with rehash: Pass (+2)" );
			}
				
			else {
				System.out.println(" Table size with rehash: Fail (-2)" );
				System.out.println(" Expected Table Size = 29 " );
			}
			
			if(HTest.getNumKeys()==6) {
				TotalMarks= TotalMarks+2; 
				System.out.println("Insert with rehash: Pass (+2)" );			
			}
			else {
				System.out.println(" Table size with rehash: Fail (-2)" );
				System.out.println(" Expected #of Keys = 6 " );
			}		
		}
		catch (Exception x) {
				System.out.println("Error in Test3");
		}
        System.out.println("Table Size = " + HTest.getTableSize());
        System.out.println("Load factor = " + HTest.getMaxLoadFactor());
        System.out.println("# of Keys after rehash = " + HTest.getNumKeys());
        HTest.printKeysAndIndexes();
        System.out.println("************************************************************************");
        
        //Test isIn
        System.out.println("***************Test 4a - isIn(20) (return true)***************");
        try {
			if (HTest.isIn(a)==true) {
					TotalMarks= TotalMarks+3; 
					System.out.println("isIn(20) (return true): Pass (+3)" );
			}
				
			else {
				System.out.println(" isIn(20) (return true): Fail (-3)" );
				System.out.println(" Expected output: true " );
			}
			}
		catch (Exception x) {
				System.out.println("Error in Test4a");
		}
        System.out.println(HTest.isIn(a) + "\n");
        System.out.println("************************************************************************");
        
        System.out.println("***************Test 4b - isIn(55) (return false)***************");
        try {
			if (HTest.isIn(f)==false) {
					TotalMarks= TotalMarks+3; 
					System.out.println("isIn(55) (return false): Pass (+2)" );
			}
				
			else {
				System.out.println(" isIn(55) (return false): Fail (-2)" );
				System.out.println(" Expected output: false " );
			}
			}
		catch (Exception x) {
				System.out.println("Error in Test4b");
		}
        System.out.println(HTest.isIn(f) + "\n");
        System.out.println("************************************************************************");
        
       
        //Test insert with probing counts
        System.out.println("***************Test 5a - Inserts and Probing Count to insert new key " + 58+"***************");
        
        int tem= HTest.insertCount(g);
        try {
        	
        	if (HTest.getTableSize()==29 && tem==3 && HTest.getNumKeys()==7) {
        		TotalMarks= TotalMarks+3; 
        		System.out.println("Inserts and Probing Count to insert new key: Pass (+3)" );
        	}

        	else {
        		System.out.println(" Inserts and Probing Count to insert new key: Fail (-3)" );
        		System.out.println(" Expected # of Probes =3; Table Size =29 and #of Keys = 7 " );
        	}
        }
		catch (Exception x) {
				System.out.println("Error in Test5a");
		}
        System.out.println("# of Probes = " + tem);
        System.out.println("Table Size = " + HTest.getTableSize());
        System.out.println("Load factor = " + HTest.getMaxLoadFactor());
        System.out.println("# of Keys = " + HTest.getNumKeys() + "\n");
        HTest.printKeysAndIndexes();
        System.out.println("************************************************************************");
	
    
        System.out.println("***************Test 5b - Probing count when the key already IsIn***************");
        
        try {
			if (HTest.getTableSize()==29 && HTest.insertCount(c)==2 && HTest.getNumKeys()==7 ) {
					TotalMarks= TotalMarks+3; 
					System.out.println("Probing count when the key already IsIn: Pass (+3)" );
			}
				
			else {
				System.out.println(" Probing count when the key already IsIn: Fail (-3)" );
				System.out.println(" Expected # of Probes =2; Table Size =29 and #of Keys = 7 " );
			}
			}
		catch (Exception x) {
				System.out.println("Error in Test5b");
		}
        System.out.println("# of Probes = " + HTest.insertCount(c));
        System.out.println("Table Size = " + HTest.getTableSize());
        System.out.println("Load factor = " + HTest.getMaxLoadFactor());
        System.out.println("# of Keys = " + HTest.getNumKeys() + "\n");
        HTest.printKeysAndIndexes();
        System.out.println("************************************************************************");
         
        System.out.println("******************Testing Quadratic Probing*********************"); 
    	System.out.println("\n");
    	
    	
    	//Test constructor (Quadratic Probing constructor)
        System.out.println("***************Test 6 - Constructor***************");
        HashTableQuad HTestQ = new HashTableQuad(maxKeys, lambda);
        try {
        	if (HTestQ.getTableSize() == 13) {          
    			System.out.println("Hash Table Size: Pass (+2)" );
    			TotalMarks= TotalMarks + 2;
    		}
    		else {
    			System.out.println("Hash Table Size: Fail (-2)" );
    			System.out.println(" Expected Table Size =13 " );
    		}
    		if (HTestQ.getNumKeys() == 0) {
    			System.out.println("Hash Table Empty: Pass (+2)" );
    			TotalMarks = TotalMarks + 2;
    		}
    		else {
    			System.out.println("Hash Table Empty: Fail (-2)" );
    			System.out.println(" Hash Table is Expected to be empty" );
    			
    		}
	     }
	    catch (Exception x) {
		System.out.println("Error in Test6");
	   }
        System.out.println("Table Size = " + HTestQ.getTableSize());
        System.out.println("# of Keys = " + HTestQ.getNumKeys());
        System.out.println("Load factor = " + HTestQ.getMaxLoadFactor());
        HTestQ.printKeysAndIndexes(); 
        System.out.println("************************************************************************");

    
      //Test insert
        System.out.println("***************Test 7a - Insert into empty table***************");
        HTestQ.insert(a);
        try {
			if (HTestQ.getTableSize()==13 &&  HTestQ.getNumKeys()==1) {
					TotalMarks= TotalMarks+2; 
					System.out.println("Insert into empty table: Pass (+2)" );
			}
				
			else {
				System.out.println(" Insert into empty table: Fail (-2)" );
				System.out.println(" Expected Table Size =13 and #of Keys = 1 ");
			}
		}
		catch (Exception x) {
				System.out.println("Error in Test7a");
		}
   
        System.out.println("Table Size = " + HTestQ.getTableSize());
        System.out.println("Load factor = " + HTestQ.getMaxLoadFactor());
        System.out.println("# of Keys = " + HTestQ.getNumKeys());
        HTest.printKeysAndIndexes();
        System.out.println("************************************************************************");
        
        System.out.println("***************Test 7b - Insert a duplicate key***************");
        HTestQ.insert(a);
        try {
			if (HTestQ.getTableSize()==13 &&  HTestQ.getNumKeys()==1) {
					TotalMarks= TotalMarks+2; 
					System.out.println("Insert a duplicate key: Pass (+2)" );
			}
				
			else {
				System.out.println(" Insert a duplicate key: Fail (-2)" );
				System.out.println(" Expected Table Size =13 and #of Keys = 1 " );
			}
		}
		catch (Exception x) {
				System.out.println("Error in Test7b");
		}
        System.out.println("Table Size = " + HTestQ.getTableSize());
        System.out.println("Load factor = " + HTestQ.getMaxLoadFactor());
        System.out.println("# of Keys = " + HTestQ.getNumKeys());
        HTestQ.printKeys();
        HTestQ.printKeysAndIndexes();
        System.out.println("************************************************************************");
        
        System.out.println("***************Test 7c - Insert with collision***************");
        HTestQ.insert(b);
        HTestQ.insert(c);
        HTestQ.insert(d);
        try {
			if (HTestQ.getTableSize()==13 && HTestQ.getNumKeys()==4) {
					TotalMarks= TotalMarks+2; 
					System.out.println("Insert with collision: Pass (+2)" );
			}
				
			else {
				System.out.println(" Insert with collision: Fail (-2)" );
				System.out.println(" Expected Table Size =13 and #of Keys = 4 " );
			}
			}
		catch (Exception x) {
				System.out.println("Error in Test7c");
		}
        System.out.println("Table Size = " + HTestQ.getTableSize());
        System.out.println("Load factor = " + HTestQ.getMaxLoadFactor());
        System.out.println("# of Keys = " + HTestQ.getNumKeys());
        HTestQ.printKeys();
        HTestQ.printKeysAndIndexes();
        System.out.println("************************************************************************");

        System.out.println("***************Test 8 - Insert with rehash***************");
        HTestQ.insert(0);
        System.out.println("Table Size before rehash = " + HTestQ.getTableSize());
        System.out.println("Load factor before rehash = " + HTestQ.getMaxLoadFactor());
        System.out.println("# of Keys before rehash = " + HTestQ.getNumKeys());
        HTestQ.printKeysAndIndexes();
        HTestQ.insert(e);
    
        try {
			if (HTestQ.getTableSize()==29) {
					TotalMarks= TotalMarks+2; 
					System.out.println("Table size with rehash: Pass (+2)" );
			}
				
			else {
				System.out.println(" Table size with rehash: Fail (-2)" );
				System.out.println(" Expected Table Size =29" );
			}
			
			if(HTestQ.getNumKeys()==6) {
				TotalMarks= TotalMarks+2; 
				System.out.println("Insert with rehash: Pass (+2)" );			
			}
			else {
				System.out.println(" Table size with rehash: Fail (-2)" );
				System.out.println(" Expected #of Keys = 6" );
			}		
		}
		catch (Exception x) {
				System.out.println("Error in Test8");
		}
        System.out.println("Table Size = " + HTestQ.getTableSize());
        System.out.println("Load factor = " + HTestQ.getMaxLoadFactor());
        System.out.println("# of Keys = " + HTestQ.getNumKeys());
        HTestQ.printKeysAndIndexes();
        System.out.println("************************************************************************");

        
        //Test isIn/isEmpty
        System.out.println("***************Test 9a - isIn(20) (return true)***************");
        try {
			if (HTestQ.isIn(a)==true) {
					TotalMarks= TotalMarks+3; 
					System.out.println("isIn(20) (return true): Pass (+3)" );
			}
				
			else {
				System.out.println(" isIn(20) (return true): Fail (-3)" );
				System.out.println(" Expected output: true " );
			}
			}
		catch (Exception x) {
				System.out.println("Error in Test9a");
		}
        System.out.println(HTestQ.isIn(a) + "\n");
        System.out.println("************************************************************************");
        
        System.out.println("***************Test 9b - isIn(55) (return false)***************");
        try {
			if (HTestQ.isIn(f)==false) {
					TotalMarks= TotalMarks+3; 
					System.out.println("isIn(55) (return false): Pass (+3)" );
			}
				
			else {
				System.out.println(" isIn(55) (return false): Fail (-3)" );
				System.out.println(" Expected output: false " );
			}
			}
		catch (Exception x) {
				System.out.println("Error in Test9b");
		}    
        System.out.println(HTestQ.isIn(f) + "\n");
        System.out.println("************************************************************************");
      
        //Test insert with probing counts
        System.out.println("***************Test 10a - Inserts and Probing Count to insert new key " + 58+"***************");
        int temQ= HTestQ.insertCount(g);
        try {
        	
        	if (HTestQ.getTableSize()==29 && temQ==3 && HTestQ.getNumKeys()==7) {
        		TotalMarks= TotalMarks+3; 
        		System.out.println("Inserts and Probing Count to insert new key: Pass (+3)" );
        	}

        	else {
        		System.out.println(" Inserts and Probing Count to insert new key: Fail (-3)" );
        		System.out.println(" Expected # of Probes =3; Table Size =29 and #of Keys = 7 " );
        	}
        }
		catch (Exception x) {
				System.out.println("Error in Test10a");
		} 
        System.out.println("# of Probes = " + temQ);
        System.out.println("Table Size = " + HTestQ.getTableSize());
        System.out.println("Load factor = " + HTestQ.getMaxLoadFactor());
        System.out.println("# of Keys = " + HTestQ.getNumKeys() + "\n");
        HTestQ.printKeysAndIndexes();
        System.out.println("************************************************************************");
	
    
        System.out.println("***************Test 10b - Probing count when the key already IsIn***************");
        try {
			if (HTestQ.getTableSize()==29 && HTestQ.insertCount(c)==2 && HTestQ.getNumKeys()==7 ) {
					TotalMarks= TotalMarks+3; 
					System.out.println("Probing count when the key already IsIn: Pass (+3)" );
			}
				
			else {
				System.out.println(" Probing count when the key already IsIn: Fail (-3)" );
				System.out.println(" Expected # of Probes =2; Table Size =29 and #of Keys = 7 " );
			}
			}
		catch (Exception x) {
				System.out.println("Error in Test10b");
		}
        System.out.println("# of Probes = " + HTestQ.insertCount(c));
        System.out.println("Table Size = " + HTestQ.getTableSize());
        System.out.println("Load factor = " + HTestQ.getMaxLoadFactor());
        System.out.println("# of Keys = " + HTestQ.getNumKeys() + "\n");
        HTestQ.printKeysAndIndexes();
        System.out.println("************************************************************************");
        
        
        //Simulations to measure the average number of probes for successful search for each lambda (0.1~ 0.9)
        System.out.println("***************Test 11 - Probing Simulations for Linear Probing Hash Table***************");
        
        Random r = new Random();
        int numKeys = 100000, val, runs = 100, duplicates;  //Variables to store # keys, # runs, # duplicate inputs
        double loadFactor, countLoop, unsL, sL, probeCount = 0,probeunsucces = 0;    //Variables to store load factor, theoretical probes, and counting
        System.out.println("************************************************************************");
        
        for(loadFactor = 0.1; loadFactor <= 0.9; loadFactor += 0.1){    //Loop through load factors from 0.1 to 0.9
            for(int j = 0; j < runs; j++){                          //Run 100 times for each load factor
              HashTableLin HTests = new HashTableLin(numKeys, loadFactor);     //Initialize new hash table
                           
            	countLoop = 0;
                duplicates = 0;
                for(int k = 0; k < numKeys; k++){                   //Insert 100 000 random numbers within bounds of java int type
                	val = r.nextInt(Integer.MAX_VALUE);
                	 if(val<0)
                		 val*=-1;
                    if(HTests.isIn(val))                            //Track duplicate integer inputs
                    {   duplicates++;}
                    
                    countLoop += HTests.insertCount(val);                    
                     //Count number of probes to insert
                }
                probeCount += countLoop/(numKeys-duplicates);       //Average # probes for table, divided by # insertions              
            }
            probeCount /= runs;      
            
            sL = 0.5*(1+(1/(1-loadFactor)));                        //Calculation of s Lambda for LP
            
            //compare between experimental and theoretical values for avg. no. of probing
            if ((Math.abs(probeCount-sL)<.1*sL)) {  
            	System.out.printf("for Load Factor: %.1f\n", loadFactor);
    			System.out.println("simulation for linear probing result : Pass (+1)" );
    			TotalMarks= TotalMarks + 1;
    		}
    		else {
    			System.out.printf("for Load Factor: %.1f\n", loadFactor);
    			System.out.println("simulation for linear probing result : Fail (-1)" );
    		}
            //Print load factors and respective # of probes (with formatting)
            System.out.printf("Load Factor : %.1f,  Average # probes : %.3f,  Theoretical # probes (LP) : %.3f \n", loadFactor, probeCount, sL);
            
        }
        
        System.out.println("\n");
        System.out.println("************************************************************************");
       
      //Simulations to measure the average number of probes for successful search for each lambda (0.1~ 0.9)
        System.out.println("***************Test 12 - Probing Simulations for Quadratic Probing Hash Table***************");
        
        Random rQ = new Random();
        int numKeysQ = 100000, valQ, runsQ = 100, duplicatesQ;  //Variables to store # keys, # runs, # duplicate inputs
        double loadFactorQ, countLoopQ, unsLQ, sLQ, probeCountQ = 0,probeunsuccesQ = 0;           //Variables to store load factor, and counting
        System.out.println("************************************************************************");
        
        for(loadFactorQ = 0.1; loadFactorQ <= 0.9; loadFactorQ += 0.1){    //Loop through load factors from 0.1 to 0.9
            for(int j = 0; j < runsQ; j++){                          //Run 100 times for each load factor
               HashTableQuad HTestsQ = new HashTableQuad(numKeysQ, loadFactorQ);     //Initialize new hash table
              
           
            	countLoopQ = 0;
                duplicatesQ = 0;
                for(int k = 0; k < numKeysQ; k++){                   //Insert 100 000 random numbers within bounds of java int type
                	valQ = rQ.nextInt(Integer.MAX_VALUE);
                	 if(valQ<0)
                		 valQ*=-1;
                    if(HTestQ.isIn(valQ))                            //Track duplicate integer inputs
                    {   duplicatesQ++;}
                    
                    countLoopQ += HTestsQ.insertCount(valQ);                    
                     //Count number of probes to insert
                }
                probeCountQ += countLoopQ/(numKeysQ-duplicatesQ);       //Average # probes for table, divided by # insertions              
            }
            probeCountQ /= runsQ;  
            sLQ = (1/loadFactorQ)*(Math.log(1/(1-loadFactorQ)));          //Estimated Calculation of s_Lambda for QP
            
            if ((Math.abs(probeCountQ-sLQ)<.1*sLQ)) {  
            	System.out.printf("for Load Factor: %.1f\n", loadFactorQ);
    			System.out.println("simulation for Quadratic probing result : Pass (+1)" );
    			TotalMarks= TotalMarks + 1;
    		}
    		else {
    			System.out.printf("for Load Factor: %.1f\n", loadFactorQ);
    			System.out.println("simulation for Quadratic probing result : Fail (-1)" );
    		}
            
            System.out.printf("Load Factor : %.1f,  Average # probes : %.3f, Theoretically estimated # of probes: %.3f \n", loadFactorQ, probeCountQ, sLQ );
            
        }
	
        System.out.println("\n");
        System.out.println("************************************************************************");
        
        
	    //Comment out the bonus part if required method is not defined
        //Bonus method starts
        System.out.println("*****Bonus Test 13 - Probing unsuccessful searches Linear Probing Hash Table*****"); 
        System.out.println("\n");
        
        for(loadFactor = 0.1; loadFactor <= 0.9; loadFactor += 0.1){
		
		HashTableLin HTestb = new HashTableLin(numKeys, loadFactor);
	        
	        while(HTestb.getNumKeys()<numKeys)
	        {
	        	val= r.nextInt();
	        	 if(val<0)
            		 val*=-1;
	        	HTestb.insert(val);
	        }
	        System.out.println("# of Keys = " + HTestb.getNumKeys());
	        System.out.println("Table Size = " + HTestb.getTableSize());
	        
	        int total=0, success=0;
            
	        for(int i=0; i<HTestb.getTableSize(); i++)
	        {
	        	val = r.nextInt(Integer.MAX_VALUE);  
	        	 if(val<0)
            		 val*=-1;
	        	 
	         if(HTestb.isIn(val))
	         {   success++;           
	           }	 
	         else {
	         total+= HTestb.searchCount(val);
	         }
	        }
	        
	        probeunsucces=(double)total/(HTestb.getTableSize()-success);
	        unsL=0.5*(1+(1/((1-loadFactor)*(1-loadFactor))));
	        
	        if ((Math.abs(probeunsucces-unsL)<.1*unsL)) {          
    			System.out.printf("for Load Factor: %.1f\n", loadFactor);
    			System.out.println("Bonus: pass(+1)");
    			BonusMarks= BonusMarks + 1;
    		}
    		else {
    			System.out.printf("for Load Factor: %.1f\n", loadFactor);
    			System.out.println("Bonus: fail (-1)");
    		}
	        
	      System.out.printf("Load Factor : %.1f,  Average # probes : %.3f,  Theoretical # probes (LP) : %.3f \n", loadFactor,probeunsucces, unsL);
	    }
      BonusMarks=BonusMarks+1;
      System.out.println("-------------------------------Bonus Marks--------------------------------" );
      System.out.println("\n");
      System.out.println("Bonus marks for test 13: " + BonusMarks + "/10.0");
      //Bonus method ends
        
System.out.println("-------------------------------Total Marks--------------------------------" );
System.out.println("\n");
System.out.println("Total marks for tests (1-12): " + TotalMarks + " /70.0"); 


	}
}

