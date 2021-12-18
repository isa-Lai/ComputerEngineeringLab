#include <stdio.h>
#include <stdlib.h>

#include "Questions.h"

void diag_scan(int mat [][N3], int arr []){
	int count = 0;
	//situation when the first diagonal element is in the first column
	for (int i=0;i<N3;i++){
		int row = i;
		for (int j = 0;j<=i;j++){
			arr[count] = mat[row][j]; //each time will move 1 row up and 1 column right
			count++;  //the storage address will move to the next element in arr
			row--;
		}
	}

	//situation when the first diagonal element is in the last row
	for (int i=1;i<N3;i++){
		int row = N3-1;
		for (int j = i;j<N3;j++){
			arr[count] = mat[row][j]; // same movement as above.
			count++;
			row--;
		}
	}

}
