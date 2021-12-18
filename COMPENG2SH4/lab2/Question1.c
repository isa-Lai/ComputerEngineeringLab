#include <stdio.h>
#include <stdlib.h>

#include<math.h>
#include "Questions.h"



void add_vectors(double vector1[],double vector2[],double vector3[],int size)
{
    /*vector3 should store the sum of vector1 and vector2. 
	You may assume that all three arrays have the size equal to n
	*/
	//write your code below
	for (int i=0; i<size ;i++){
		vector3[i] = vector1[i]+vector2[i]; //each element in vector3 is equal to the element in vector1+element in vector2
	}
	
}

double scalar_prod(double vector1[],double vector2[],int size)
{
	// this is the variable holding the scalar product of the two vectors
    double prod=0;

	//write your code below to calculate the prod value
	for (int i=0;i<size;i++){
		prod += (vector1[i]*vector2[i]); //calculate the product of each element and add to the variable prod
	}
    

	
	// return the result
    return prod;
}

double norm2(double vector1[], int size)
{
	//this is the variable holding the L2 norm of the passed vector
    double L2;
	
    
	//write your code here
	// you should call function scalar_prod().
	L2 = sqrt(scalar_prod(vector1,vector1,size)); // square root of the scalar product.
	//finally, return the L2 norm 
    return L2;
}
