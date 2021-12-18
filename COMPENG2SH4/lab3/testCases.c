#include <assert.h>
#include <setjmp.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "CuTest.h"
#include "Questions.h"
    


   
    
//=========Question 1==================================
	void TestQ1_two_strings(CuTest *tc) {
		
		CuAssertStrEquals(tc, "HelloWorld!", my_strcat("Hello","World!"));
	}
    
	void TestQ1_second_empty(CuTest *tc) {
		
		CuAssertStrEquals(tc, "Hello", my_strcat("Hello",""));
	}
	void TestQ1_first_empty(CuTest *tc) {
		
		CuAssertStrEquals(tc, "World!", my_strcat("","World!"));
	}
	void TestQ1_empty(CuTest *tc) {

		CuAssertStrEquals(tc, "", my_strcat("",""));
	}
    
//===========================================================
//=================Question 2================================
	void TestQ2_find_notThere(CuTest *tc) {

		char inputFile[] =  "students.txt";
		int size;
		//create list using the input file
		student **Clist = create_class_list(inputFile,&size);

		int expected = -1;
		int actual = find(0011,Clist,size);

		CuAssertIntEquals(tc, expected, actual);

	}

	void TestQ2_find_There(CuTest *tc) {

		char inputFile[] =  "students.txt";
		int size;
		//create list using the input file
		student **Clist = create_class_list(inputFile,&size);

		int expected = 4;
		int actual = find(9000,Clist,size);

		CuAssertIntEquals(tc, expected, actual);

	}

	void TestQ2_inputGradesAndCalculateFinal(CuTest *tc) {

		

		char inputFile[] =  "students.txt";
		char gradesFile[] =  "grades.txt";
		int size;
		//create list using the input file
		student **Clist = create_class_list(inputFile,&size);
		//input grades using the grades input file
		input_grades(gradesFile,Clist,size);
		//calculate final grades
		compute_final_course_grades(Clist,size);
		//Output final course marks
		output_final_course_grades("outputGrades.txt",Clist,size);

		//now we open the golden file with the written file and compare line by line

		char actualFile[] =  "outputGrades.txt";
		char goldenFile[] =  "output.txt";

		char actual[1000], expected[1000];
		int actualID,expectedID;
		double actualFinalGrade,expectedFinalGrade;
		FILE *actualPtr, *expectedPtr;
		if ((actualPtr = fopen(actualFile, "r")) == NULL || (expectedPtr = fopen(goldenFile, "r")) == NULL) {
			printf("Error! opening file");
			// Program exits if file pointer returns NULL.
			exit(1);
		}

		int actualNumStudents,expectedNumStudents;


		fscanf(actualPtr,"%d",&actualNumStudents);
		
		fscanf(expectedPtr,"%d",&expectedNumStudents);
		
		CuAssertIntEquals(tc, expectedNumStudents, actualNumStudents);

		int i;
		for(i=0;i<expectedNumStudents;i++){
		//while (actualPtr != NULL && expectedPtr!=NULL){
			// reads text until newline is encountered
		/*	fscanf(actualPtr, "%[^\n]", actual);
			printf("%s ", actual);
			fscanf(expectedPtr, "%[^\n]", expected);
			printf("%s\n", expected);

			CuAssertStrEquals(tc, expected, actual);

		*/
			fscanf(actualPtr,"%d",&actualID);
			
			fscanf(expectedPtr,"%d",&expectedID);
			
			CuAssertIntEquals(tc, expectedID, actualID);

			fscanf(actualPtr,"%lf",&actualFinalGrade);
			fscanf(expectedPtr,"%lf",&expectedFinalGrade);
			CuAssertDblEquals(tc, expectedFinalGrade, actualFinalGrade,0.000001);
		}

		if ((actualPtr == NULL && expectedPtr!=NULL)|| (actualPtr != NULL && expectedPtr==NULL)){

			printf("files are not of equal size");
			// Program exits if file pointer returns NULL.
			exit(1);

		}

	}


	void TestQ2_test_of_all_function(CuTest *tc) {



			char inputFile[] =  "student_test.txt";
			char gradesFile[] =  "grades_test.txt";
			int size;
			//create list using the input file
			student **Clist = create_class_list(inputFile,&size);
			//input grades using the grades input file
			input_grades(gradesFile,Clist,size);
			//calculate final grades
			compute_final_course_grades(Clist,size);
			//withdraw three times
			withdraw(8000,Clist,&size); //should not found
			withdraw(3000,Clist,&size);
			withdraw(3400,Clist,&size);
			//Output final course marks
			output_final_course_grades("outputGrades_test.txt",Clist,size);

			//now we open the golden file with the written file and compare line by line

			char actualFile[] =  "outputGrades_test.txt";
			char goldenFile[] =  "output_test.txt";

			char actual[1000], expected[1000];
			int actualID,expectedID;
			double actualFinalGrade,expectedFinalGrade;
			FILE *actualPtr, *expectedPtr;
			if ((actualPtr = fopen(actualFile, "r")) == NULL || (expectedPtr = fopen(goldenFile, "r")) == NULL) {
				printf("Error! opening file");
				// Program exits if file pointer returns NULL.
				exit(1);
			}

			int actualNumStudents,expectedNumStudents;


			fscanf(actualPtr,"%d",&actualNumStudents);

			fscanf(expectedPtr,"%d",&expectedNumStudents);

			CuAssertIntEquals(tc, expectedNumStudents, actualNumStudents);

			int i;
			for(i=0;i<expectedNumStudents;i++){

				fscanf(actualPtr,"%d",&actualID);

				fscanf(expectedPtr,"%d",&expectedID);

				CuAssertIntEquals(tc, expectedID, actualID);

				fscanf(actualPtr,"%lf",&actualFinalGrade);
				fscanf(expectedPtr,"%lf",&expectedFinalGrade);
				CuAssertDblEquals(tc, expectedFinalGrade, actualFinalGrade,0.000001);
			}

			if ((actualPtr == NULL && expectedPtr!=NULL)|| (actualPtr != NULL && expectedPtr==NULL)){

				printf("files are not of equal size");
				// Program exits if file pointer returns NULL.
				exit(1);

			}

//destroy list now
			destroy_list(Clist,&size);
			output_final_course_grades("outputGrade_des.txt",Clist,size);

			//now we open the golden file with the written file and compare line by line

			char actualFile_des[] =  "outputGrade_des.txt";
			char goldenFile_des[] =  "output_des.txt";


			if ((actualPtr = fopen(actualFile_des, "r")) == NULL || (expectedPtr = fopen(goldenFile_des, "r")) == NULL) {
				printf("Error! opening file");
				// Program exits if file pointer returns NULL.
				exit(1);
			}


			fscanf(actualPtr,"%d",&actualNumStudents);

			fscanf(expectedPtr,"%d",&expectedNumStudents);

			CuAssertIntEquals(tc, expectedNumStudents, actualNumStudents);

			for(i=0;i<expectedNumStudents;i++){

				fscanf(actualPtr,"%d",&actualID);

				fscanf(expectedPtr,"%d",&expectedID);

				CuAssertIntEquals(tc, expectedID, actualID);

				fscanf(actualPtr,"%lf",&actualFinalGrade);
				fscanf(expectedPtr,"%lf",&expectedFinalGrade);
				CuAssertDblEquals(tc, expectedFinalGrade, actualFinalGrade,0.000001);
			}

			if ((actualPtr == NULL && expectedPtr!=NULL)|| (actualPtr != NULL && expectedPtr==NULL)){

				printf("files are not of equal size");
				// Program exits if file pointer returns NULL.
				exit(1);

			}





		}
//===========================================================
//=================Question 3================================  
	void TestQ3_readandSort1(CuTest *tc) {

		char inputFile[] =  "wordlist.txt";
		int size;
		//create list using the input file
		char **actualList = read_words(inputFile,&size);
		sort_words(actualList,size);

		char *expectedList[]={"apple","banana","hello","milan","programming","zebra"};
		
		int i;
		for (i=0;i<size;i++)
			CuAssertStrEquals(tc, expectedList[i], actualList[i]);

	}

	void TestQ3_readandSort2(CuTest *tc) {

		char inputFile[] =  "wordlist.txt";
		int size;
		//create list using the input file
		char **actualList = read_words(inputFile,&size);
		sort2_words(actualList,size);

		char *expectedList[]={"apple","banana","hello","milan","programming","zebra"};

		

		int i;
		for (i=0;i<size;i++)
			CuAssertStrEquals(tc, expectedList[i], actualList[i]);

	}

	void TestQ3_readandSort1_test(CuTest *tc) {

		char inputFile[] =  "wordlist_test.txt";
		int size;
		//create list using the input file
		char **actualList = read_words(inputFile,&size);
		sort_words(actualList,size);

		char *expectedList[]={"compeng","eleceng","engineering","mcmaster","programming"};

		int i;
		for (i=0;i<size;i++)
			CuAssertStrEquals(tc, expectedList[i], actualList[i]);

	}

	void TestQ3_readandSort2_test(CuTest *tc) {

		char inputFile[] =  "wordlist_test.txt";
		int size;
		//create list using the input file
		char **actualList = read_words(inputFile,&size);
		sort2_words(actualList,size);

		char *expectedList[]={"compeng","eleceng","engineering","mcmaster","programming"};



		int i;
		for (i=0;i<size;i++)
			CuAssertStrEquals(tc, expectedList[i], actualList[i]);

	}
 

    
  
//===========================================================
	CuSuite* Lab3GetSuite() {
		CuSuite* suite = CuSuiteNew();
		SUITE_ADD_TEST(suite, TestQ1_two_strings); 
		SUITE_ADD_TEST(suite, TestQ1_second_empty);
		SUITE_ADD_TEST(suite, TestQ1_first_empty);
		SUITE_ADD_TEST(suite, TestQ1_empty);


		SUITE_ADD_TEST(suite, TestQ2_find_notThere);
		SUITE_ADD_TEST(suite, TestQ2_find_There);
		SUITE_ADD_TEST(suite, TestQ2_inputGradesAndCalculateFinal);
		SUITE_ADD_TEST(suite, TestQ2_test_of_all_function);


		SUITE_ADD_TEST(suite, TestQ3_readandSort1);
		SUITE_ADD_TEST(suite, TestQ3_readandSort2);
		SUITE_ADD_TEST(suite, TestQ3_readandSort1_test);
		SUITE_ADD_TEST(suite, TestQ3_readandSort2_test);
        
       

		return suite;
	}
    
