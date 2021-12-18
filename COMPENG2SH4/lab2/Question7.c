#include <stdio.h>
#include <stdlib.h>
#include "Questions.h"


void addEff(int val1[], int val2[], int val3[], int pos1[], int pos2[], int pos3[], int k1, int k2){
	//access to each element in pos1[] and check if it has same position in pos2[]
	//if pos2[]has smaller value of position, add val2[] first before add val1[]
	int count=0;
	int i=0;
	int j=0;
	while (i<k1){
		while (pos2[j]<pos1[i]){
			//situation if only val2 can affect this position
			val3[count]=val2[j];
			pos3[count]=pos2[j];
			count++;
			j++;
		}

		//situation if elements in val1 and val2 has same position and not add up to 0
		//add value in val2 and val1 to val3 and continue to next loop
		if (pos2[j]==pos1[i] && (val1[i]+val2[j])!=0){
			val3[count]=val2[j]+val1[i];
			pos3[count]=pos2[j];
			count++;
			j++;
			i++;

		}
		//if add up to 0, skip this is pos1 and pos2
		else if((val1[i]+val2[j])==0){
			i++;
			j++;
		}
		else  {
			//add value val1 to val2
			val3[count]=val1[i];
			pos3[count]=pos1[i];
			count++;
			i++;
		}
	}

	//access to the rest of element in val2
	//since the rest in val2 will have larger pos value than val1, do not need to access val1
	while (j<k2){
		val3[count]=val2[j];
		pos3[count]=pos2[j];
		count++;
		j++;
	}

}
