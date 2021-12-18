package lab4;

public class HashTableQuad {
	private Integer[] table;
	private int size;
	private int maxKey;
	private int numKey;
	private double loadNum;
	public HashTableQuad(int maxNum, double load) {
		maxKey = maxNum;
		int tempSize = (int)Math.ceil(maxNum/load);
		size = getNextPrime(tempSize);//get the next prime number
		table = new Integer[size];
		numKey = 0;
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
	
	public void insert(int n) {
		if(n<0)
			return; //cannot insert negative value
		
		
		insert(n,0);
		if(getNumKeys()>=maxKey) 
			rehash();//rehash to a bigger size first if the array is full
	}
	//helper function
	private void insert(int n,int count) {
		int index = n+(count)*(count);
		if(table[index%size]<0 || index <0){
			table[index%size] = n;//insert value if found the position
			numKey++;
			return;
		}
		if(table[index%size]==n)//if duplicate do nothing
			return;
		insert(n, count+1);//if not reach yet, go to next position
	}
	private void rehash() {
		HashTableQuad newTable = new HashTableQuad(maxKey*2,loadNum);
		for (int i=0;i<size;i++) {
			newTable.insert(table[i]);
		}
		table = newTable.table;
		size = newTable.size;
		maxKey = newTable.maxKey;
	}
	
	public boolean isIn(int n) {
		if(n<0)
			return false;//no negatice number
		return isIn(n,0);
	}
	//helper function
	private boolean isIn(int n,int count) {
		int index = n+(count)*(count);
		if(table[index%size]<0 || index<0){
			return false; //if reach the end, not found
		}
		if(table[index%size]==n)
			return true; //if found return, true
		return isIn(n, count+1); //not found or not end, go to next position
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
		return insertCount(n,0)+1;
	}
	//helper function
	private int insertCount(int n,int count) {
		int index = n+(count)*(count);
		if(table[index%size]<0 || index <0){
			table[index%size] = n;//insert value if found the position
			numKey++;
			return 0;
		}
		if(table[index%size]==n)//if duplicate do nothing
			return 0;
		return insertCount(n, count+1)+1;//if not reach yet, go to next position
	}
	public int searchCount(int n) {
		if(n<0)
			return 0;//no negatice number
		//if(isIn(n)) //succuss will not return
			//return 0;
		return searchCount(n,0)+1;
	}
	//helper function
	private int searchCount(int n,int count) {
		int index = n+(count)*(count);
		if(table[index%size]<0 || index <0){
			return 0; //if reach the end, not found
		}
		if(table[index%size]==n)
			return 0; //if found return, true
		return searchCount(n, count+1)+1; //not found or not end, go to next position
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
