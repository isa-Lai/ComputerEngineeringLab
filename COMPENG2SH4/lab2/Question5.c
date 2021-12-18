#include <stdio.h>
#include <stdlib.h>
#include "Questions.h"

void string_copy(const char source[], char destination[], int n){
		// copy the first n elements from source[] to destination[]
		for (int i=0;i<(n-1);i++){
			if (source[i]=='\0'){
				break;
			}
			destination[i]=source[i];
		}

	
    
}


