#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "Questions.h"



char *my_strcat(const char * const str1, const char * const str2){

	/* this is the pointer holding the string to return */
	char *z = NULL;
	
	/*write your implementation here*/
	//allocate memory to output
	int totalsize = strlen(str1)+strlen(str2);
	z = (char *)calloc(totalsize,sizeof(char));

	//adding str1
	int i=0;
	while (*(str1+i)!='\0'){
		*(z+i)=*(str1+i);
		i++;
	}

	//adding str2
	int k = 0;
	while (*(str2+k)!='\0'){
		*(z+i)=*(str2+k);
		i++;
		k++;
	}


	/* finally, return the string*/
	return z;
	
}
