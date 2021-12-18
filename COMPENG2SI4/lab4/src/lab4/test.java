package lab4;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashTableLin testlin= new HashTableLin(7,0.5);
		System.out.println("size "+testlin.getTableSize());
		//testlin.insert(1);
		//testlin.insert(48);
		testlin.insert(26);
		testlin.insert(39);
		testlin.insert(4);
		testlin.insert(9);
		testlin.insert(26);
		testlin.insert(43);
		System.out.println("numKey "+testlin.getNumKeys());
		testlin.printKeysAndIndexes();
		System.out.println("size "+testlin.getTableSize());
		System.out.println("5? "+testlin.isIn(5)+testlin.insertCount(5)+testlin.isIn(5));
		System.out.println("9? "+testlin.isIn(9)+testlin.insertCount(9));
		
		System.out.println("now test quad");
		HashTableQuad testqua= new HashTableQuad(7,0.5);
		System.out.println("size "+testqua.getTableSize());
		testqua.insert(1);
		testqua.insert(48);
		testqua.insert(26);
		testqua.insert(39);
		testqua.insert(4);
		testqua.insert(9);
		testqua.insert(26);
		testqua.insert(43);
		testqua.insert(12);
		System.out.println("numKey "+testqua.getNumKeys());
		testqua.printKeysAndIndexes();
		System.out.println("size "+testqua.getTableSize());
		System.out.println("5? "+testqua.isIn(5)+testqua.insertCount(5)+testqua.isIn(5));
		System.out.println("43? "+testqua.isIn(43)+testqua.insertCount(43));
		
	}

}
