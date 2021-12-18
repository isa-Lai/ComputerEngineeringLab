package lab1;

import java.util.zip.DataFormatException;
import java.math.BigInteger; 
import java.util.Random;


public class RunTime {
	public static void main(String args[]){
		int MAXNUMINTS = 100;
		int MAXRUN = 45000000;
		//Testing HugeInteger
		/*
		for(int i = 10;i<=10000;i*=10) {
			//double addtime = testHugeAdd(i,MAXNUMINTS,MAXRUN/i);
			//System.out.println("HugeInt with n="+i+" add is "+addtime);
			//double subtime = testHugeSub(i,MAXNUMINTS,MAXRUN/i);
			//System.out.println("HugeInt with n="+i+" sub is "+subtime);
			double multime = testHugeMul(i,MAXNUMINTS,MAXRUN/i/i*3);
			System.out.println("HugeInt with n="+i+" mul is "+multime);
			//double comtime = testHugeCom(i,MAXNUMINTS,MAXRUN*15);
			//System.out.println("HugeInt with n="+i+" com is "+comtime);
		}
		for(int i = 500;i<=5000;i*=10) {
			//double addtime = testHugeAdd(i,MAXNUMINTS,MAXRUN/i);
			//System.out.println("HugeInt with n="+i+" add is "+addtime);
			//double subtime = testHugeSub(i,MAXNUMINTS,MAXRUN/i);
			//System.out.println("HugeInt with n="+i+" sub is "+subtime);
			double multime = testHugeMul(i,MAXNUMINTS,MAXRUN/i/i*3);
			System.out.println("HugeInt with n="+i+" mul is "+multime);
			//double comtime = testHugeCom(i,MAXNUMINTS,MAXRUN*15);
			//System.out.println("HugeInt with n="+i+" com is "+comtime);
		}
		*/
		
		//testing BigInteger
		MAXRUN = 30000000;
		for(int i = 1000;i<=10000;i*=10) {
			//double addtime = testBigAdd(i,MAXNUMINTS,MAXRUN/3*5/i);
			//System.out.println("BigInt with n="+i+" add is "+addtime);
			//double subtime = testBigSub(i,MAXNUMINTS,MAXRUN);
			//System.out.println("BigInt with n="+i+" sub is "+subtime);
			//double multime = testBigMul(i,MAXNUMINTS,MAXRUN);
			//System.out.println("BigInt with n="+i+" mul is "+multime);
			double comtime = testBigCom(i,MAXNUMINTS,MAXRUN*15);
			System.out.println("BigInt with n="+i+" com is "+comtime);
			//count++;
		}
		//count = 1;
		for(int i = 500;i<=5000;i*=10) {
			//double addtime = testBigAdd(i,MAXNUMINTS,MAXRUN);
			//System.out.println("BigInt with n="+i+" add is "+addtime);
			//double subtime = testBigSub(i,MAXNUMINTS,MAXRUN);
			//System.out.println("BigInt with n="+i+" sub is "+subtime);
			//double multime = testBigMul(i,MAXNUMINTS,MAXRUN/i/i*3);
			//System.out.println("BigInt with n="+i+" mul is "+multime);
			//double comtime = testBigCom(i,MAXNUMINTS,MAXRUN*15);
			//System.out.println("BigInt with n="+i+" com is "+comtime);
			//count++;
		}
		
	}
	public static double testHugeAdd(int n,int MAXNUMINTS,int MAXRUN) {
		HugeInteger huge1, huge2, huge3;
		long startTime, endTime;
		double runTime = 0.0;
		try {
			for (int numInts=1; numInts<MAXNUMINTS;numInts++) {
				huge1 = new HugeInteger(n);
				huge2 = new HugeInteger(n);
				startTime = System.currentTimeMillis();
				for (int numRun = 0;numRun<MAXRUN;numRun++) {
					huge3 = huge1.add(huge2);
				}
				endTime = System.currentTimeMillis();
				double difference = endTime-startTime;
				runTime +=difference/((double)MAXRUN);
				System.out.println(difference);
			}
			runTime = runTime/((double)MAXNUMINTS);
			return runTime;
		}
		catch (DataFormatException e) {
			return 0.0;
		}
	}
	public static double testHugeSub(int n,int MAXNUMINTS,int MAXRUN) {
		HugeInteger huge1, huge2, huge3;
		long startTime, endTime;
		double runTime = 0.0;
		try {
			for (int numInts=1; numInts<MAXNUMINTS;numInts++) {
				huge1 = new HugeInteger(n);
				huge2 = new HugeInteger(n);
				startTime = System.currentTimeMillis();
				for (int numRun = 0;numRun<MAXRUN;numRun++) {
					huge3 = huge1.subtract(huge2);
				}
				endTime = System.currentTimeMillis();
				double difference = endTime-startTime;
				runTime +=difference/((double)MAXRUN);
				System.out.println(difference);
			}
			runTime = runTime/((double)MAXNUMINTS);
			return runTime;
		}
		catch (DataFormatException e) {
			return 0.0;
		}
	}
	public static double testHugeMul(int n,int MAXNUMINTS,int MAXRUN) {
		HugeInteger huge1, huge2, huge3;
		long startTime, endTime;
		double runTime = 0.0;
		try {
			for (int numInts=1; numInts<MAXNUMINTS;numInts++) {
				huge1 = new HugeInteger(n);
				huge2 = new HugeInteger(n);
				startTime = System.currentTimeMillis();
				for (int numRun = 0;numRun<MAXRUN;numRun++) {
					huge3 = huge1.multiply(huge2);
				}
				endTime = System.currentTimeMillis();
				double difference = endTime-startTime;
				runTime +=difference/((double)MAXRUN);
				System.out.println(difference);
			}
			runTime = runTime/((double)MAXNUMINTS);
			return runTime;
		}
		catch (DataFormatException e) {
			return 0.0;
		}
	}
	public static double testHugeCom(int n,int MAXNUMINTS,int MAXRUN) {
		HugeInteger huge1, huge2;
		int result;
		long startTime, endTime;
		double runTime = 0.0;
		try {
			for (int numInts=1; numInts<MAXNUMINTS;numInts++) {
				huge1 = new HugeInteger(n);
				huge2 = new HugeInteger(n);
				startTime = System.currentTimeMillis();
				for (int numRun = 0;numRun<MAXRUN;numRun++) {
					result = huge1.compareTo(huge2);
				}
				endTime = System.currentTimeMillis();
				double difference = endTime-startTime;
				runTime +=difference/((double)MAXRUN);
				System.out.println(difference);
			}
			runTime = runTime/((double)MAXNUMINTS);
			return runTime;
		}
		catch (DataFormatException e) {
			return 0.0;
		}
	}
	public static double testBigAdd(int num,int MAXNUMINTS,int MAXRUN) {
		BigInteger huge1, huge2, huge3;
		long startTime, endTime;
		double runTime = 0.0;
		for (int numInts=1; numInts<MAXNUMINTS;numInts++) {
			huge1 = new BigInteger(randomstr(num));
			//System.out.println(huge1.toString());
			huge2 = new BigInteger(randomstr(num));
			//System.out.println(huge2.toString());
			startTime = System.currentTimeMillis();
			for (int numRun = 0;numRun<MAXRUN;numRun++) {
				huge3 = huge1.add(huge2);
			}
			endTime = System.currentTimeMillis();
			double difference = endTime-startTime;
			runTime +=difference/((double)MAXRUN);
			System.out.println(difference);
		}
		runTime = runTime/((double)MAXNUMINTS);
		return runTime;

	}
	public static double testBigSub(int num,int MAXNUMINTS,int MAXRUN) {
		BigInteger huge1, huge2, huge3;
		long startTime, endTime;
		double runTime = 0.0;

		for (int numInts=1; numInts<MAXNUMINTS;numInts++) {
			huge1 = new BigInteger(randomstr(num));
			//System.out.println(huge1.toString());
			huge2 = new BigInteger(randomstr(num));
			//System.out.println(huge2.toString());
			startTime = System.currentTimeMillis();
			for (int numRun = 0;numRun<MAXRUN;numRun++) {
				huge3 = huge1.subtract(huge2);
			}
			endTime = System.currentTimeMillis();
			double difference = endTime-startTime;
			runTime +=difference/((double)MAXRUN);
			System.out.println(difference);
		}
		runTime = runTime/((double)MAXNUMINTS);
		return runTime;
	}
	public static double testBigMul(int num,int MAXNUMINTS,int MAXRUN) {
		BigInteger huge1, huge2, huge3;
		long startTime, endTime;
		double runTime = 0.0;

		for (int numInts=1; numInts<MAXNUMINTS;numInts++) {
			huge1 = new BigInteger(randomstr(num));
			//System.out.println(huge1.toString());
			huge2 = new BigInteger(randomstr(num));
			startTime = System.currentTimeMillis();
			for (int numRun = 0;numRun<MAXRUN;numRun++) {
				huge3 = huge1.multiply(huge2);
			}
			endTime = System.currentTimeMillis();
			double difference = endTime-startTime;
			runTime +=difference/((double)MAXRUN);
			System.out.println(difference);
		}
		runTime = runTime/((double)MAXNUMINTS);
		return runTime;
	}
	public static double testBigCom(int num,int MAXNUMINTS,int MAXRUN) {
		BigInteger huge1, huge2;
		int result;
		long startTime, endTime;
		double runTime = 0.0;

		for (int numInts=1; numInts<MAXNUMINTS;numInts++) {
			huge1 = new BigInteger(randomstr(num));
			//System.out.println(huge1.toString());
			huge2 = new BigInteger(randomstr(num));
			startTime = System.currentTimeMillis();
			for (int numRun = 0;numRun<MAXRUN;numRun++) {
				result = huge1.compareTo(huge2);
			}
			endTime = System.currentTimeMillis();
			double difference = endTime-startTime;
			runTime +=difference/((double)MAXRUN);
			System.out.println(difference);
		}
		runTime = runTime/((double)MAXNUMINTS);
		return runTime;
	}
	public static String randomstr(int n) {
		String result = new String();
		Random randNum = new Random();
		//random sign-
		//store random integer into array
		//the first must be larger than 1.
		//System.out.println(num[0]);
		for (int i = 1;i<n;i++) {
			result += randNum.nextInt(8)+1;
			//System.out.println(num[i]);
		}
		result += randNum.nextInt(8)+1;
		return result;
	}
}
