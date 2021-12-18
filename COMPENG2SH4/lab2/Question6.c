#include <stdio.h>
#include <stdlib.h>
#include "Questions.h"


void efficient(const int source[], int val[], int pos[], int size){
	//access to each element in source[] and check if they are not 0
	// if not 0, store value in val[] and pos[]
	int count = 0;
	for (int i=0;i<size;i++){
		if (source[i]!=0){
			val[count]=source[i];
			pos[count]=i;
			count++;
		}
	}
	

}

void reconstruct(int source[], int m, const int val[], const int pos[], int n){
	//access to each element in pos[], and assign value to source[]
	for (int i=0;i<n;i++){
		source[pos[i]]=val[i];
	}

}
