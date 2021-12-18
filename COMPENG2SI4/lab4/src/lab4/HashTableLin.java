package lab4;

public class HashTableLin {
	private Integer[] table;
	private int size;
	private int maxKey;
	private int numKey;
	private double loadNum;
	
	//O(n)
	public HashTableLin(int maxNum, double load) {
		maxKey = maxNum;
		numKey = 0;
		int tempSize = (int)Math.ceil(maxNum/load);
		size = getNextPrime(tempSize);//get the next prime number
		table = new Integer[size];
		loadNum = load; 
		//turn all empty place to -1
		for (int i=0;i<size;i++) {
			table[i]=-1;
		}
	}
	//helper function
	private int getNextPrime(int size) {
		boolean isPrime = false;
		int result = size-1;
		do {
			result++;
			for(int i=2;i<=result;i++) {
				if(i==result) {
					isPrime = true;
					break;
				}
				if(result%i==0)
					break;
			}
		}while(!isPrime);
		return result;
	}
	
	//O(1)
	public void insert(int n) {
		if(n<0)
			return; //cannot insert negative value
		
		insert(n,n);
		if(getNumKeys()>=maxKey) 
			rehash();//rehash to a bigger size first if the array is full
	}
	//helper function
	private void insert(int n,int index) {
		if(table[index%size]<0 || index <0){
			table[index%size] = n;//insert value if found the position
			numKey++;
			return;
		}
		if(table[index%size]==n)//if duplicate do nothing
			return;
		insert(n, index+1);//if not reach yet, go to next position
	}
	
	
	private void rehash() {
		HashTableLin newTable = new HashTableLin(maxKey*2,loadNum);
		for (int i=0;i<size;i++) {
			newTable.insert(table[i]);
		}
		table = newTable.table;
		size = newTable.size;
		maxKey = newTable.maxKey;
	}
	
	//O(1)
	public boolean isIn(int n) {
		if(n<0)
			return false;//no negatice number
		return isIn(n,n);
	}
	//helper function
	private boolean isIn(int n,int index) {
		if(table[index%size]<0 || index<0){
			return false; //if reach the end, not found
		}
		if(table[index%size]==n)
			return true; //if found return, true
		return isIn(n,index+1); //not found or not end, go to next position
	}
	public void printKeys() {
		int size = table.length;
		for(int i = 0;i<size;i++) {
			if(table[i]<0) //is that is empty, dont print
				continue;
			System.out.print(table[i]+" ");
		}
		System.out.print('\n');
	}
	public void printKeysAndIndexes() {
		int size = table.length;
		for(int i = 0;i<size;i++) {
			if(table[i]<0) //is that is empty, dont print
				continue;
			System.out.print("["+i+"]:"+table[i]+" ");
		}
		System.out.print('\n');;
	}
	
	public int insertCount(int n) {
		if(n<0)
			return -1; //cannot insert negative value
		if(getNumKeys()>=maxKey) 
			rehash();//rehash to a bigger size first if the array is full
		return insertCount(n,n)+1;
	}
	//helper function
	private int insertCount(int n,int index) {
		if(table[index%size]<0 || index <0){
			table[index%size] = n;//insert value if found the position
			numKey++;
			return 0;
		}
		if(table[index%size]==n)//if duplicate do nothing
			return 0;
		return insertCount(n, index+1)+1;//if not reach yet, go to next position
	}
	public int searchCount(int n) {
		if(n<0)
			return 0;//no negatice number
		//if(isIn(n)) //susseful will not return
			//return 0;
		return searchCount(n,n)+1;
	}
	//helper function
	private int searchCount(int n,int index) {
		if(table[index%size]<0 || index <0){
			return 0; //if reach the end, not found
		}
		if(table[index%size]==n)
			return 0; //if found return, true
		return searchCount(n,index+1)+1; //not found or not end, go to next position
	}
	
	//assesor
	public int getTableSize() {
		return size;
	}
	public int getNumKeys() {
		return numKey;
	}
	public double getMaxLoadFactor() {
		return loadNum;
	}
	
	
	
}
