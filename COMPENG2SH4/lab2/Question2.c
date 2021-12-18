#include <stdio.h>
#include <stdlib.h>

#include "Questions.h"
#include "math.h"


int is_diag_dom(int mat[][N2]){

    //this is the flag that is returned to tell whether the mat is diagonally
	int isDiag=0;
	
	//write your code below
	//Return 1 if the matrix is diagonally dominant and 0 otherwise
	
	for (int i=0;i<N2;i++){
		int sum = 0;
		for (int j=0;j<N2;j++){
			if(i ==j){
				continue; //calculate the sum in each row except the diagonal element
			}
			sum += fabs(mat[i][j]);
		}
		if (fabs(mat[i][i])>sum){
			isDiag = 1; //if this row match the requirement, temporally set to true
		}
		else{
			isDiag=0; // if one row does not match the requirement, set to false and quit this loop
			break;
		}
	}


    return isDiag;
}
