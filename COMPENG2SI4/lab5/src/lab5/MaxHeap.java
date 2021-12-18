//yuying Lai 400268588
package lab5;



public class MaxHeap {
	private Integer[] heap;
	private int size;
	private int MAXSIZE;
	
	public MaxHeap(int arraySize) {
		size = 0;
		MAXSIZE = arraySize;
		heap = new Integer[MAXSIZE];
	}
	
	//O(n)
	public MaxHeap(Integer[] someArray) {
		size = 0;
		MAXSIZE = someArray.length;
		heap = new Integer[MAXSIZE];
		for(int i=0;i<someArray.length;i++)
			insert(someArray[i]); //inseat every element into this
		
	}

	//Time
	//O(log(n))
	//if reallocate O(n)
	
	//space: two extra int O(1)
	//space is reallocate: +1array O(n)
	public void insert(int n) {
		//double the size
		if(size == MAXSIZE) {
			MAXSIZE *=2;
			Integer[] newtable= new Integer[MAXSIZE];
			for(int i=0;i<size;i++)
				newtable[i] = heap[i]; //inseat every element into this
			heap = newtable;
		}
		
		
		int current = size;
		int parent = (size-1)/2; //find index of parent
		Integer temp;
		heap[size] =n;
		size++;
		while(heap[current]>heap[parent]) {
			//if current is bigger, swap
			temp = heap[current];
			heap[current] = heap[parent];
			heap[parent] = temp;
			//move up
			current = parent;
			parent = (current-1)/2;
		}
		
	}
	
	//time: O(log(n))
	//space: O(1)
	private int deleteMax() {
		int value = heap[0];
		heap[0] = heap[--size];//chose the last element
		
		//push element in the right position
		int index = 0;
		int left = 1;
		int right = 2;
		while(heap[index]<heap[left]||heap[index]<heap[right]) {
			//reach the end line
			
			//choose the largest to swap;
			//the left or right has to be in the heap table, since I am not deleting the data, data still exist but it has to be ignored.
			if(heap[left]>heap[right]&&left<size) {
				Integer temp = heap[index];
				heap[index] = heap[left];
				heap[left] = temp;
				index = left;
			}
			else if(right<size){
				Integer temp = heap[index];
				heap[index] = heap[right];
				heap[right] = temp;
				index = right;
			}
			left = index*2+1;
			right = index*2+2;
			if(index>=size/2)
				break;
		}
		return value;
	}
	
	//build heap O(n) delet n max O(nlog(n))
	public static void heapsort(Integer[] arrayToSort) {
		MaxHeap result = new MaxHeap(arrayToSort);
		for (int i=0;i<arrayToSort.length;i++) {
			arrayToSort[i] = result.deleteMax(); //put every max value into arrayToSort
		}
		
		
	}
	public String toString() {
		String result = new String();
		for(int i=0;i<size;i++) {
			result= result+heap[i]+",";
		}
		
		return result;
	}
	
	//accessor
	public int getSizeArr() {
		return MAXSIZE;
	}

	public int getSizeHeap() {
		return size;
	}

	public Integer[] getHeap() {
		return heap;
	}
}
