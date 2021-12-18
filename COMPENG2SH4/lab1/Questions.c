
#include "Questions.h"



int Q1_for(){
	int sum = 0;

	// calculate your sum below..this has to use for loop
	int i;
	for (i=30;i<=1000;i++)
	{
		if ((i%4) == 0){
			sum = sum +i;
		}
	}

	// here, we return the calcualted sum:
	return sum;
}
int Q1_while(){
	int sum = 0;


	// calculate your sum below..this has to use while loop
	int i = 30;
	while (i<=1000){
		if ((i%4) == 0){
			sum = sum +i;

		}
		i++;
	}

	// here, we return the calcualted sum:
	return sum;
}
int Q1_do(){
	int sum = 0;
	
	// calculate your sum below..this has to use do-while loop
	int i = 30;
	do{
		if ((i%4) == 0){
			sum = sum +i;

		}
		i++;
	}while(i<=1000);

	  

	// here, we return the calcualted sum:
	return sum;
}
//===============================================================================================
int Q2(int Q2_input){
	
	// this is the flag that is holding whether the input number is 5-digit or not
	int IsFiveDigit = 0;
	
	// Do your coding below to check
	if ((Q2_input<100000 && Q2_input>=10000)||(Q2_input<=-10000 && Q2_input>-100000))
	{
		IsFiveDigit = 1;  //in the range 10000<=x<100000 or -100000<x<=-10000, it is 5 digit
	}



	//finally, we must return IsFiveDigit, which must be 1 is the number is five digits and 0 otherwise
	return IsFiveDigit;
}
//===============================================================================================
int Q3(float Q3_input){
	
	/* Question 3: 
	Write a program that takes a student's Q3_input as an input, which is a floating point value,
	and prints:
	4 if the Q3_input is in the range 90-100,
	3 if it is in the range 80-89, 
	2 if it is in the range 70-79,
	1 if it is in the range 60-69 and
	0 if the Q3_input is between 0 and 59.
	If the Q3_input is not in the range 0-100, the program should print a message to signal an invalid input.
*/	
	// this is the varaible that holds the out value to return
	float GPA;
	
	GPA = 0;
	// Do your coding below to calculate the GPA variable value
	if (Q3_input>=90 && Q3_input <= 100){
		GPA = 4;
	}
	else if(Q3_input>=80 && Q3_input <= 89){
		GPA = 3;
	}
	else if(Q3_input>=70 && Q3_input <= 79){
		GPA = 2;
	}
	else if(Q3_input>=60 && Q3_input <= 69){
		GPA = 1;
	}
	else if(Q3_input>=0 && Q3_input <= 59)
	{
		GPA = 0;
	}
	else{
		GPA = -1;
	}
	

	// finally, we return the GPA
	return GPA;	
}
//===============================================================================================
double Q4(int Q4_input){
	
	// this is the varaible that holds the pi value to return
	double pi=0;
	
	
	// Do your coding below to calculate the pi value
	double i ;
	int sign = 1;
	if (Q4_input>0){


		for (i=1;i<=Q4_input;i++){

			pi = pi+sign*4/(i*2-1); // add the i term
			sign  = sign*(-1);   //change the sign of the next term
		}
	}
	else{
		pi = -1;  //error message for non-positive value
	}
	
	// finally, we return the pi value
	return pi;	
}
//===============================================================================================
int Q5(){
	
	// this is the varaible that holds and the total number of triples to return at the end of the function
	int totNumTribles=0;
	
	
	/* Do your coding below
		Use a triple-nested for loop that simply tries all possibilities
		print each triple on a separate line, 
		and update the total number of triples if needed
	*/
	int side1, side2, side3;
	for (side1=1;side1<=400;side1++){
		for (side2=side1;side2<=400;side2++){
			for (side3=1;side3<=400;side3++){
				if ( side1*side1+side2*side2 == side3*side3){
					totNumTribles +=1;
				}
			}
		}
	}


	//this is the format that you MUST print the found tribles..uncomment this line and fill in the missings to get it working correctly
	//printf("A trible found: side1=%d, side2=%d, side3=%d\n", ...)
	
	//finally you should return the total number of tribles.
	return totNumTribles;
}
//===============================================================================================
int Q6(int Q6_input, int perfect[]){
	
	
	
		//counts is teh variable that should hold the total number of found perfect numbers
		//you should update it within your code and return it at the end
	    int counts=0;


		/*
		*
		*perfect is an array that you need to add into it any perfect number you find
		*which means at the end of this function, the perfect[] array should hold all the found perfect numbers in the range
		*you do not need to return perfect because as stated in lectures arrays are already passed by reference. so, modifying them will 
		*autmoatically reflect in the main calling function.
		*/
	    int i, factor, sum;
	    for (i=1;i< Q6_input;i++){
	    	sum = 0;
	    	for (factor=1;factor<i;factor++){
	    		if (i%factor == 0){
	    			sum += factor;  //search every factors
	    		}
	    	}
	    	if (sum == i){
	    		perfect[counts] = i; //add the perfect number to the array
	    		counts += 1;

	    	}
	    }
		
		
		


	   return counts;

	
}
//===============================================================================================
int Q7a(int Q7_input){
	
	//this is the variable that holds the reversed int.
	int reversedInt=0;
	
	
	// Do your coding logic here
	//Hint: Use the division and remainder operators to separate the number into its individual digits.
	//in this part you can assume the knowledge of the number of digits: seven-digit positive integer
	int input_digit ,reverse_digit, value, leftover,i;
	input_digit = 1000000;
	reverse_digit = 1;
	leftover = Q7_input;

	for (i=0;i<7;i++){
		value = leftover/input_digit;  //find the value of the specific digit
		leftover %= input_digit;		//clear the largest digit for the next loop
		reversedInt += (value * reverse_digit); 	//add value to the result from the lowest digit
		input_digit /= 10;				//for input value, move right to the next digit
		reverse_digit *= 10;			//for output, move left to the next digit
	}

	return reversedInt;
	
}
int Q7b(int Q7b_input){
	
	//this is the variable that holds the reversed int.
	int reversedInt=0;
	
	

	// Do your coding logic here
	int input_digit ,reverse_digit,numberDigit, value, leftover,i;
	numberDigit = 1;
	input_digit = 1;
	reverse_digit = 1;
	leftover = Q7b_input;

	while (Q7b_input/input_digit>=10){
		numberDigit++;
		input_digit *= 10;  //determine the number of digit of input
	}

	for (i=0;i<numberDigit;i++){
		value = leftover/input_digit;   //same function of the previous function
		leftover %= input_digit;
		reversedInt += (value * reverse_digit);
		input_digit /= 10;
		reverse_digit *= 10;
	}

	//Hint: Use the division and remainder operators to separate the number into its individual digits.
	//this part should be generalized for any number of allowed digits
	
	
	
	return reversedInt;

	
	
	
}
